package com.example.one.vm

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.one.data.SQLite.db.MyBreathDatabase
import com.example.one.data.SQLite.db.MyClockDatabase
import com.example.one.data.SQLite.db.MyDrinkDatabase
import com.example.one.data.SQLite.db.MyHotMapDatabase
import com.example.one.data.SQLite.db.MyMeditationDatabase
import com.example.one.data.SQLite.entity.MyBreathData
import com.example.one.data.SQLite.entity.MyClockData
import com.example.one.data.SQLite.entity.MyDrinkData
import com.example.one.data.SQLite.entity.MyMeditationData
import com.example.one.data.SQLite.repository.MyBreathRepository
import com.example.one.data.SQLite.repository.MyClockRepository
import com.example.one.data.SQLite.repository.MyDrinkRepository
import com.example.one.data.SQLite.repository.MyHotMapDataRepository
import com.example.one.data.SQLite.repository.MyMeditationRepository
import com.example.one.data.SharedPreferences.BREATH_ONE_DAY
import com.example.one.data.SharedPreferences.CLOCK_ONE_DAY
import com.example.one.data.SharedPreferences.DRINK_ONE_DAY
import com.example.one.data.SharedPreferences.MEDITATION_ONE_DAY
import com.example.one.data.SharedPreferences.MySharedPreference
import com.example.one.data.SharedPreferences.NOTICE_TITLE
import com.example.one.helper.MessageHelper
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentYear
import kotlinx.coroutines.launch

/**
 * @since 2024/5/18
 * 数据分析viewModel
 */
class DataAnalyseViewModel(private val app: Application): AndroidViewModel(app) {
    var ifDataSet = false

    private val breathDb: MyBreathDatabase by lazy {
        Room.databaseBuilder(
            app,MyBreathDatabase::class.java,
            "MyBreathData.db"
        ).build()
    }

    private val clockDb: MyClockDatabase by lazy {
        Room.databaseBuilder(
            app,MyClockDatabase::class.java,
            "MyClockData.db"
        ).build()
    }
    private val meditationDb: MyMeditationDatabase by lazy {
        Room.databaseBuilder(
            app,MyMeditationDatabase::class.java,
            "MyMeditationData.db"
        ).build()
    }
    private val drinkDb: MyDrinkDatabase by lazy {
        Room.databaseBuilder(
            app,MyDrinkDatabase::class.java,
            "MyDrinkData.db"
        ).build()
    }

    //初始化Repository
    private var myDrinkDataRepository: MyDrinkRepository = MyDrinkRepository(drinkDb)
    private var myBreathDataRepository: MyBreathRepository = MyBreathRepository(breathDb)
    private var myClockRepository:MyClockRepository = MyClockRepository(clockDb)
    private var myMeditationRepository:MyMeditationRepository = MyMeditationRepository(meditationDb)
    // 当前查询时间
    private val _year:MutableLiveData<Int> = MutableLiveData(getCurrentYear())
    private val _month:MutableLiveData<Int> = MutableLiveData(getCurrentMonth())
    private val _day:MutableLiveData<Int> = MutableLiveData(getCurrentDay())
    val year:LiveData<Int>
        get() = _year
    val month:LiveData<Int>
        get() = _month
    val day:LiveData<Int>
        get() = _day


    var breathData = myBreathDataRepository.getItemByData(
        getCurrentYear(),
        getCurrentMonth(),
        getCurrentDay()
    )

    var clockData = myClockRepository.getItemByData(
        getCurrentYear(),
        getCurrentMonth(),
        getCurrentDay()
    )

    var meditationData = myMeditationRepository.getItemByData(
        getCurrentYear(),
        getCurrentMonth(),
        getCurrentDay()
    )

    var drinkData = myDrinkDataRepository.getItemByData(
        getCurrentYear(),
        getCurrentMonth(),
        getCurrentDay()
    )

    // 每日任务
    private val _breathOneDay:MutableLiveData<Int> = MutableLiveData(MySharedPreference.getInt(
        BREATH_ONE_DAY))
    val breathOneDay:LiveData<Int>
        get() = _breathOneDay
    private val _meditationOneDay:MutableLiveData<Int> = MutableLiveData(MySharedPreference.getInt(
        MEDITATION_ONE_DAY))
    val meditationOneDay:LiveData<Int>
        get() = _meditationOneDay
    private val _clockOneDay:MutableLiveData<Int> = MutableLiveData(MySharedPreference.getInt(
        CLOCK_ONE_DAY))
    val clockOneDay:LiveData<Int>
        get() = _clockOneDay
    private val _drinkOneDay:MutableLiveData<Int> = MutableLiveData(MySharedPreference.getInt(
        DRINK_ONE_DAY))
    val drinkOneDay:LiveData<Int>
        get() = _drinkOneDay

    init{
        Log.d("ViewModel Init","Data Analyse ViewModel Init")
        // 检查是否存在每日任务设置，若没有则设置为默认值
        viewModelScope.launch {
            listOf(MEDITATION_ONE_DAY, DRINK_ONE_DAY, BREATH_ONE_DAY, CLOCK_ONE_DAY).forEach{
                if(MySharedPreference.getInt(it) == 0)
                {
                    if(it == DRINK_ONE_DAY)
                    {
                        MySharedPreference.editIntData(it,1)
                    }else{
                        MySharedPreference.editIntData(it,8)
                    }
                }
            }
            refreshOneDay()
        }
    }

