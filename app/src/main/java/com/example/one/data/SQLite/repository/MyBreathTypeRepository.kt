package com.example.one.data.SQLite.repository

import androidx.lifecycle.LiveData
import com.example.one.data.SQLite.db.MyBreathTypeDatabase
import com.example.one.data.SQLite.entity.MyBreathTypeData

/**
 * @since 2024/5/17
 */
class MyBreathTypeRepository(private val db:MyBreathTypeDatabase){
    suspend fun add(myBreathTypeData: MyBreathTypeData):Long{
        return db.myBreathTypeDataDao().add(myBreathTypeData)
    }

    suspend fun delete(myBreathTypeData: MyBreathTypeData)
    {
        db.myBreathTypeDataDao().delete(myBreathTypeData)
    }

    fun getAll():LiveData<List<MyBreathTypeData>>
    {
        return db.myBreathTypeDataDao().getAll()
    }

    suspend fun getWithId(id:Long): MyBreathTypeData? {
        return db.myBreathTypeDataDao().getItemById(id)
    }
}