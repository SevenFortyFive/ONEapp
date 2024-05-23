package com.example.one.vm

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.example.one.data.PlayerData.PlayerState
import com.example.one.data.PlayerData.getAudioDataList
import com.example.one.data.SQLite.db.MyAudioDatabase
import com.example.one.data.SQLite.entity.MyAudioData
import com.example.one.data.SQLite.repository.MyAudioRepository
import com.example.one.data.SharedPreferences.SharedPreferencesHelper
import com.example.one.data.StoreData.ExtAudioData
import com.example.one.data.StoreData.getExtAudioList
import com.example.one.player.ExoMusicPlayerManager
import kotlinx.coroutines.launch
import kotlin.random.Random


class PlayerViewModel(private val app: Application): AndroidViewModel(app) {
    private var player: ExoPlayer = ExoMusicPlayerManager.getExoPlayer()!!

    // 保存列表
    private var _dataList:MutableLiveData<MutableList<MyAudioData>> = MutableLiveData()

    val dataList: LiveData<MutableList<MyAudioData>>
        get() = _dataList

    private var _currentAudioData: MutableLiveData<MyAudioData> = MutableLiveData()
    val currentAudioData: LiveData<MyAudioData>
        get() = _currentAudioData

    private var _currentState: MutableLiveData<PlayerState> = MutableLiveData(PlayerState.STOP)

    val currentState: LiveData<PlayerState>
        get() = _currentState

    // 实例化Database对象
    private val db: MyAudioDatabase by lazy {
        Room.databaseBuilder(
            app, MyAudioDatabase::class.java,
            "MyAudioData.db"
        ).build()
    }
    // 初始化Repository
    private var myAudioRepository:MyAudioRepository = MyAudioRepository(db)

    /**
     * viewModel初始化模块
     * 1. 比对数据库中的数据是否正确
     * 2. 从数据库中加载数据到viewModel和player
     * @author yooo_fan
     */
    init {
        _currentState.postValue(PlayerState.LOADING)

        // 从数据库调出数据
        viewModelScope.launch {
            // 检查数据库
            getAudioDataList().forEach {
                // 去数据库中查询
                val temData = myAudioRepository.findById(it.id)
                // 没有查到存入数据库
                if (temData == null) {
                    myAudioRepository.add(it)
                }
            }

            // 处理额外的列表
            getExtAudioList().forEach {
                if (SharedPreferencesHelper.getIfAudioOk(it.key)) {
                    Log.d("exAudioData",it.name+" unlocked")
                    val temData = myAudioRepository.findById(it.id)
                    // 检查是否已经存入
                    if (temData == null) {
                        val newAudioData = MyAudioData(
                            it.id, it.name, it.author, it.surface,
                            null.toString(), Uri.parse(it.uri).toString(), false
                        )
                        myAudioRepository.add(newAudioData)
                    }
                }else {
                    Log.d("exAudioData", it.name + " locked")
                }
            }

            val temList : MutableList<MyAudioData> = mutableListOf()

            myAudioRepository.getAllMyData().forEach {
                // 添加到列表中
                temList.add(it)
                // 添加到player播放列表
                val mediaItem = MediaItem.fromUri(it.uri)
                player.addMediaItem(mediaItem)
            }

            _dataList.value = temList

            _dataList.value!!.forEach {
                Log.d("AudioData",it.toString())
            }

            // 设置循环模式
            player.repeatMode = ExoPlayer.REPEAT_MODE_ONE;

            _currentAudioData.value = _dataList.value?.get(player.currentMediaItemIndex)
            _currentState.postValue(PlayerState.STOP)
        }
    }



