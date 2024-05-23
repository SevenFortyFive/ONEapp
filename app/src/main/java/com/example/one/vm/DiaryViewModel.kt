package com.example.one.vm

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.one.data.DiarySurface.DiarySurfaceData
import com.example.one.data.SQLite.db.MyDiaryDatabase
import com.example.one.data.SQLite.entity.MyDiaryData
import com.example.one.data.SQLite.repository.MyDiaryRepository
import com.example.one.helper.ImageHelper
import com.example.one.helper.MessageHelper
import com.example.one.helper.WebHelper
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryViewModel(private val app:Application):AndroidViewModel(app) {
    private val db:MyDiaryDatabase by lazy{
        Room.databaseBuilder(
            app,MyDiaryDatabase::class.java,
            "MyDiaryData.db"
        ).build()
    }

    // 选中的image，来自MediaStore或者takePhoto
    private var _selectedImage:MutableLiveData<Bitmap?> = MutableLiveData()
    val selectedImage:LiveData<Bitmap?>
        get() = _selectedImage
    private var _selectedLocation:MutableLiveData<String> = MutableLiveData("")
    val selectedLocation:LiveData<String>
        get() = _selectedLocation
    private val _selectedAuthor:MutableLiveData<String> = MutableLiveData("")
    val selectedAuthor:LiveData<String>
        get() = _selectedAuthor
    private val _selectedTitle:MutableLiveData<String> = MutableLiveData("")
    val selectedTitle:LiveData<String>
        get() = _selectedTitle
    private val _selectedDetail:MutableLiveData<String> = MutableLiveData("")
    val selectedDetail:LiveData<String>
        get() = _selectedDetail

    // 将从网站获取的图片作为默认的图片
    private var _hotImage:MutableLiveData<DiarySurfaceData> = MutableLiveData()
    val hotImage:LiveData<DiarySurfaceData>
        get() = _hotImage

    private var myDiaryRepository: MyDiaryRepository = MyDiaryRepository(db)

    val dataList: LiveData<List<MyDiaryData>> = myDiaryRepository.getAllData()

    private val _webMessage:MutableLiveData<ArrayList<DiarySurfaceData>?> = MutableLiveData()
    val webMessage: LiveData<ArrayList<DiarySurfaceData>?>
        get() = _webMessage

    // 可能被编辑的小记
    private val _mayBeEditedImage:MutableLiveData<MyDiaryData> = MutableLiveData()
    val mayBeEditedImage:LiveData<MyDiaryData>
        get() = _mayBeEditedImage

    /**
     * 添加一个Data
     */
    fun addDiaryData(myDiaryData: MyDiaryData){
        viewModelScope.launch {
            myDiaryRepository.add(myDiaryData)
        }
    }

    /**
     * 去网站上获取图片uri和文案
     */
    fun fetchMessage(){
        CoroutineScope(Dispatchers.IO).launch {
            val message = WebHelper.fetchContent("https://wufazhuce.com/")
            CoroutineScope(Dispatchers.Main).launch {
                _webMessage.value = message
                _hotImage.value = message?.get(0)
            }
        }
    }

    /**
     * 通过bitmap来创建个data并且存储在数据库中
     * 实际上bitmap来自于selectedImage
     * selectedImage来自于解析自uri或者takePhoto返回的bitmap
     */
    fun saveImageToDataBase(bitmap:Bitmap, title:String, detail:String, author:String, location:String)
    {
        viewModelScope.launch {
            ImageHelper.convertToString(bitmap)?.let {
                myDiaryRepository.add(
                    myDiaryData = MyDiaryData(
                        0, getCurrentYear(), getCurrentMonth(), getCurrentDay(),
                        location,author,title,detail,it
                    )
                )
            }
        }
    }

    fun updateImageToDataBase(myDiaryData: MyDiaryData,bitmap: Bitmap,detail:String,title: String){
        viewModelScope.launch {
            ImageHelper.convertToString(bitmap)?.let {
                myDiaryData.imageAsString = it
                myDiaryData.detail = detail
                myDiaryData.title = title
                myDiaryRepository.update(myDiaryData)
            }
        }
    }

    /**
     * 通过拍照选择图片时，返回bitmap
     * 将其上传到选择界面
     */
    fun selectImageFromTakePhoto(bitmap:Bitmap){
        viewModelScope.launch {
            _selectedImage.value = bitmap
        }
    }

    /**
     * 通过MediaStore选择图片时，返回uri
     * 将uri解析唯bitmap上传到选择界面
     */
    fun selectImageFromMediaStore(uri:Uri,context: Context)
    {
        viewModelScope.launch {
            uri.let {
                // 将uri解析为bitmap
                val inputStream = context.contentResolver.openInputStream(it)
                _selectedImage.value = BitmapFactory.decodeStream(inputStream)
            }
        }
    }

    fun selectHotImage(uri:String,detail: String)
    {
        viewModelScope.launch {
            _selectedImage.value = ImageHelper.downloadBitmapFromUrl(uri)
            _selectedDetail.value = detail
        }
    }

    fun deleteData(myDiaryData: MyDiaryData)
    {
        viewModelScope.launch {
            myDiaryRepository.delete(myDiaryData)
        }
    }

    /**
     * @since 2024/5/17
     * 分享网络图片到本地
     */
    fun shareImage(context: Context,uri: String){
        viewModelScope.launch {
            ImageHelper.downloadBitmapFromUrl(uri)
                ?.let { MessageHelper.shareBitmap(context, it,"title") }
        }
    }

    /**
     * 设置将要编辑的小记
     */
    fun setMayBeEditedDiary(myDiaryData: MyDiaryData){
        viewModelScope.launch {
            _mayBeEditedImage.value = myDiaryData
            _selectedImage.value = ImageHelper.convertToBitmap(myDiaryData.imageAsString)
            _selectedTitle.value = myDiaryData.title
            _selectedAuthor.value = myDiaryData.author
            _selectedDetail.value = myDiaryData.detail
            _selectedLocation.value = myDiaryData.location
        }
    }

    fun updateLocation(string: String)
    {
        viewModelScope.launch {
            _selectedLocation.value = string
        }
    }

    fun updateTitle(string: String)
    {
        viewModelScope.launch {
            _selectedTitle.value = string
        }
    }

    fun updateAuthor(string: String)
    {
        viewModelScope.launch {
            _selectedAuthor.value = string
        }
    }

    fun updateDetail(string:String)
    {
        viewModelScope.launch {
            _selectedDetail.value = string
        }
    }
}