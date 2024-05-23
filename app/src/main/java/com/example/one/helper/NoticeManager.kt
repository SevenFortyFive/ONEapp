package com.example.one.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.one.R
import com.example.one.vm.DataAnalyseViewModel
import com.example.one.vm.NoticeViewModel
import kotlinx.coroutines.runBlocking
import java.util.Calendar

const val CHANNEL_ID = "ONE_CHANNEL_ID"
const val NOTIFICATION_ID = 101
const val REQUEST_CODE = 102
/**
 * @since 2024/5/19
 * 定时通知的业务类
 */
object NoticeManager {
    // 用于获取通知信息
    private var dataAnalyseViewModel:DataAnalyseViewModel? = null
    private var noticeViewModel:NoticeViewModel? = null

    /**
     * 初始化获取viewModel引用
     */
    fun init(dataAnalyseViewModel: DataAnalyseViewModel,noticeViewModel: NoticeViewModel,context: Context){
        Log.d("NoticeManager","NoticeManager Init")
        this.dataAnalyseViewModel = dataAnalyseViewModel
        this.noticeViewModel = noticeViewModel
        createNotificationChannel(context)
    }

    /**
     * 创建noticeChannel
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel(context: Context) {
        Log.d("NoticeManager","Channel set")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MyChannel"
            val descriptionText = "My Notification Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                CHANNEL_ID,
                name,
                importance).apply {
                description = descriptionText
            }.apply {
                description = "My Lock Screen Notification Channel"
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC // 设置锁屏可见性
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * 设置通知时间
     */
    @SuppressLint("ServiceCast", "ScheduleExactAlarm")
    fun scheduleNotification(context: Context, hour: Int, minute: Int) {
        // 取消之前的定时器
        cancelScheduledNotification(context)
        Log.d("NoticeManager", "Schedule set $hour $minute")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val intent = Intent(context, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE, // 使用相同的requestCode
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    /**
     * 取消之前的定时器
     */
    fun cancelScheduledNotification(context: Context) {
        Log.d("NoticeManager","Cancel ScheduleNotification with code $REQUEST_CODE")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun sendNotification(context: Context) {
        Log.d("NoticeManager","sendNotification")
        if(dataAnalyseViewModel != null)
        {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(dataAnalyseViewModel!!.getNoticeTitle())
                // sunningBlock顺序执行
                .setContentText(runBlocking { dataAnalyseViewModel!!.getMessage() })
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }
}


/**
 * BroadcastReceiver用于系统定时唤醒之后执行工作
 */
class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // 在这里处理定时器触发的广播事件
        // 通常在这里调用发送通知的函数
        if (context != null) {
            NoticeManager.sendNotification(context)
        }
    }
}