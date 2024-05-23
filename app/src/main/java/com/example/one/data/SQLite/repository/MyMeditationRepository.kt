package com.example.one.data.SQLite.repository

import androidx.lifecycle.LiveData
import com.example.one.data.SQLite.db.MyMeditationDatabase
import com.example.one.data.SQLite.entity.MyMeditationData

class MyMeditationRepository(private val db: MyMeditationDatabase) {
    suspend fun add(myMeditationData: MyMeditationData):Long{
        return db.myMeditationDataDao().add(myMeditationData)
    }

    suspend fun delete(myMeditationData: MyMeditationData)
    {
        db.myMeditationDataDao().delete(myMeditationData)
    }

    fun getItemByData(year:Int, month:Int, day:Int): LiveData<List<MyMeditationData>> {
        return db.myMeditationDataDao().getItemByData(year,month, day)
    }

    suspend fun getAll():List<MyMeditationData>
    {
        return db.myMeditationDataDao().getAll()
    }

    suspend fun getDataWithDate(year: Int,month: Int,day: Int):List<MyMeditationData>
    {
        return db.myMeditationDataDao().getDataWithDate(year, month, day)
    }
}