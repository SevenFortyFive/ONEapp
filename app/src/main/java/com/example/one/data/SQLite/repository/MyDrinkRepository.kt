package com.example.one.data.SQLite.repository

import androidx.lifecycle.LiveData
import com.example.one.data.SQLite.db.MyDrinkDatabase
import com.example.one.data.SQLite.entity.MyDrinkData

class MyDrinkRepository(private val db:MyDrinkDatabase) {
    suspend fun add(myDrinkData: MyDrinkData):Long{
        return db.myDrinkDataDao().add(myDrinkData)
    }

    suspend fun delete(myDrinkData: MyDrinkData)
    {
        db.myDrinkDataDao().delete(myDrinkData)
    }

    fun getItemByData(year:Int, month:Int, day:Int): LiveData<List<MyDrinkData>> {
        return db.myDrinkDataDao().getItemByData(year,month, day)
    }

    suspend fun getAll():List<MyDrinkData>
    {
        return db.myDrinkDataDao().getAll()
    }

    suspend fun getDataWithDate(year: Int,month: Int,day: Int):List<MyDrinkData>
    {
        return db.myDrinkDataDao().getDataWithDate(year, month, day)
    }
}