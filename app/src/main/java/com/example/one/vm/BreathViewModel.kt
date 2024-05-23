package com.example.one.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.one.data.SQLite.db.MyBreathTypeDatabase
import com.example.one.data.SQLite.entity.MyBreathTypeData
import com.example.one.data.SQLite.repository.MyBreathTypeRepository
import com.example.one.data.getBreathTypeList
import kotlinx.coroutines.launch

/**
 * @since 2024/5/17
 */
class BreathViewModel(private val app: Application): AndroidViewModel(app)  {
    private val _ifRunning:MutableLiveData<Boolean> = MutableLiveData(false)

    val ifRunning:LiveData<Boolean>
        get() = _ifRunning

    private val db: MyBreathTypeDatabase by lazy {
        Room.databaseBuilder(
            app,MyBreathTypeDatabase::class.java,
            "MyBreathTypeData.db"
        ).build()
    }

    private var myBreathTypeDataRepository:MyBreathTypeRepository = MyBreathTypeRepository(db)

    val typeList = myBreathTypeDataRepository.getAll()

    init {
        viewModelScope.launch {
            getBreathTypeList().forEach{
                val temData = myBreathTypeDataRepository.getWithId(it.id)
                if(temData == null)
                {
                     myBreathTypeDataRepository.add(it)
                }
            }
        }
    }

    fun setNotRunning(){
        _ifRunning.value = false
    }
    fun setRunning(){
        _ifRunning.value = true
    }

    fun addType(title:String,describe:String,use:String,one:Long,two:Long,three:Long,four:Long){
        viewModelScope.launch {
            myBreathTypeDataRepository.add(
                MyBreathTypeData(
                    0,
                    title,
                    use,
                    describe,
                    one,
                    two,
                    three,
                    four
                )
            )
        }
    }

    fun deleteType(myBreathTypeData: MyBreathTypeData)
    {
        viewModelScope.launch {
            myBreathTypeDataRepository.delete(
                myBreathTypeData
            )
        }
    }
}