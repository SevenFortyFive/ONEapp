package com.example.one.player

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer


/**
 * 全局音频播放器
 * 用于管理背景音乐
 * 全局单例
 */
object ExoMusicPlayerManager {
    private var exoPlayer: ExoPlayer? = null

    fun initializeExoPlayer(context: Context) {
        // 初始化ExoPlayer实例
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context)
                .setAudioAttributes(
                    AudioAttributes.DEFAULT,/* handleAudioFocus= */true)
                .setHandleAudioBecomingNoisy(true)
                .setWakeMode(C.WAKE_MODE_LOCAL)
                .build()
        }
    }

    fun getExoPlayer(): ExoPlayer? {
        return exoPlayer
    }

    fun releasePlayer() {
        // 释放ExoPlayer实例
        exoPlayer?.release()
        exoPlayer = null
    }
}