    /**
     * 设置查询时间并且更改数据
     */
    fun setTime(year:Int,month: Int,day: Int){
        viewModelScope.launch {
            _year.value = year
            _month.value = month
            _day.value = day
            breathData = breathDb.myBreathDataDao().getItemByData(
                year,month,day
            )

            clockData = clockDb.myClockDataDao().getItemByData(
                year,month,day
            )

            meditationData = meditationDb.myMeditationDataDao().getItemByData(
                year, month, day
            )

            drinkData = myDrinkDataRepository.getItemByData(
                year,month,day
            )
            ifDataSet = true
        }
    }

    fun getDrinkData(){
        Log.d("data",myDrinkDataRepository.getItemByData(getCurrentYear(), getCurrentMonth(),
            getCurrentDay()).value.toString())
    }

    fun addDrinkData(myDrinkData: MyDrinkData){
        viewModelScope.launch {
            myDrinkDataRepository.add(myDrinkData)
        }
    }

    fun addClockData(myClockData: MyClockData)
    {
        viewModelScope.launch {
            myClockRepository.add(myClockData)
        }
    }

    fun addMeditationData(myMeditationData:MyMeditationData)
    {
        viewModelScope.launch {
            myMeditationRepository.add(myMeditationData)
        }
    }

    fun addBreathData(myBreathData: MyBreathData)
    {
        viewModelScope.launch {
            myBreathDataRepository.add(myBreathData)
        }
    }

    /**
     * 用于NoticeManager获取信息
     */
    suspend fun getMessage(): String {
        val builder:StringBuilder = StringBuilder()
        var sum = 0f
        myBreathDataRepository.getDataWithDate(getCurrentYear(), getCurrentMonth(),
            getCurrentDay()).forEach{ sum+=it.value }
        if(sum < _breathOneDay.value!!){
            builder.append(
//                "每日深呼吸"+("%.1f").format(sum / _breathOneDay.value!!) +
                        "呼吸距离目标"+("%.2f").format( _breathOneDay.value!! - sum)+"")
        }
        sum = 0f
        myClockRepository.getDataWithDate(getCurrentYear(), getCurrentMonth(),
            getCurrentDay()).forEach{ sum+=it.value }
        if(sum < _clockOneDay.value!!){
            builder.append(
//                "每日专注"+("%.1f").format(sum / _breathOneDay.value!!)  +
                    "专注距离目标"+("%.2f").format( _clockOneDay.value!!  - sum)+"")
        }
        sum = 0f
        myMeditationRepository.getDataWithDate(getCurrentYear(), getCurrentMonth(),
            getCurrentDay()).forEach{ sum+=it.value }
        if(sum < _meditationOneDay.value!!){
            builder.append(
//                "每日冥想"+("%.1f").format(sum / _breathOneDay.value!!)  +
                    "冥想距离目标"+("%.2f").format(_meditationOneDay.value!!  - sum)+"")
        }
        sum = 0f
        myDrinkDataRepository.getDataWithDate(getCurrentYear(), getCurrentMonth(),
            getCurrentDay()).forEach{ sum+=it.value }
        if(sum < _drinkOneDay.value!!){
            builder.append(
//                "每日饮水"+("%.1f").format(sum / _breathOneDay.value!!)  +
                    "距离目标"+("%.2f").format(_drinkOneDay.value!!  - sum)+"")
        }
        if(builder.isEmpty() || builder.isBlank())
        {
            builder.append("全部完成!")
        }
        return builder.toString()
    }

    fun getNoticeTitle(): String? {
        val title:String = MySharedPreference.getString(NOTICE_TITLE).toString()
        if(title == " ")
        {
            return "ONE"
        }
        return MySharedPreference.getString(NOTICE_TITLE)
    }

    fun setNoticeTitle(value:String){
        viewModelScope.launch {
            MySharedPreference.editStringData(NOTICE_TITLE,value)
        }
    }

    /**
     * 用于设置每日任务
     */
    fun setOneDay(key:String,value:Int){
        viewModelScope.launch {
            MySharedPreference.editIntData(key,value)
            refreshOneDay()
        }
    }

    /**
     * 刷新任务设置
     */
    private fun refreshOneDay(){
        viewModelScope.launch {
            _breathOneDay.value = MySharedPreference.getInt(BREATH_ONE_DAY)
            _clockOneDay.value = MySharedPreference.getInt(CLOCK_ONE_DAY)
            _meditationOneDay.value = MySharedPreference.getInt(MEDITATION_ONE_DAY)
            _drinkOneDay.value = MySharedPreference.getInt(DRINK_ONE_DAY)
        }
    }

    /**
     * 分享信息到其他应用
     */
    fun shareMessage(context: Context){
        viewModelScope.launch {
            val builder = StringBuilder()
            myBreathDataRepository.getAll().forEach {
                builder.append("${it.year}年${it.month}月${it.day}日${it.hour}小时 深呼吸${it.value}分钟\n")
            }
            myClockRepository.getAll().forEach {
                builder.append("${it.year}年${it.month}月${it.day}日${it.hour}小时 专注${it.value}分钟\n")
            }
            myMeditationRepository.getAll().forEach {
                builder.append("${it.year}年${it.month}月${it.day}日${it.hour}小时 冥想${it.value}分钟\n")
            }
            myDrinkDataRepository.getAll().forEach {
                builder.append("${it.year}年${it.month}月${it.day}日${it.hour}小时 饮水${it.value}L\n")
            }
            builder.append("数据来自${getCurrentYear()}年${getCurrentMonth()}月${getCurrentDay()}日之前")
            sendPareTextMessage(context,builder.toString())
        }
    }

    private fun sendPareTextMessage(context: Context, data:String){
        viewModelScope.launch {
            MessageHelper.sendPlainText(context,data)
        }
    }
}
