package com.example.one.data.StoreData

import com.example.one.R

data class ExtAudioData(
    val id:Long,
    val uri:String,
    val name: String,
    val author:String,
    val surface: Int,
    val key:String,
    val cost:Int
)
/**
 * 返回可消费的audio列表
 */
fun getExtAudioList():List<ExtAudioData>{
    return listOf(
        ExtAudioData(20,"asset:///airConditionerInCar.mp3","空调","yooo_fan", R.raw.airincar,"AUDIO_1",10000),
        ExtAudioData(21,"asset:///blackBird.mp3","黑鸟","yooo_fan",  R.raw.blackbird,"AUDIO_2",10000),
        ExtAudioData(22,"asset:///campFire.mp3","篝火","yooo_fan",  R.raw.campfire,"AUDIO_3",10000),
        ExtAudioData(23,"asset:///carRadioStatic.mp3","车载收音机","yooo_fan",  R.raw.carradio,"AUDIO_4",10000),
        ExtAudioData(24,"asset:///catPurr.mp3","猫咪","yooo_fan",  R.raw.catpurr,"AUDIO_5",10000)
    )
}