package com.example.one.data.SQLite.repository

import androidx.lifecycle.LiveData
import com.example.one.data.SQLite.db.MyClockDatabase
import com.example.one.data.SQLite.entity.MyClockData

class MyClockRepository(private val db: MyClockDatabase) {
    suspend fun add(myClockData: MyClockData):Long{
        return db.myClockDataDao().add(myClockData)
    }

    suspend fun delete(myClockData: MyClockData)
    {
        db.myClockDataDao().delete(myClockData)
    }

    fun getItemByData(year:Int, month:Int, day:Int): LiveData<List<MyClockData>> {
        return db.myClockDataDao().getItemByData(year,month, day)
    }

    suspend fun getAll():List<MyClockData>
    {
        return db.myClockDataDao().getAll()
    }

    suspend fun getDataWithDate(year: Int,month: Int,day: Int):List<MyClockData>
    {
        return db.myClockDataDao().getDataWithDate(year, month, day)
    }
}