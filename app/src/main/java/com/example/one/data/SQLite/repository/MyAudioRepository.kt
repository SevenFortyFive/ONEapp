package com.example.one.data.SQLite.repository

import com.example.one.data.SQLite.db.MyAudioDatabase
import com.example.one.data.SQLite.entity.MyAudioData

class MyAudioRepository(private val db: MyAudioDatabase) {
    suspend fun add(myData: MyAudioData):Long{
        return db.myAudioDataDao().add(myData)
    }

    suspend fun getAllMyData():List<MyAudioData>{
        return db.myAudioDataDao().getAll()
    }
    suspend fun modify(myData: MyAudioData){
        return db.myAudioDataDao().update(myData)
    }

    suspend fun delete(myData: MyAudioData){
        return db.myAudioDataDao().delete(myData)
    }

    suspend fun findById(id:Long):MyAudioData?
    {
        return db.myAudioDataDao().findById(id)
    }

    suspend fun findByNameAndAuthor(name:String,author:String): MyAudioData? {
        return db.myAudioDataDao().findByNameAndAuthor(name, author)
    }
}