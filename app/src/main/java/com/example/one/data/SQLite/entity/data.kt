package com.example.one.data.SQLite.entity

import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * HotMap数据库中的数据类
 */
@Entity(tableName = "MyHotMapData")
data class MyHotMapData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var year: Int,
    var month: Int,
    var day: Int,
    var breath: Int,
    var clock: Int,
    var drink:Int,
    var meditation:Int
)

/**
 * Audio数据库中的数据类
 */
@Entity(tableName = "MyAudioData")
data class MyAudioData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name:String,
    val author:String,
    val surfaceId:Int,
    val surfaceUri:String,
    val uri:String,
    var love:Boolean
)

/**
 * 小记中保存的数据类
 */
@Entity(tableName = "MyDiaryData")
class MyDiaryData(
    @PrimaryKey(autoGenerate = true)
    var id: Long ,
    var year:Int,
    var month:Int,
    var day:Int,
    var location:String,
    var author:String,
    var title: String,
    var detail: String,
    var imageAsString: String
)

/**
 * 饮水记录
 */
@Entity(tableName = "MyDrinkData")
class MyDrinkData(
    @PrimaryKey(autoGenerate = true)
    var id:Long,
    var year:Int,
    var month:Int,
    var day: Int,
    var hour:Float,
    var value:Float
)

/**
 * 番茄钟记录
 */
@Entity(tableName = "MyClockData")
class MyClockData(
    @PrimaryKey(autoGenerate = true)
    var id:Long,
    var year:Int,
    var month: Int,
    var day: Int,
    var hour:Float,
    var value: Float
)

/**
 * @since 2024/5/16
 * 呼吸模式
 * title表示模式
 * describe为描述
 * 四位Long型数据组成，分别表示吸气 -> 停顿 -> 呼气 -> 停顿，均为毫秒表示
 */
@Entity(tableName = "MyBreathTypeData")
class MyBreathTypeData(
    @PrimaryKey(autoGenerate = true)
    var id:Long,
    var title:String,
    var use:String,
    var describe:String,
    var one:Long,
    var two:Long,
    var three:Long,
    var four:Long
)

/**
 * 深呼吸记录
 */
@Entity(tableName = "MyBreathData")
class MyBreathData(
    @PrimaryKey(autoGenerate = true)
    var id:Long,
    var year:Int,
    var month:Int,
    var day:Int,
    var hour: Float,
    var value: Float
)

/**
 * 冥想记录
 */
@Entity(tableName = "MyMeditationData")
class MyMeditationData(
    @PrimaryKey(autoGenerate = true)
    var id:Long,
    var year: Int,
    var month:Int,
    var day:Int,
    var hour:Float,
    var value: Float
)