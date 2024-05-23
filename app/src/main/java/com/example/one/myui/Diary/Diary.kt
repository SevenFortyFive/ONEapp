package com.example.one.myui.Diary

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.one.data.DiarySurface.DiarySurfaceData
import com.example.one.data.SQLite.entity.MyDiaryData
import com.example.one.helper.ImageHelper
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentYear
import com.example.one.helper.getDataString
import com.example.one.helper.getEnCaraWithLe
import com.example.one.setting.Setting
import com.example.one.vm.DiaryViewModel
import kotlinx.coroutines.launch

@Composable
fun DiaryUi(
    diaryViewModel: DiaryViewModel,
    showBottomSheet: MutableState<Boolean>,
    showEditBottomSheet: MutableState<Boolean>
) {

    val myselfData = diaryViewModel.dataList.observeAsState()
    val webMessage = diaryViewModel.webMessage.observeAsState()
    // 地点
    val location = remember {
        mutableStateOf("平流层")
    }
    // 日期,默认当前时间
    val data = remember {
        mutableStateListOf<Int>(
            getCurrentYear(),
            getCurrentMonth(),
            getCurrentDay()
        )
    }

    Column(modifier = Modifier.fillMaxSize(1f)) {
        DiaryHeader(location,
            data,
            Modifier.weight(1f))
        DiaryFace(
            diaryViewModel,
            location,
            data,
            webMessage,
            myselfData,
            diaryViewModel::deleteData,
            diaryViewModel::selectHotImage,
            showBottomSheet,
            showEditBottomSheet,
            Modifier.weight(10f))
    }
}

@Composable
fun DiaryHeader(
    location: MutableState<String>,
    data: SnapshotStateList<Int>,
    modifier: Modifier
){
    Row(modifier = modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.Top) {
            Text(text = data[2].toString(), fontSize = 50.sp)
            Text(text = "${getEnCaraWithLe(data[1])} ${data[0]}")
        }
        Text(text = location.value)
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@SuppressLint("ResourceType")
@Composable
fun DiaryFace(
    diaryViewModel: DiaryViewModel,
    location: MutableState<String>,
    data: SnapshotStateList<Int>,
    webMessage: State<ArrayList<DiarySurfaceData>?>,
    myselfData: State<List<MyDiaryData>?>,
    delete: (MyDiaryData) -> Unit,
    selectHotImage: (String,String) -> Unit,
    showBottomSheet: MutableState<Boolean>,
    showEditBottomSheet: MutableState<Boolean>,
    modifier: Modifier
){
    if(webMessage.value == null || myselfData.value == null)
    {
        return
    }
    val webHorizontalPagerState = rememberPagerState(pageCount = {
        webMessage.value?.size!!
    })
    val verticalPagerState = rememberPagerState (pageCount = { 2 })
    val myselfHorizontalPagerState = rememberPagerState(pageCount = {
        myselfData.value?.size!!
    })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(verticalPagerState.currentPage) {
        if(verticalPagerState.currentPage == 0)
        {
            location.value = "平流层"
            data[0] = getCurrentYear()
            data[1] = getCurrentMonth()
            data[2] = getCurrentDay()
        }
    }
    VerticalPager(state = verticalPagerState,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        ) {
        if(it == 0)
        {
            HorizontalPager(state = webHorizontalPagerState,
                modifier = Modifier
                    .fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(10.dp),
                    elevation =  CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(Setting.WholeElevation)
                ){
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.fillMaxWidth())
                        {
                            GlideImage(model = webMessage.value?.get(it)!!.imageUri,
                                contentDescription = "",
                                contentScale = ContentScale.FillWidth)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "摄影")
                        Spacer(modifier = Modifier.height(50.dp))
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp))
                        {
                            LazyColumn {
                                item {
                                    Text(text = webMessage.value?.get(it)!!.message)
                                }
                            }
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .zIndex(1f))
                            {
                                Row(modifier = Modifier.align(Alignment.BottomStart)) {
                                    IconButton(onClick = {
                                        selectHotImage(webMessage.value?.get(it)!!.imageUri,webMessage.value?.get(it)!!.message)
                                        showBottomSheet.value = true
                                    }) {
                                        Icon(imageVector = Icons.Rounded.Add, contentDescription = "select hotImage")
                                    }
                                    IconButton(onClick = {
                                        diaryViewModel.shareImage(context,webMessage.value?.get(it)!!.imageUri)
                                    }) {
                                        Icon(imageVector = Icons.Rounded.Share, contentDescription = "share image")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else{
            if(myselfData.value!!.isEmpty())
            {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(10.dp),
                    elevation =  CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(Setting.WholeElevation)){
                    Box(modifier = Modifier.fillMaxSize()){
                        Text(text = "没有任何小记,点击下方按钮添加",
                            modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
            HorizontalPager(state = myselfHorizontalPagerState,
                modifier = Modifier
                    .fillMaxWidth()) {
                location.value = myselfData.value?.get(it)!!.location
                data[0] = myselfData.value?.get(it)!!.year
                data[1] = myselfData.value?.get(it)!!.month
                data[2] = myselfData.value?.get(it)!!.day

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(10.dp),
                    elevation =  CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(Setting.WholeElevation)
                ){
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.fillMaxWidth())
                        {
                            Image(bitmap = ImageHelper.convertToBitmap(myselfData.value?.get(it)!!.imageAsString).asImageBitmap(),
                                contentDescription = myselfData.value!![it].title,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth)
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround) {
                            Text(text =
                            getDataString(myselfData.value!![it].year,myselfData.value!![it].month, myselfData.value!![it].day))
                            if(myselfData.value!![it].author == "")
                            {
                                Text(text = "无名")
                            }else{
                                Text(text = myselfData.value!![it].author)
                            }
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp))
                        {
                            LazyColumn {
                                item {
                                    Column(modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = myselfData.value!![it].detail)
                                        if(myselfData.value!![it].title == "")
                                        {
                                            Text(text = "《" + "无题" + "》")
                                        }else{
                                            Text(text = "《" + myselfData.value!![it].title + "》")
                                        }
                                    }

                                }
                            }
                            Box(modifier = Modifier
                                .fillMaxSize()
                                .zIndex(1f))
                            {
                                Row(modifier = Modifier.align(Alignment.BottomStart)) {
                                    IconButton(onClick = {
                                        scope.launch {
                                            val temData = myselfData.value!![it]

                                            if(verticalPagerState.currentPage == 0)
                                            {
                                                webHorizontalPagerState.scrollToPage(0)
                                            }else{
                                                verticalPagerState.scrollToPage(verticalPagerState.currentPage-1)
                                            }
                                            delete(temData)
                                        }
                                        // 进行删除
//                                        delete(myselfData.value!![it])
                                    }) {
                                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "delete diary")
                                    }
                                    IconButton(onClick = {
                                        // 设置为可能要编辑的信息
                                        diaryViewModel.setMayBeEditedDiary(myselfData.value?.get(it)!!)
                                        showEditBottomSheet.value = true

                                    }) {
                                        Icon(imageVector = Icons.Rounded.Create, contentDescription = "share image")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}