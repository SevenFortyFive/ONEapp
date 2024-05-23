package com.example.one.data.SQLite.repository

import androidx.lifecycle.LiveData
import com.example.one.data.SQLite.db.MyBreathDatabase
import com.example.one.data.SQLite.entity.MyBreathData

class MyBreathRepository(private val db: MyBreathDatabase) {
    suspend fun add(myBreathData:MyBreathData):Long{
        return db.myBreathDataDao().add(myBreathData)
    }

    suspend fun delete(myBreathData: MyBreathData)
    {
        db.myBreathDataDao().delete(myBreathData)
    }

    fun getItemByData(year:Int, month:Int, day:Int): LiveData<List<MyBreathData>> {
        return db.myBreathDataDao().getItemByData(year,month, day)
    }

    suspend fun getAll():List<MyBreathData>
    {
        return db.myBreathDataDao().getAll()
    }

    suspend fun getDataWithDate(year: Int,month: Int,day: Int):List<MyBreathData>
    {
        return db.myBreathDataDao().getDataWithDate(year, month, day)
    }
}