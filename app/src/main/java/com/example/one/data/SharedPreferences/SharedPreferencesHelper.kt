package com.example.one.data.SharedPreferences

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SharedPreferencesHelper {
    private val sp = MySharedPreference

    fun add(key:String,value: Int){
        CoroutineScope(Dispatchers.IO).launch{
            val tem = sp.getInt(key)
            sp.editIntData(key,tem+value)
        }
    }

    fun sub(key:String,value:Int)
    {
        CoroutineScope(Dispatchers.IO).launch{
            val tem = sp.getInt(key)
            sp.editIntData(key,tem-value)
        }
    }

    fun getIfAudioOk(key: String): Boolean {
        return sp.getBool(key)
    }
}