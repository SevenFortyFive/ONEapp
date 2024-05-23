package com.example.one.helper

import android.annotation.SuppressLint

object TimeFormatUtils {

    @SuppressLint("DefaultLocale")
    fun formatTime(time: Long): String {
        var value = time
        val seconds = value % 60
        value /= 60
        val minutes = value % 60
        value /= 60
        val hours = value % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
