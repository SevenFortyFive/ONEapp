package com.example.one.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//用于创建ViewModel的工厂方法
class MyHotMapViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HotMapViewModel::class.java)){
            return HotMapViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

//用于创建ViewModel的工厂方法
class MyAudioViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlayerViewModel::class.java)){
            return PlayerViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

//用于创建ViewModel的工厂方法
class MyDiaryViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DiaryViewModel::class.java)){
            return DiaryViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

//用于创建ViewModel的工厂方法
class MyBreathViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BreathViewModel::class.java)){
            return BreathViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

//用于创建ViewModel的工厂方法
class MyDataAnalyseViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DataAnalyseViewModel::class.java)){
            return DataAnalyseViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}