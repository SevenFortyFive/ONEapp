package com.example.one.timer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.util.Log
import android.view.animation.LinearInterpolator
import com.example.one.data.MainPageState
import com.example.one.data.SQLite.entity.MyBreathData
import com.example.one.data.SQLite.entity.MyClockData
import com.example.one.data.SQLite.entity.MyMeditationData
import com.example.one.data.SharedPreferences.BALANCE
import com.example.one.data.SharedPreferences.BREATH_TIME_KEY
import com.example.one.data.SharedPreferences.CLOCK_TIME_KEY
import com.example.one.data.SharedPreferences.DRINK_TIME_KEY
import com.example.one.data.SharedPreferences.MEDITATION_TIME_KEY
import com.example.one.data.SharedPreferences.SharedPreferencesHelper
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentHour
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentYear
import com.example.one.timer.TimerStatus.PausedStatus
import com.example.one.timer.TimerStatus.StartedStatus
import com.example.one.timer.TimerStatus.NotStartedStatus
import com.example.one.timer.TimerStatus.CompletedStatus
import com.example.one.vm.AppViewModel
import com.example.one.vm.DataAnalyseViewModel
import com.example.one.vm.HotMapViewModel
import com.example.one.vm.SPViewModel
import com.example.one.vm.TimerViewModel
import kotlin.math.ceil

const val SPEED = 100

object TimerAnimatorController{
    private var timerViewModel:TimerViewModel? = null
    private var hotMapViewModel:HotMapViewModel? = null
    private var dataAnalyseViewModel:DataAnalyseViewModel? = null
    private var sPViewModel:SPViewModel? = null


    fun init(
        timerViewModel: TimerViewModel,
        hotMapViewModel: HotMapViewModel,
        dataAnalyseViewModel: DataAnalyseViewModel,
        sPViewModel: SPViewModel
    ){
        this.timerViewModel = timerViewModel
        this.hotMapViewModel = hotMapViewModel
        this.dataAnalyseViewModel = dataAnalyseViewModel
        this.sPViewModel = sPViewModel
    }

    private var valueAnimator: ValueAnimator? = null
    // 记录这次的value，用于统计时间
    private var thisTimeValue = 0

    fun startClock() {
        Log.d("start","try to start")
        if(timerViewModel != null)
        {
            if (timerViewModel!!.totalTime == 0L) return
            if (valueAnimator == null) {
                // Animator: totalTime -> 0
                valueAnimator = ValueAnimator.ofInt(timerViewModel!!.totalTime.toInt() * SPEED, 0)
                valueAnimator?.interpolator = LinearInterpolator()
                this.thisTimeValue = timerViewModel!!.totalTime.toInt()
                // Update timeLeft in ViewModel
                valueAnimator?.addUpdateListener {
                    timerViewModel!!.animValue = (it.animatedValue as Int) / SPEED.toFloat()
                    timerViewModel!!.timeLeft = ceil(it.animatedValue as Int / SPEED.toFloat()).toLong()
                }
                valueAnimator?.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        // 减去剩余时间
                        Log.d("animator","end")
                        complete()
                    }
                })
            } else {
                thisTimeValue = timerViewModel!!.totalTime.toInt()
                valueAnimator?.setIntValues(timerViewModel!!.totalTime.toInt() * SPEED, 0)
            }
            // (LinearInterpolator + duration) aim to set the interval as 1 second.
            valueAnimator?.duration = timerViewModel!!.totalTime * 1000L
            valueAnimator?.start()
            // 标记繁忙状态
            if(AppViewModel.getInstance().ifHasTask.value)
            {
                AppViewModel.getInstance().setAppBusy()
            }
            timerViewModel!!.status = StartedStatus(timerViewModel!!)
        }

    }

    fun pause() {
        Log.d("pause","try to pause")
        if(timerViewModel != null)
        {
            valueAnimator?.pause()
            timerViewModel!!.status = PausedStatus(timerViewModel!!)
            Log.d("notice","no complete")
        }
    }

    fun resume() {
        Log.d("resume","try to resume")
        if(timerViewModel != null)
        {
            valueAnimator?.resume()
            timerViewModel!!.status = StartedStatus(timerViewModel!!)
        }
    }

    fun stop() {
        Log.d("stop","try to stop")
        if(timerViewModel != null)
        {
            valueAnimator?.cancel()
            timerViewModel!!.timeLeft = 0
            timerViewModel!!.status = NotStartedStatus(timerViewModel!!)
        }
    }

    /**
     * 计数完成时调用
     */
    fun complete() {
        Log.d("complete","try to complete")
        Log.d("left time", timerViewModel!!.timeLeft.toString())
        AppViewModel.getInstance().setAppFree()
        if(timerViewModel != null &&  timerViewModel!!.timeLeft > 10)
        {
            timerViewModel!!.totalTime = 0
            timerViewModel!!.status = CompletedStatus(timerViewModel!!)
            AppViewModel.getInstance().setAppFree()
            thisTimeValue = 0
        }
        else if(timerViewModel != null)
        {
            timerViewModel!!.totalTime = 0
            timerViewModel!!.status = CompletedStatus(timerViewModel!!)
            // 调用热图viewmodel修改数据
            hotMapViewModel!!.update()
            // 恢复空闲状态
            AppViewModel.getInstance().setAppFree()
            when(hotMapViewModel!!.type.value)
            {
                MainPageState.DRINK ->{
                    SharedPreferencesHelper.add(DRINK_TIME_KEY, this.thisTimeValue / 60)
                    SharedPreferencesHelper.add(BALANCE,this.thisTimeValue)
                }
                MainPageState.MEDITATION ->{
                    SharedPreferencesHelper.add(MEDITATION_TIME_KEY, this.thisTimeValue / 60)
                    SharedPreferencesHelper.add(BALANCE,this.thisTimeValue)
                    dataAnalyseViewModel?.addMeditationData(
                        MyMeditationData(0, getCurrentYear(), getCurrentMonth(), getCurrentDay(),
                            getCurrentHour(),
                            (thisTimeValue/60).toFloat())
                    )
                }
                MainPageState.BREATH ->{
                    SharedPreferencesHelper.add(BREATH_TIME_KEY, this.thisTimeValue / 60)
                    SharedPreferencesHelper.add(BALANCE,this.thisTimeValue)
                    dataAnalyseViewModel?.addBreathData(
                        MyBreathData(0, getCurrentYear(), getCurrentMonth(), getCurrentDay(),
                            getCurrentHour(),
                            (thisTimeValue/60).toFloat())
                    )
                }
                MainPageState.CLOCK ->{
                    Log.d("here","here")
                    SharedPreferencesHelper.add(CLOCK_TIME_KEY, this.thisTimeValue / 60)
                    SharedPreferencesHelper.add(BALANCE,this.thisTimeValue)
                    dataAnalyseViewModel?.addClockData(
                        MyClockData(0, getCurrentYear(), getCurrentMonth(), getCurrentDay(),
                            getCurrentHour(),
                            (thisTimeValue/60).toFloat())
                    )
                }

                null -> {
                    Log.e("ERROR","complete in AnimatorController error")
                }
            }
            thisTimeValue = 0
            sPViewModel!!.onChange()
        }
    }
}
