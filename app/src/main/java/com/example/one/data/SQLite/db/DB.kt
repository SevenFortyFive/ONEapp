package com.example.one.data.SQLite.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.one.data.SQLite.dao.MyAudioDao
import com.example.one.data.SQLite.dao.MyBreathDao
import com.example.one.data.SQLite.dao.MyBreathTypeDao
import com.example.one.data.SQLite.dao.MyClockDao
import com.example.one.data.SQLite.dao.MyDiaryDao
import com.example.one.data.SQLite.dao.MyDrinkDao
import com.example.one.data.SQLite.dao.MyHotMapDao
import com.example.one.data.SQLite.dao.MyMeditationDao
import com.example.one.data.SQLite.entity.MyAudioData
import com.example.one.data.SQLite.entity.MyBreathData
import com.example.one.data.SQLite.entity.MyBreathTypeData
import com.example.one.data.SQLite.entity.MyClockData
import com.example.one.data.SQLite.entity.MyDiaryData
import com.example.one.data.SQLite.entity.MyDrinkData
import com.example.one.data.SQLite.entity.MyHotMapData
import com.example.one.data.SQLite.entity.MyMeditationData

@Database(
    entities = [MyHotMapData::class],  //表明本数据库中有几张表
    version = 1,  //当前数据库的版本号（重要！）
    exportSchema = false //不导出Schema
)
abstract class MyHotMapDatabase: RoomDatabase() {
    //引用存取特定表的数据存取对象
    abstract fun myHotMapDataDao(): MyHotMapDao
}

@Database(
    entities = [MyAudioData::class],  //表明本数据库中有几张表
    version = 1,  //当前数据库的版本号（重要！）
    exportSchema = false //不导出Schema
)
abstract class MyAudioDatabase: RoomDatabase() {
    //引用存取特定表的数据存取对象
    abstract fun myAudioDataDao(): MyAudioDao
}

@Database(
    entities = [MyDiaryData::class],  //表明本数据库中有几张表
    version = 1,  //当前数据库的版本号（重要！）
    exportSchema = false //不导出Schema
)
abstract class MyDiaryDatabase: RoomDatabase() {
    //引用存取特定表的数据存取对象
    abstract fun myDiaryDataDao(): MyDiaryDao
}

@Database(
    entities = [MyDrinkData::class],  //表明本数据库中有几张表
    version = 1,  //当前数据库的版本号（重要！）
    exportSchema = false //不导出Schema
)
abstract class MyDrinkDatabase: RoomDatabase() {
    //引用存取特定表的数据存取对象
    abstract fun myDrinkDataDao(): MyDrinkDao
}

@Database(
    entities = [MyClockData::class],  //表明本数据库中有几张表
    version = 1,  //当前数据库的版本号（重要！）
    exportSchema = false //不导出Schema
)
abstract class MyClockDatabase: RoomDatabase() {
    //引用存取特定表的数据存取对象
    abstract fun myClockDataDao(): MyClockDao
}

@Database(
    entities = [MyBreathTypeData::class],  //表明本数据库中有几张表
    version = 1,  //当前数据库的版本号（重要！）
    exportSchema = false //不导出Schema
)
abstract class MyBreathTypeDatabase: RoomDatabase() {
    //引用存取特定表的数据存取对象
    abstract fun myBreathTypeDataDao(): MyBreathTypeDao
}

@Database(
    entities = [MyBreathData::class],  //表明本数据库中有几张表
    version = 1,  //当前数据库的版本号（重要！）
    exportSchema = false //不导出Schema
)
abstract class MyBreathDatabase: RoomDatabase() {
    //引用存取特定表的数据存取对象
    abstract fun myBreathDataDao(): MyBreathDao
}

@Database(
    entities = [MyMeditationData::class],  //表明本数据库中有几张表
    version = 1,  //当前数据库的版本号（重要！）
    exportSchema = false //不导出Schema
)
abstract class MyMeditationDatabase: RoomDatabase() {
    //引用存取特定表的数据存取对象
    abstract fun myMeditationDataDao(): MyMeditationDao
}