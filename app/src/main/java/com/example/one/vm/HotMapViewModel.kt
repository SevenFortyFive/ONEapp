package com.example.one.vm

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.one.data.MainPageState
import com.example.one.data.SQLite.db.MyHotMapDatabase
import com.example.one.data.SQLite.entity.MyHotMapData
import com.example.one.data.SQLite.repository.MyHotMapDataRepository
import com.example.one.helper.MessageHelper
import com.example.one.helper.findPreviousMonth
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentYear
import com.example.one.helper.getNumOfDay
import kotlinx.coroutines.launch

class HotMapViewModel(private val app:Application):AndroidViewModel(app) {
    //实例化Database对象
    private val db: MyHotMapDatabase by lazy {
        Room.databaseBuilder(
            app, MyHotMapDatabase::class.java,
            "MyHotMapData.db"
        ).build()
    }
    // 保存主页的状态
    private var _type:MutableLiveData<MainPageState> = MutableLiveData(MainPageState.BREATH)
    val type:LiveData<MainPageState>
        get() = _type

    //初始化Repository
    private var myHotMapDataRepository: MyHotMapDataRepository = MyHotMapDataRepository(db)

    var dataListOne = myHotMapDataRepository.findLiveDataByYearAndMonth(getCurrentYear(),
        getCurrentMonth() - 3)

    var dataListTwo = myHotMapDataRepository.findLiveDataByYearAndMonth(getCurrentYear(),
        getCurrentMonth() - 2)

    var dataListThree = myHotMapDataRepository.findLiveDataByYearAndMonth(getCurrentYear(),
        getCurrentMonth() - 1)

    var dataListFour = myHotMapDataRepository.findLiveDataByYearAndMonth(getCurrentYear(),
        getCurrentMonth())

    init {
        // 检查是否有错误
        viewModelScope.launch {
            val currentYear = getCurrentYear()
            (0..3).map {monthD->
                val yearAndMonth = findPreviousMonth(getCurrentYear(), getCurrentMonth(),
                    monthD)
                val year = yearAndMonth.first
                val month = yearAndMonth.second
                val numOfDay = getNumOfDay(year, month)
                (1..numOfDay).map {day->
                    val temData = myHotMapDataRepository.findByDataWithDay(
                        year,
                        month,
                        day
                    )
                    if(temData == null)
                    {
                        myHotMapDataRepository.add(
                            MyHotMapData(
                                0,
                                year,
                                month,
                                day,
                                0,
                                0,
                                0,
                                0
                            )
                        )
                    }
                }
            }
        }
    }

    fun update(){
        viewModelScope.launch {
            val temData = myHotMapDataRepository.findByDataWithDay(
                getCurrentYear(),
                getCurrentMonth(),
                getCurrentDay()
            )
            if(temData == null)
                return@launch

            when(type.value){
                MainPageState.DRINK -> temData.drink++
                MainPageState.BREATH -> temData.breath++
                MainPageState.CLOCK -> temData.clock++
                MainPageState.MEDITATION -> temData.meditation++
                null -> {
                }
            }
            myHotMapDataRepository.modify(temData)
            Log.d("upDate",temData.toString())
        }
    }

    fun changeType(changeTo:MainPageState)
    {
        viewModelScope.launch {
            _type.value = changeTo
        }
    }

    private fun sendPareTextMessage(context: Context, data:String){
        viewModelScope.launch {
            MessageHelper.sendPlainText(context,data)
        }
    }

    fun shareDetailHotMapMessage(context: Context){
        viewModelScope.launch {
            val builder = StringBuilder()
            val formattedString = "%-6s | %-3s| %-3s| %-5s | %-5s | %-5s | %-5s\n"
            builder.append(String.format(formattedString,"年","月","日","呼吸","专注","饮水","冥想"))
            myHotMapDataRepository.getAllMyData().forEach{
                builder.append(String.format(formattedString,
                    it.year.toString(),
                    it.month.toString(),
                    it.day.toString(),
                    it.breath.toString(),
                    it.clock.toString(),
                    it.drink.toString(),
                    it.meditation.toString()
                ))
            }
            sendPareTextMessage(context,builder.toString())
        }
    }
}