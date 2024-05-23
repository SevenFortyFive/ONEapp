package com.example.one.data.PlayerData

import android.net.Uri
import com.example.one.R
import com.example.one.data.SQLite.entity.MyAudioData

/**
 * 返回初始化的audio列表
 */
fun getAudioDataList(): List<MyAudioData> {
    return listOf(
        MyAudioData(1,"飞鸟","yooo_fan",R.raw.wind, null.toString(),
            Uri.parse("asset:///wind.mp3").toString(),
            false
        ),
        MyAudioData(2,"城中车流","yooo_fan",R.raw.traffic, null.toString(),
            Uri.parse("asset:///cityTraffic.mp3").toString(),
            false
        ),
        MyAudioData(3,"飞驰的噪声","yooo_fan",R.raw.noisesweep, null.toString(),
            Uri.parse("asset:///whiteNoiseSweep.mp3").toString(),
            false
        ),
        MyAudioData(4,"漫步","yooo_fan",R.raw.wandering, null.toString(),
            Uri.parse("asset:///wandering.mp3").toString(),
            false
        ),
        MyAudioData(5,"水下","yooo_fan",R.raw.underwater, null.toString(),
            Uri.parse("asset:///underwater.mp3").toString(),
            false
        ),
        MyAudioData(6,"宏伟教堂","yooo_fan",R.raw.templenoise, null.toString(),
            Uri.parse("asset:///templeNoise.mp3").toString(),
            false
        ),
        MyAudioData(7,"海边浪花","yooo_fan",R.raw.beachwave, null.toString(),
            Uri.parse("asset:///sandyBeachCalmWavesWaterNatureSounds.mp3").toString(),
            false
        ),
        MyAudioData(8,"轻雨","yooo_fan",R.raw.rainy, null.toString(),
            Uri.parse("asset:///rainy.mp3").toString(),
            false
        ),
        MyAudioData(9,"火源","yooo_fan",R.raw.fuego, null.toString(),
            Uri.parse("asset:///fuego.mp3").toString(),
            false
        ),
        MyAudioData(10,"森林","yooo_fan",R.raw.forest, null.toString(),
            Uri.parse("asset:///forest.mp3").toString(),
            false
        ),
        MyAudioData(11,"风扇","yooo_fan",R.raw.fan, null.toString(),
            Uri.parse("asset:///fanInTheRoom.mp3").toString(),
            false
        ),
        MyAudioData(12,"钟表","yooo_fan",R.raw.clock, null.toString(),
            Uri.parse("asset:///clockTicking60SecondCountdown.mp3").toString(),
            false
        ),
        MyAudioData(13,"课间交谈","yooo_fan",R.raw.classtalk, null.toString(),
            Uri.parse("asset:///classromTalk.mp3").toString(),
            false
        ),
        MyAudioData(14,"城中交通","yooo_fan",R.raw.traffic, null.toString(),
            Uri.parse("asset:///cityTraffic.mp3").toString(),
            false
        )
    )
}