package com.example.one.data.SQLite.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.one.data.SQLite.entity.MyAudioData
import com.example.one.data.SQLite.entity.MyBreathData
import com.example.one.data.SQLite.entity.MyBreathTypeData
import com.example.one.data.SQLite.entity.MyClockData
import com.example.one.data.SQLite.entity.MyDiaryData
import com.example.one.data.SQLite.entity.MyDrinkData
import com.example.one.data.SQLite.entity.MyHotMapData
import com.example.one.data.SQLite.entity.MyMeditationData

@Dao
interface MyHotMapDao{
    @Insert
    suspend fun add(myHotMapData: MyHotMapData):Long

    @Delete
    suspend fun delete(myHotMapData: MyHotMapData)

    @Update
    suspend fun update(myHotMapData: MyHotMapData)

    @Query("select * from MyHotMapData ORDER BY year ASC,month ASC,day ASC")
    suspend fun getAll(): List<MyHotMapData>

    @Query("select * from MyHotMapData where year=:year and month =:month and day=:day")
    suspend fun findByDataWithDay(year: Int, month: Int,day:Int): MyHotMapData?

    @Query("select * from MyHotMapData where id =:id")
    suspend fun findById(id:Long): MyHotMapData?

    @Query("select * from MyHotMapData where year=:year and month =:month ORDER BY day ASC")
    fun getLiveDataWithYearAndMonth(year: Int,month: Int):LiveData<List<MyHotMapData>>
}

@Dao
interface MyAudioDao{
    @Insert
    suspend fun add(myAudioData: MyAudioData):Long

    @Delete
    suspend fun delete(myAudioData: MyAudioData)

    @Update
    suspend fun update(myAudioData: MyAudioData)

    @Query("select * from myaudiodata")
    suspend fun getAll():List<MyAudioData>

    @Query("select * from myaudiodata where id = :id")
    suspend fun findById(id:Long):MyAudioData?

    @Query("select * from myaudiodata where name = :name and author =:author")
    suspend fun findByNameAndAuthor(name:String,author:String):MyAudioData?
}

@Dao
interface MyDiaryDao{
    @Insert
    suspend fun add(myDiaryData: MyDiaryData):Long

    @Update
    suspend fun update(myDiaryData: MyDiaryData)

    @Delete
    suspend fun delete(myDiaryData: MyDiaryData)

    /**
     * 按照日期顺序提取数据
     */
    @Query("SELECT * FROM MyDiaryData ORDER BY year ASC,month ASC,day ASC")
    fun getAllImages(): LiveData<List<MyDiaryData>>

    @Query("SELECT * FROM MyDiaryData WHERE id=:id")
    suspend fun getItemById(id:Long):MyDiaryData
}

@Dao
interface MyDrinkDao{
    @Insert
    suspend fun add(myDrinkData: MyDrinkData):Long

    /**
     * 通过日期获得信息
     */
    @Query("SELECT * FROM MyDrinkData WHERE year=:year AND month =:month AND day =:day")
    fun getItemByData(year: Int,month: Int,day: Int):LiveData<List<MyDrinkData>>

    @Query("SELECT * FROM MyDrinkData WHERE id =:id")
    suspend fun getItemById(id:Long):MyDrinkData

    @Delete
    suspend fun delete(myDrinkData: MyDrinkData)

    @Query("SELECT * FROM MyDrinkData ORDER BY year ASC,month ASC,day ASC,hour ASC")
    suspend fun getAll():List<MyDrinkData>

    @Query("SELECT * FROM MyDrinkData WHERE year=:year AND month=:month AND day=:day")
    suspend fun getDataWithDate(year: Int,month: Int,day: Int):List<MyDrinkData>
}

@Dao
interface MyClockDao{
    @Insert
    suspend fun add(myClockData: MyClockData):Long

    /**
     * 通过日期获得信息
     */
    @Query("SELECT * FROM MyClockData WHERE year=:year AND month =:month AND day =:day")
    fun getItemByData(year: Int,month: Int,day: Int):LiveData<List<MyClockData>>

    @Query("SELECT * FROM MyClockData WHERE id =:id")
    suspend fun getItemById(id:Long):MyClockData

    @Delete
    suspend fun delete(myClockData: MyClockData)

    @Query("SELECT * FROM MyClockData ORDER BY year ASC,month ASC,day ASC,hour ASC")
    suspend fun getAll():List<MyClockData>

    @Query("SELECT * FROM MyClockData WHERE year=:year AND month=:month AND day=:day")
    suspend fun getDataWithDate(year: Int,month: Int,day: Int):List<MyClockData>
}

@Dao
interface MyBreathTypeDao{
    @Insert
    suspend fun add(myBreathTypeData: MyBreathTypeData):Long

    @Query("SELECT * FROM MyBreathTypeData WHERE id =:id")
    suspend fun getItemById(id:Long):MyBreathTypeData?

    @Query("SELECT * FROM MyBreathTypeData ORDER BY id ASC")
    fun getAll():LiveData<List<MyBreathTypeData>>

    @Delete
    suspend fun delete(myBreathTypeData: MyBreathTypeData)
}

@Dao
interface MyBreathDao{
    @Insert
    suspend fun add(myBreathData: MyBreathData):Long

    /**
     * 通过日期获得信息
     */
    @Query("SELECT * FROM MyBreathData WHERE year=:year AND month =:month AND day =:day")
    fun getItemByData(year: Int,month: Int,day: Int):LiveData<List<MyBreathData>>

    @Query("SELECT * FROM MyBreathData WHERE id =:id")
    suspend fun getItemById(id:Long):MyClockData

    @Delete
    suspend fun delete(myBreathData: MyBreathData)

    @Query("SELECT * FROM MyBreathData ORDER BY year ASC,month ASC,day ASC,hour ASC")
    suspend fun getAll():List<MyBreathData>

    @Query("SELECT * FROM MyBreathData WHERE year=:year AND month=:month AND day=:day")
    suspend fun getDataWithDate(year: Int,month: Int,day: Int):List<MyBreathData>
}

@Dao
interface MyMeditationDao{
    @Insert
    suspend fun add(myMeditationData: MyMeditationData):Long

    /**
     * 通过日期获得信息
     */
    @Query("SELECT * FROM MyMeditationData WHERE year=:year AND month =:month AND day =:day")
    fun getItemByData(year: Int,month: Int,day: Int):LiveData<List<MyMeditationData>>

    @Query("SELECT * FROM MyMeditationData WHERE id =:id")
    suspend fun getItemById(id:Long):MyMeditationData

    @Delete
    suspend fun delete(myMeditationData: MyMeditationData)

    @Query("SELECT * FROM MyMeditationData ORDER BY year ASC,month ASC,day ASC,hour ASC")
    suspend fun getAll():List<MyMeditationData>

    @Query("SELECT * FROM MyMeditationData WHERE year=:year AND month=:month AND day=:day")
    suspend fun getDataWithDate(year: Int,month: Int,day: Int):List<MyMeditationData>
}
