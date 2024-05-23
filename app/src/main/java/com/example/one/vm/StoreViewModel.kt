package com.example.one.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one.data.SharedPreferences.BALANCE
import com.example.one.data.SharedPreferences.MySharedPreference
import kotlinx.coroutines.launch

class StoreViewModel: ViewModel() {
    private val _balance:MutableLiveData<Int> = MutableLiveData()

    val balance :LiveData<Int>
        get() = _balance

    init {
        viewModelScope.launch {
            _balance.value = MySharedPreference.getInt(BALANCE)
        }
    }

    fun onBalanceChange(){
        viewModelScope.launch {
            _balance.value = MySharedPreference.getInt(BALANCE)
        }
    }
}