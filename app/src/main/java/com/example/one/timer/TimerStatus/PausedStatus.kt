package com.example.one.timer.TimerStatus

import android.util.Log
import com.example.one.vm.TimerViewModel

class PausedStatus(private val viewModel: TimerViewModel) : IStatus {
    override fun startButtonDisplayString() = "开始"

    override fun clickStartButton() { viewModel.animatorController.resume()
    Log.d("start","pauseClickStartButton")}

    override fun stopButtonEnabled() = true

    override fun clickStopButton() = viewModel.animatorController.stop()

    override fun clickPauseButton() {
        viewModel.animatorController.pause()
    }

    override fun showEditText() = false

    override fun progressSweepAngle() = viewModel.animValue / viewModel.totalTime * 360

    override fun completedString() = ""

    override fun dragEnable() = false
}