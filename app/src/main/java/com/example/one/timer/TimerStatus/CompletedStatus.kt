package com.example.one.timer.TimerStatus

import android.util.Log
import com.example.one.vm.TimerViewModel

class CompletedStatus(private val viewModel: TimerViewModel) : IStatus {
    override fun startButtonDisplayString() = "开始"

    override fun clickStartButton(){viewModel.animatorController.startClock()
    Log.d("start","completeClickStartButton")}

    override fun stopButtonEnabled() = false

    override fun clickStopButton() {}

    override fun clickPauseButton() {}

    override fun showEditText() = true

    override fun progressSweepAngle() = 0f

    override fun completedString() = "Completed!"

    override fun dragEnable() = true
}