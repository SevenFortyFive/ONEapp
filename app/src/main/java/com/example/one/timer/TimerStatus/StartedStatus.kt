package com.example.one.timer.TimerStatus

import android.util.Log
import com.example.one.vm.TimerViewModel

class StartedStatus(private val viewModel: TimerViewModel) : IStatus {
    override fun startButtonDisplayString() = "暂停"

    override fun clickStartButton() {
//        viewModel.animatorController.pause()
    Log.d("start","startedClickStartButton")}

    override fun stopButtonEnabled() = true

    override fun clickPauseButton() {
        viewModel.animatorController.pause()
    }

    override fun clickStopButton() = viewModel.animatorController.stop()

    override fun showEditText() = false

    override fun progressSweepAngle() = viewModel.animValue / viewModel.totalTime * 360

    override fun completedString() = ""

    override fun dragEnable() = false
}
