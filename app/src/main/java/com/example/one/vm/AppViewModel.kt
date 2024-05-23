package com.example.one.vm

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one.data.SharedPreferences.CAN_VIBRATE
import com.example.one.data.SharedPreferences.FOLLOW_SYSTEM
import com.example.one.data.SharedPreferences.HAS_NOTICE_SCHEDULE
import com.example.one.data.SharedPreferences.MySharedPreference
import com.example.one.data.SharedPreferences.NOW_DARK
import kotlinx.coroutines.launch

/**
 * 全局viewModel
 * 标记App繁忙状态
 * 例如正在记时等
 * 可能用于：阻止导航事件等
 */
class AppViewModel : ViewModel() {
    // 标记当前是否存在任务
    private val _ifHasTask = mutableStateOf(false)
    val ifHasTask:State<Boolean>
        get() = _ifHasTask

    // 因为需要实时进行检测，定义为liveData
    private val _ifUpsetDown:MutableLiveData<Boolean> = MutableLiveData(false )
    val idUpsetDown:LiveData<Boolean>
        get() = _ifUpsetDown

    private val _ifDark = mutableStateOf(false)
    val ifDark:State<Boolean>
        get() = _ifDark

    private val _ifCanVibrate = mutableStateOf(false)
    val ifCanVibrate:State<Boolean>
        get() = _ifCanVibrate

    private val _ifHasNoticeSchedule = mutableStateOf(false)
    val ifHasNoticeSchedule:State<Boolean>
        get() = _ifHasNoticeSchedule


    private val _ifFollowSystem = mutableStateOf(false)
    val ifFollowSystem:State<Boolean>
        get() = _ifFollowSystem

    /**
     *  设置为繁忙状态
     */
    fun setAppBusy(){
        viewModelScope.launch {
            Log.d("free","setAppBusy")
            _ifHasTask.value = true
        }
    }

    /**
     * 设置为空闲状态
     */
    fun setAppFree(){
        viewModelScope.launch {
            Log.d("free","setAppFree")
            _ifHasTask.value = false
        }
    }

    fun setAppUpSetDown(){
        viewModelScope.launch {
            _ifUpsetDown.value = true
        }
    }

    fun setAppNotUpsetDown(){
        viewModelScope.launch {
            _ifUpsetDown.value = false
        }
    }

    fun setDark(){
        viewModelScope.launch {
            _ifDark.value = true
            MySharedPreference.editBooleanData(NOW_DARK,true)
        }
    }

    fun setLight(){
        viewModelScope.launch {
            _ifDark.value = false
            MySharedPreference.editBooleanData(NOW_DARK,false)
        }
    }

    fun setCanVibrate(){
        viewModelScope.launch {
            _ifCanVibrate.value = true
            MySharedPreference.editBooleanData(CAN_VIBRATE,true)
        }
    }

    fun setCannotVibrate(){
        viewModelScope.launch {
            _ifCanVibrate.value = false
            MySharedPreference.editBooleanData(CAN_VIBRATE,false)
        }
    }

    fun setHasNoticeSchedule(){
        viewModelScope.launch {
            _ifHasNoticeSchedule.value = true
            MySharedPreference.editBooleanData(HAS_NOTICE_SCHEDULE,true)
        }
    }

    fun setNotHasNoticeSchedule(){
        viewModelScope.launch {
            _ifHasNoticeSchedule.value = false
            MySharedPreference.editBooleanData(HAS_NOTICE_SCHEDULE,false)
        }
    }


    fun setFollowSystem(){
        viewModelScope.launch {
            _ifFollowSystem.value = true
            MySharedPreference.editBooleanData(FOLLOW_SYSTEM,true)
        }
    }

    fun setNotFollowSystem(){
        viewModelScope.launch {
            _ifFollowSystem.value = false
            MySharedPreference.editBooleanData(FOLLOW_SYSTEM,false)
        }
    }

    fun initInformation(){
        viewModelScope.launch {
            _ifDark.value = MySharedPreference.getBool(NOW_DARK)
            _ifCanVibrate.value = MySharedPreference.getBool(CAN_VIBRATE)
            _ifHasNoticeSchedule.value = MySharedPreference.getBool(HAS_NOTICE_SCHEDULE)
            _ifFollowSystem.value = MySharedPreference.getBool(FOLLOW_SYSTEM)
        }
    }

    /**
     * 全局单例
     */
    companion object{
        private var instance: AppViewModel? = null

        fun getInstance():AppViewModel{
            if(instance == null){
                instance = AppViewModel()
            }
            return instance!!
        }
    }
}