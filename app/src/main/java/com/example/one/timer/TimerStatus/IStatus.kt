package com.example.one.timer.TimerStatus

interface IStatus {
    /**
     * The content string displayed in Start Button.
     * include: Start, Pause, Resume.
     */
    fun startButtonDisplayString(): String

    /**
     * The behaviour when click Start Button.
     */
    fun clickStartButton()

    fun clickPauseButton()
    /**
     * Stop Button enable status
     */
    fun stopButtonEnabled(): Boolean

    /**
     * The behaviour when click Stop Button.
     */
    fun clickStopButton()

    /**
     * Show or hide EditText
     */
    fun showEditText(): Boolean

    /**
     * Sweep angle of progress circle
     */
    fun progressSweepAngle(): Float

    /**
     * Completed string
     */
    fun completedString(): String

    fun dragEnable():Boolean
}
