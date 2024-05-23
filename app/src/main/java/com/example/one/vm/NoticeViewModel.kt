package com.example.one.vm

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one.data.SharedPreferences.MySharedPreference
import com.example.one.data.SharedPreferences.NOTICE_HOUR
import com.example.one.data.SharedPreferences.NOTICE_MINUTE
import com.example.one.helper.NoticeManager
import kotlinx.coroutines.launch
import kotlin.math.min

/**
 * @since 2025/5/19
 */
class NoticeViewModel: ViewModel() {

    var hour:Int = MySharedPreference.getInt(NOTICE_HOUR)
    var minute:Int = MySharedPreference.getInt(NOTICE_HOUR)
    /**
     * 用于调用NoticeManager设定定时通知
     */
    private fun setNoticeSchedule(context: Context,hour:Int,minute:Int){
        viewModelScope.launch {
            if(ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED)
            {
                return@launch
            }else{
                NoticeManager.scheduleNotification(context,hour,minute)
            }
        }
    }

    /**
     * 用于调用NoticeManager取消定时通知
     */
    fun cancelNoticeSchedule(context: Context)
    {
        viewModelScope.launch {
            NoticeManager.cancelScheduledNotification(context)
        }
    }

    /**
     * 设置时间
     */
    fun setTime(hour:Int,minute:Int)
    {
        viewModelScope.launch {
            this@NoticeViewModel.hour = hour
            this@NoticeViewModel.minute = minute
        }
    }
    /**
     * 自动设置时间，通过viewModel保存的时间
     */
    fun autoSetNoticeSchedule(context: Context){
        viewModelScope.launch {
            // 保存到设置
            MySharedPreference.editIntData(NOTICE_HOUR,this@NoticeViewModel.hour)
            MySharedPreference.editIntData(NOTICE_MINUTE,this@NoticeViewModel.minute)
            setNoticeSchedule(context,
                this@NoticeViewModel.hour,
                this@NoticeViewModel.minute)
        }
    }
}