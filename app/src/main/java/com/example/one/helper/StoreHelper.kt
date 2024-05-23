package com.example.one.helper

import com.example.one.data.SharedPreferences.MySharedPreference
import com.example.one.data.StoreData.ExtAudioData
import com.example.one.player.AudioController

fun unlockAudio(extAudioData: ExtAudioData){
    MySharedPreference.editBooleanData(extAudioData.key,true)
    AudioController.addItem(extAudioData)
}

fun ifBought(extAudioData: ExtAudioData): Boolean {
    return MySharedPreference.getBool(extAudioData.key)
}