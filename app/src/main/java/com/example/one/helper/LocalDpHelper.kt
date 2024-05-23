package com.example.one.helper

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


object LocalDpHelper {
    private var dpHeight: Float? = null
    private var dpWidth: Float? = null

    fun init(context: Context){
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        dpHeight = (displayMetrics.heightPixels / displayMetrics.density)
        dpWidth = (displayMetrics.widthPixels / displayMetrics.density)
    }

    fun getDpWidth(): Float {
        return if(dpWidth != null) {
            dpWidth!!
        } else{
            0F
        }
    }

    fun getDpHeight(): Float {
        return if(dpHeight != null) {
            dpHeight!!
        } else{
            0F
        }
    }
}