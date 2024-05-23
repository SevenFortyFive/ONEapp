package com.example.one.player

import com.example.one.data.StoreData.ExtAudioData
import com.example.one.vm.PlayerViewModel

object AudioController {
    private var playerViewModel: PlayerViewModel? = null

    fun init(playerViewModel: PlayerViewModel) {
        this.playerViewModel = playerViewModel
    }

    fun addItem(extAudioData: ExtAudioData){
        playerViewModel?.unlockExAudioItem(extAudioData)
    }

    fun release(){
        playerViewModel = null
    }
}