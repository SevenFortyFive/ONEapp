package com.example.one.page

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.one.helper.Toaster
import com.example.one.helper.vibrate
import com.example.one.myui.Diary.DiaryUi
import com.example.one.setting.Setting
import com.example.one.vm.DiaryViewModel
import com.example.one.vm.MyDiaryViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("ResourceType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun DiaryPage()
{
    val diaryViewModel: DiaryViewModel = viewModel(
        LocalContext.current as ComponentActivity
        ,factory = MyDiaryViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val context:Context = LocalContext.current

    val showAddBottomSheet = remember { mutableStateOf(false) }
    val showEditBottomSheet = remember {
        mutableStateOf(false)
    }

    // 创建intent去拍照
    val launcherForTakePhoto: ActivityResultLauncher<Void?> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) {
            if (it != null) {
                // 如果获取成功，将其返回给viewModel处理
                diaryViewModel.selectImageFromTakePhoto(it)
            }else{
                Toaster.showShortToaster(context,"没有获得信息")
            }
        }

    // 创建intent去获取mediaStore中的内容
    val launcherForMediaStore: ActivityResultLauncher<String> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()) {
            if(it != null)
            {
                // 如果获取成功，创建携程处理，以免阻塞UI
                MainScope().launch {
                    withContext(Dispatchers.IO) {
                        // 在后台线程执行耗时操作，例如保存图片等
                        diaryViewModel.selectImageFromMediaStore(it,context)
                    }
                }
            }else{
                Toaster.showShortToaster(context,"没有获取到uri")
            }
        }

    val launcherForInternet =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()) {isGranted:Boolean->
            if(isGranted)
            {
                diaryViewModel.fetchMessage()
            }else{
                Toaster.showShortToaster(context,"没有获取到网络权限")
            }
    }
    if(ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.INTERNET
        )!= PackageManager.PERMISSION_GRANTED)
    {
        // 不存在权限申请权限
        launcherForInternet.launch(Manifest.permission.INTERNET)
    }else{
        // 存在权限，直接设置
        diaryViewModel.fetchMessage()
    }

    val selectedImage = diaryViewModel.selectedImage.observeAsState()
    // 获取可能用来被编辑的小记
    val mayEditDiary = diaryViewModel.mayBeEditedImage.observeAsState()
    // 用于显示默认封面
    val hotImage = diaryViewModel.hotImage.observeAsState()

    if(hotImage.value == null)
    {
        return
    }

    val addSheetState = rememberModalBottomSheetState()
    val addScope = rememberCoroutineScope()
    val editSheetState = rememberModalBottomSheetState()
    val editScope = rememberCoroutineScope()


    val selectedTitle = diaryViewModel.selectedTitle.observeAsState()
    val selectedDetail = diaryViewModel.selectedDetail.observeAsState()
    val selectedAuthor = diaryViewModel.selectedAuthor.observeAsState()
    val selectedLocation = diaryViewModel.selectedLocation.observeAsState()

    if(selectedLocation.value == null
        || selectedAuthor.value == null
        || selectedDetail.value == null
        || selectedTitle.value == null)
    {
        return
    }

    // 标记选取状态照片的方式
    val selectedIndex = remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {

                showAddBottomSheet.value = true
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add one Diary")
            }
        }
    ) { contentPadding ->
        // Screen content
        Box(modifier = Modifier.padding(contentPadding))
        {
            // 主体ui
            DiaryUi(diaryViewModel = diaryViewModel,showAddBottomSheet,showEditBottomSheet)
        }

        // 供添加小记的BottomSheet
        if (showAddBottomSheet.value) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                onDismissRequest = {
                    showAddBottomSheet.value = false
                },
                sheetState = addSheetState
            ) {
                // Sheet content
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 手机蜂鸣提醒
                            vibrate(context, 100)
                            if (selectedIndex.intValue == 0) {
                                launcherForTakePhoto.launch(null)
                            } else {
                                launcherForMediaStore.launch("image/*")
                            }
                        })
                    {
                        if(selectedImage.value != null)
                        {
                            Image(bitmap = selectedImage.value!!.asImageBitmap(),
                                contentDescription = "selected image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .height(200.dp)
                                    .align(Alignment.Center),
                                )
                        }else{
                            GlideImage(model = hotImage.value?.imageUri,
                                contentDescription = "",
                                contentScale = ContentScale.FillWidth)
                        }
                        Box(modifier = Modifier.matchParentSize())
                        {
                            Text(text = "点击更换图片",modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp))
                    {
                        Column(modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            SelectFromWhere(selectedIndex)
                            Spacer(modifier = Modifier.height(10.dp))
                            TextField(value = selectedTitle.value!!,
                                onValueChange = {
                                                diaryViewModel.updateTitle(it)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "标题")
                                })
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround) {
                                TextField(value = selectedLocation.value!!,
                                    onValueChange = { diaryViewModel.updateLocation(it) },
                                    modifier = Modifier.weight(1f),
                                    placeholder = {
                                        Text(text = "地点")
                                    })
                                Spacer(modifier = Modifier.width(10.dp))
                                TextField(value = selectedAuthor.value!!, onValueChange = {
                                    diaryViewModel.updateAuthor(it) },
                                    modifier = Modifier.weight(1f),
                                    placeholder = {
                                        Text(text = "作者")
                                    })
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            TextField(
                                value = selectedDetail.value!!,
                                onValueChange = {
                                    diaryViewModel.updateDetail(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(color = Color.Transparent),
                                shape = RectangleShape, // 设置边框形状为空
                                )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround) {
                                ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 2.dp
                                    ),onClick = {
                                    addScope.launch { addSheetState.hide() }.invokeOnCompletion {
                                        if (!addSheetState.isVisible) {
                                            showAddBottomSheet.value = false
                                        }
                                    }
                                }) {
                                    Text(text = "取消")
                                }
                                ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 2.dp
                                    ),onClick = {
                                    selectedImage.value?.let {
                                        diaryViewModel.saveImageToDataBase(
                                            it,selectedTitle.value!!, selectedDetail.value!!,
                                            selectedAuthor.value!!,selectedLocation.value!!
                                        )
                                    }
                                        addScope.launch { addSheetState.hide() }.invokeOnCompletion {
                                            if (!addSheetState.isVisible) {
                                                showAddBottomSheet.value = false
                                            }
                                        }
                                }) {
                                    Text(text = "保存")
                                }
                            }
                        }
                    }
                }
            }
        }

        if(showEditBottomSheet.value)
        {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                onDismissRequest = {
                    showAddBottomSheet.value = false
                },
                sheetState = addSheetState
            ) {
                // Sheet content
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 手机蜂鸣提醒
                            vibrate(context, 100)
                            if (selectedIndex.intValue == 0) {
                                launcherForTakePhoto.launch(null)
                            } else {
                                launcherForMediaStore.launch("image/*")
                            }
                        })
                    {
                        if(selectedImage.value != null)
                        {
                            Image(bitmap = selectedImage.value!!.asImageBitmap(),
                                contentDescription = "selected image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .height(200.dp)
                                    .align(Alignment.Center),
                            )
                        }else{
                            GlideImage(model = hotImage.value?.imageUri,
                                contentDescription = "",
                                contentScale = ContentScale.FillWidth)
                        }
                        Box(modifier = Modifier.matchParentSize())
                        {
                            Text(text = "点击更换图片",modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp))
                    {
                        Column(modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            SelectFromWhere(selectedIndex)
                            Spacer(modifier = Modifier.height(10.dp))
                            TextField(value = selectedTitle.value!!,
                                onValueChange = { diaryViewModel.updateTitle(it) },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = "标题")
                                })
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround) {
                                TextField(value = selectedLocation.value!!,
                                    onValueChange = { },
                                    enabled = false,
                                    modifier = Modifier.weight(1f),
                                    placeholder = {
                                        Text(text = "地点")
                                    })
                                Spacer(modifier = Modifier.width(10.dp))
                                TextField(value = selectedAuthor.value!!, onValueChange = {},
                                    modifier = Modifier.weight(1f),
                                    enabled = false,
                                    placeholder = {
                                        Text(text = "作者")
                                    })
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            TextField(
                                value = selectedDetail.value!!,
                                onValueChange = {
                                    diaryViewModel.updateDetail(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(color = Color.Transparent),
                                shape = RectangleShape, // 设置边框形状为空
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround) {
                                ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 2.dp
                                    ),onClick = {
                                        editScope.launch { editSheetState.hide() }.invokeOnCompletion {
                                            if (!editSheetState.isVisible) {
                                                showEditBottomSheet.value = false
                                            }
                                        }
                                    }) {
                                    Text(text = "取消")
                                }
                                ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 2.dp
                                    ),onClick = {
                                        selectedImage.value?.let {
                                            mayEditDiary.value?.let { mySelectedDiary ->
                                                diaryViewModel.updateImageToDataBase(
                                                    mySelectedDiary,it,selectedDetail.value!!,selectedTitle.value!!
                                                )
                                            }
                                        }
                                        editScope.launch { editSheetState.hide() }.invokeOnCompletion {
                                            if (!editSheetState.isVisible) {
                                                showEditBottomSheet.value = false
                                            }
                                        }
                                    }) {
                                    Text(text = "保存")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFromWhere(selectedIndex: MutableState<Int>) {
    val options = listOf("拍照", "相册")
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { selectedIndex.value = index },
                selected = index == selectedIndex.value
            ) {
                Text(label)
            }
        }
    }
}
