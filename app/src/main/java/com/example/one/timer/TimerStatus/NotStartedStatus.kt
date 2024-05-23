package com.example.one.timer.TimerStatus

import android.util.Log
import com.example.one.vm.TimerViewModel

class NotStartedStatus(private val viewModel: TimerViewModel) : IStatus {

    override fun startButtonDisplayString() = "开始"

    override fun clickStartButton()  {viewModel.animatorController.startClock()
        Log.d("start","unStartedClickStartButton")
    }

    override fun stopButtonEnabled() = false

    override fun clickStopButton() {}

    override fun clickPauseButton() {}

    override fun showEditText() = true

    override fun progressSweepAngle() = if (viewModel.totalTime > 0) 360f else 0f

    override fun completedString() = ""

    override fun dragEnable() = true
}