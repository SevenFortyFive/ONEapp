package com.example.one.helper

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.example.one.vm.AppViewModel

fun vibrate(context: Context, duration: Long) {
    // 检查设置是否允许震动
    val appViewModel = AppViewModel.getInstance()
    if(!appViewModel.ifCanVibrate.value)
    {
        return
    }
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(duration)
        }
}