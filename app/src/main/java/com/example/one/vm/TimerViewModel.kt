package com.example.one.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.one.timer.TimerAnimatorController
import com.example.one.timer.TimerStatus.CompletedStatus
import com.example.one.timer.TimerStatus.IStatus
import com.example.one.timer.TimerStatus.NotStartedStatus

const val MAX_INPUT_LENGTH = 5

class TimerViewModel : ViewModel() {


    var totalTime: Long by mutableLongStateOf(0)

    // 记时过程中剩余时间
    var timeLeft: Long by mutableStateOf(0)

    var animatorController = TimerAnimatorController

    var status: IStatus by mutableStateOf(NotStartedStatus(this))


    var animValue: Float by mutableStateOf(0.0f)

    fun updateValue(text: String) {

        if (text.length > MAX_INPUT_LENGTH) return

        var value = text.replace("\\D".toRegex(), "")

        if (value.startsWith("0")) value = value.substring(1)

        if (value.isBlank()) value = "0"
        totalTime = value.toLong()
        timeLeft = value.toLong()

        if (status is CompletedStatus) status = NotStartedStatus(this)
    }
}
