package com.example.one.data.SQLite.repository

import androidx.lifecycle.LiveData
import com.example.one.data.SQLite.db.MyAudioDatabase
import com.example.one.data.SQLite.db.MyDiaryDatabase
import com.example.one.data.SQLite.entity.MyDiaryData

class MyDiaryRepository(private val db:MyDiaryDatabase) {
    suspend fun add(myDiaryData: MyDiaryData):Long{
        return db.myDiaryDataDao().add(myDiaryData)
    }

    suspend fun update(myDiaryData: MyDiaryData)
    {
        db.myDiaryDataDao().update(myDiaryData)
    }

    suspend fun delete(myDiaryData: MyDiaryData)
    {
        db.myDiaryDataDao().delete(myDiaryData)
    }

    fun getAllData():LiveData<List<MyDiaryData>>
    {
        return db.myDiaryDataDao().getAllImages()
    }

    suspend fun getItemById(id:Long): MyDiaryData {
        return db.myDiaryDataDao().getItemById(id)
    }
}