    /**
     * 在viewModel初始化时调用
     * 用于检查初始化的Audio列表和供购买的Audio列表是否已经存在于数据库中
     * @author yooo_fan
     */
    private suspend fun checkDataInDatabase() {
        viewModelScope.launch {
            getAudioDataList().forEach {
                // 去数据库中查询
                val temData = myAudioRepository.findById(it.id)
                // 没有查到存入数据库
                if (temData == null) {
                    myAudioRepository.add(it)
                }
            }

            // 处理额外的列表
            getExtAudioList().forEach {
                if (SharedPreferencesHelper.getIfAudioOk(it.key)) {
                    Log.d("exAudioData",it.name+" unlocked")
                    val temData = myAudioRepository.findById(it.id)
                    // 检查是否已经存入
                    if (temData == null) {
                        val newAudioData = MyAudioData(
                            it.id, it.name, "无名", it.surface,
                            null.toString(), Uri.parse(it.uri).toString(), false
                        )
                        myAudioRepository.add(newAudioData)
                    } else {
                        Log.d("exAudioData", it.name + " locked")
                    }
                }
            }
        }
    }

    fun start() {
        viewModelScope.launch {
            Log.d("player", player.currentMediaItemIndex.toString())
            _currentState.postValue(PlayerState.PLAYING)
            player.prepare()
            player.play()
        }
    }

    /**
     * 暂停音乐
     * @author yooo_fan
     */
    fun pause() {
        _currentState.postValue(PlayerState.STOP)
        player.pause()
    }

    /**
     * 下一首
     * @author yooo_fan
     */
    fun nextAudio() {
        this.player.seekToDefaultPosition(getNextIdx(player.currentMediaItemIndex))
        this.onDataChanged()
        this.start()
    }

    /**
     * 上一首
     * @author yooo_fan
     */
    fun preAudio() {
        this.player.seekToDefaultPosition(getPreIdx(player.currentMediaItemIndex))
        this.onDataChanged()
           this.start()
    }

    /**
     * 在切换歌曲时调用，用于同步dataLIst与player的list
     * @param存在变化的MyAudioData
     * @author yooo_fan
     */
    private fun onDataChanged(data:MyAudioData? = null){
        if(data == null)
        {
            _currentAudioData.postValue(_dataList.value!![player.currentMediaItemIndex])
        }else{
            _dataList.value?.set(player.currentMediaItemIndex,data)
            _currentAudioData.postValue(_dataList.value!![player.currentMediaItemIndex])
        }
    }

    /**
     * 从当前索引得到前一个索引
     */
    private fun getPreIdx(currentIdx: Int): Int {
        return if (currentIdx == 0)
            this._dataList.value?.size!! - 1
        else {
            player.currentMediaItemIndex - 1
        }
    }

    /**
     * 从当前索引得到下一个索引
     */
    private fun getNextIdx(currentIdx: Int): Int {
        return if (currentIdx == _dataList.value?.size!! - 1) {
            0
        } else {
            player.currentMediaItemIndex + 1
        }
    }

    /**
     * 外部调用，添加可解锁的音乐曲目
     * @author yooo_fan
     */
    fun unlockExAudioItem(audioData: ExtAudioData){
        viewModelScope.launch {
            val temData = MyAudioData(audioData.id,audioData.name,"无名",audioData.surface,
                null.toString(),Uri.parse(audioData.uri).toString(),false
            )

            if(myAudioRepository.findById(audioData.id) == null)
            {
                myAudioRepository.add(temData)
            }
            _dataList.value?.add(temData)
        }
    }

    /**
     * 更改歌曲的喜爱状态
     * 1. 更新数据库
     * 2. 调用onDataChange更新viewModel中的数据
     * @author yooo_fan
     */
    fun changeItemLoveState(data:MyAudioData){
        viewModelScope.launch {
            val temData = myAudioRepository.findByNameAndAuthor(data.name,data.author)
            if(temData != null)
            {
                Log.d("data","find data:"+temData)
                temData.love = !temData.love
                myAudioRepository.modify(temData)
                // 更新当前audio
                onDataChanged(temData)
            }
        }
    }

    /**
     * 通过MyAudioData切换曲目
     * @author yooo_fan
     */
    fun setAudioItemWithAudioData(data:MyAudioData)
    {
        val idx = _dataList.value?.indexOf(data)
        if(idx != -1)
        {
            player.seekToDefaultPosition(idx!!)
            onDataChanged()
        }
    }

    fun setAudioItemWithIndex(index:Int){
        viewModelScope.launch {
            if(index in 0..<dataList.value!!.size){
                player.seekToDefaultPosition(index)
                onDataChanged()
            }
        }
    }
}