package com.example.one

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.one.vm.PlayerViewModel
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.rememberNavController
import com.example.one.data.SharedPreferences.MySharedPreference
import com.example.one.nav.MainScreen
import com.example.one.player.ExoMusicPlayerManager
import com.example.one.helper.LocalDpHelper
import com.example.one.helper.NoticeManager
import com.example.one.player.AudioController
import com.example.one.timer.TimerAnimatorController
import com.example.one.ui.theme.ONETheme
import com.example.one.vm.AppViewModel
import com.example.one.vm.DataAnalyseViewModel
import com.example.one.vm.HotMapViewModel
import com.example.one.vm.MyAudioViewModelFactory
import com.example.one.vm.MyDataAnalyseViewModelFactory
import com.example.one.vm.MyHotMapViewModelFactory
import com.example.one.vm.NoticeViewModel
import com.example.one.vm.SPViewModel
import com.example.one.vm.TimerViewModel

class MainActivity : ComponentActivity(),SensorEventListener {

    private val appViewModel = AppViewModel.getInstance()
    private val timerViewModel:TimerViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by viewModels{ MyAudioViewModelFactory(this.application) }
    private val hotMapViewModel:HotMapViewModel by viewModels{MyHotMapViewModelFactory(this.application)}
    private val dataAnalyseViewModel:DataAnalyseViewModel by viewModels {MyDataAnalyseViewModelFactory(this.application)  }
    private val noticeViewModel:NoticeViewModel by viewModels()
    private val sPViewModel: SPViewModel by viewModels()
    // 传感器及常量
    private lateinit var sensorManager: SensorManager
    private var gravitySensor: Sensor? = null
    private var originalBrightness: Float = 0.5f // 默认值

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 保存亮度设置
        saveBrightness()
        // 在窗口上设置FLAG_KEEP_SCREEN_ON标志以防止屏幕自动关闭
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 初始化传感器
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_UI)
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        // 初始化部分工具类
        LocalDpHelper.init(this)
        ExoMusicPlayerManager.initializeExoPlayer(this)
        MySharedPreference.initSharedPreferences(this)
        TimerAnimatorController.init(timerViewModel,hotMapViewModel,dataAnalyseViewModel,sPViewModel)
        AudioController.init(playerViewModel)
        NoticeManager.init(dataAnalyseViewModel, noticeViewModel,this)

        setContent {
            ONETheme(
                darkTheme =
            if(appViewModel.ifFollowSystem.value){
                isSystemInDarkTheme()
            }else{
                appViewModel.ifDark.value
            }) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ONEApp()
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        super.onDestroy()
        // 释放资源，避免java野指针
        timerViewModel.animatorController.stop()
//        ExoMusicPlayerManager.releasePlayer()
//        ExoVideoPlayerManager.releasePlayer()
        AudioController.release()
//        videoPlayerViewModel.onRelease()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            val gravityZ = event.values[2]

            // 如果手机倒扣在桌子上，重力向量的Z分量应接近-9.8（重力朝向手机的正面）
            if (gravityZ < -9) {
                // 倒扣
                appViewModel.setAppUpSetDown()
                setAppScreenBrightness(0f)
                // 没有倒扣
            } else {
                appViewModel.setAppNotUpsetDown()
                resetBrightness()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // 不需要实现
    }

    /**
     * @since 2024/5/14
     * 保存window亮度设置
     */
    private fun saveBrightness(){
        val window = window;
        val lp:WindowManager.LayoutParams  = window.attributes;
        originalBrightness = lp.screenBrightness
    }

    /**
     * @since 2024/5/14
     * 将window亮度设置为指定值
     */
    private fun setAppScreenBrightness(birghtessValue:Float) {
        val window = window;
        val lp:WindowManager.LayoutParams  = window.attributes;
        lp.screenBrightness = birghtessValue;
        window.setAttributes(lp);
    }

    /**
     * @since 2024/5/14
     * 将window恢复原始亮度
     */
    private fun resetBrightness(){
        setAppScreenBrightness(originalBrightness)
    }
}

@Composable
fun ONEApp(){
    // 定义导航
    val mainNavController = rememberNavController()
    MainScreen(navController = mainNavController)
}