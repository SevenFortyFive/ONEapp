package com.example.one.myui.MainPageUi.StateBox

import android.app.Application
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.data.getBreathTimeList
import com.example.one.data.getBreathTypeList
import com.example.one.helper.TimeFormatUtils
import com.example.one.helper.vibrate
import com.example.one.myui.UtilsUi.AlertDialogExample
import com.example.one.setting.Setting
import com.example.one.timer.TimerStatus.NotStartedStatus
import com.example.one.timer.TimerStatus.StartedStatus
import com.example.one.vm.BreathViewModel
import com.example.one.vm.MyBreathViewModelFactory
import com.example.one.vm.TimerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @since 2024/5/16
 * breathBox
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BreathStateBox(timerViewModel: TimerViewModel){
    val breathViewModel: BreathViewModel = viewModel(
        LocalContext.current as ComponentActivity
        ,factory = MyBreathViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val ifRunning = breathViewModel.ifRunning.observeAsState()
    // 模式数据
    val typeList = breathViewModel.typeList.observeAsState()

    val showBottomSheet = remember { mutableStateOf(false) }
    if(typeList.value == null)
    {
        return
    }
    val context = LocalContext.current
    // sheet控制状态
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val inputTitle = remember {
        mutableStateOf("")
    }
    val inputUse = remember {
        mutableStateOf("")
    }
    val inputDescription = remember {
        mutableStateOf("")
    }
    val inputOne = remember {
        mutableLongStateOf(0L)
    }
    val inputTwo = remember {
        mutableLongStateOf(0L)
    }
    val inputThree = remember {
        mutableLongStateOf(0L)
    }
    val inputFour = remember {
        mutableLongStateOf(0L)
    }

    // timeList选择序列
    val timeList = getBreathTimeList()

    // 是否允许震动
    val ifVibrate = remember {
        mutableStateOf(true)
    }
    // type选择序列状态控制
    val verticalPagerStateOne = rememberPagerState(pageCount = {
        typeList.value!!.size
    })
    // 时间选择序列状态控制
    val verticalPagerStateTwo = rememberPagerState(
        pageCount = {
            timeList.size
        }
    )
    val showCannotBeDeleteDialog = remember {
        mutableStateOf(false)
    }
    val confirmDeleteDialog = remember {
        mutableStateOf(false)
    }
    val currentType by remember {
        mutableStateOf(getBreathTypeList()[0])
    }
    LaunchedEffect(verticalPagerStateTwo.currentPage) {
        timerViewModel.updateValue((timeList[verticalPagerStateTwo.currentPage] * 60).toString())
    }
    Scaffold { paddingValue ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValue))
        {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.weight(2f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    // 呼吸动画
                    BreathCircleWithoutAnimate(
                        ifVibrate = ifVibrate,
                        ifRun = ifRunning,
                        type = listOf(
                            currentType.one,
                            currentType.two,
                            currentType.three,
                            currentType.four),
                        modifier = Modifier.weight(1f)
                    )
                    Column(modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = TimeFormatUtils.formatTime(timerViewModel.timeLeft),
                            fontSize = 25.sp, modifier = Modifier.weight(1f))
                        VerticalPager(
                            userScrollEnabled = timerViewModel.status.dragEnable(),
                            state = verticalPagerStateTwo,
                            modifier = Modifier.weight(2f)) {
                            Card(modifier = Modifier
                                .fillMaxSize()
                                .animateContentSize()
                                .padding(10.dp),
                                elevation =  CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )) {
                                Box(modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center){
                                    Text(text = timeList[it].toString() + "分钟",
                                        fontSize = 25.sp)
                                }
                            }
                        }
                    }
                }
                Row(modifier = Modifier.weight(2f)){
                    VerticalPager(
                        userScrollEnabled = timerViewModel.status.dragEnable(),
                        state = verticalPagerStateOne,
                        modifier = Modifier.weight(1f)) {
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .padding(10.dp),
                            elevation =  CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )) {
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center) {
                                Text(text = typeList.value!![it].title, fontSize = 20.sp)
                                Text(text = typeList.value!![it].describe)
                                Box(modifier = Modifier.fillMaxSize())
                                {
                                    Row(modifier = Modifier.align(Alignment.BottomStart)) {
                                        IconButton(onClick = {showBottomSheet.value = true}) {
                                            Icon(imageVector = Icons.Rounded.Add, contentDescription = "add breath type")
                                        }
                                        IconButton(onClick = {
                                            if(it < 4)
                                            {
                                                showCannotBeDeleteDialog.value = true
                                            }else{
                                                confirmDeleteDialog.value = true
                                            }
                                        }) {
                                            Icon(imageVector = Icons.Rounded.Delete, contentDescription = "delete breath type")
                                        }
                                    }
                                }
                            }
                        }
                        if(confirmDeleteDialog.value)
                        {
                            AlertDialogExample(
                                onDismissRequest = { confirmDeleteDialog.value = false },
                                onConfirmation = { confirmDeleteDialog.value = false
                                    breathViewModel.deleteType(typeList.value!![it])},
                                dialogTitle = "确认",
                                dialogText = "是否删除",
                                icon = Icons.Rounded.Delete
                            )
                        }
                    }
                }
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically){
                    ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp
                        ),onClick = {
                            vibrate(context,100)
                            if(timerViewModel.status is StartedStatus)
                            {
                                timerViewModel.status.clickPauseButton()
                            }else
                            {
                                timerViewModel.status.clickStartButton()
                            }
                            if(ifRunning.value == true)
                            {
                                breathViewModel.setNotRunning()
                            }else{
                                breathViewModel.setRunning()
                            }
                    }
                    ) {
                        Text(text = timerViewModel.status.startButtonDisplayString())
                    }
                    ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp
                        ),onClick = {
                        vibrate(context,100)
                        breathViewModel.setNotRunning()
                        timerViewModel.status.clickStopButton()
                    }) {
                        Text(text = "停止")
                    }
                }
            }
        }

        // 用与添加type的sheet
        if (showBottomSheet.value) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                onDismissRequest = {
                    showBottomSheet.value = false
                },
                sheetState = sheetState) {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center){

                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            TextEditInBreathState(text = inputTitle, label = "标题", modifier = Modifier.weight(1f))
                            TextEditInBreathState(text = inputUse, label = "用法", modifier = Modifier.weight(1f))
                        }
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center)  {
                            TextEditInBreathState(text = inputDescription, label = "描述")
                        }

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            LongInputInBreathStateBox(number = inputOne, label = "吸气")
                            LongInputInBreathStateBox(number = inputTwo, label = "屏住吸气")
                            LongInputInBreathStateBox(number = inputThree, label = "呼气")
                            LongInputInBreathStateBox(number = inputFour, label = "屏住呼气")
                        }

                        Row (modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically){
                            ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 2.dp
                                ),onClick = {
                                vibrate(context,100)
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet.value = false
                                    }
                                }
                            }){
                                Text(text = "取消")
                            }
                            ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 2.dp
                                ),onClick = {
                                vibrate(context,100)
                                breathViewModel.addType(
                                    inputTitle.value,
                                    inputDescription.value,
                                    inputUse.value,
                                    inputOne.longValue,
                                    inputTwo.longValue,
                                    inputThree.longValue,
                                    inputFour.longValue
                                )
                                showBottomSheet.value = false
                            }) {
                                Text(text = "保存")
                            }
                        }

                    }
                }
            }
        }
    }

    if(showCannotBeDeleteDialog.value)
    {
        AlertDialogExample(
            onDismissRequest = {
                               showCannotBeDeleteDialog.value = false
            },
            onConfirmation = { showCannotBeDeleteDialog.value = false },
            dialogTitle = "禁止",
            dialogText = "这个模式不能被删除",
            icon = Icons.Rounded.Info
        )
    }

}

/**
 * @since 2024/5/16
 * breathState呼吸动画
 */
@Composable
fun BreathCircleWithoutAnimate(
    ifVibrate: MutableState<Boolean>,
    ifRun: State<Boolean?>,
    type: List<Long>,
    modifier: Modifier = Modifier){
    val context = LocalContext.current
    var mySize by remember { mutableFloatStateOf(0f) }
    val myText = remember {
        mutableStateOf("")
    }
    LaunchedEffect(ifRun.value) {
        Log.d("time",type.toString())
        while (true) {
            val animatable = Animatable(0f)
            if(!ifRun.value!!)
            {
                myText.value = ""
                animatable.stop()
                val animation2 = Animatable(mySize)
                animation2.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = type[0].toInt())
                ) {
                    mySize = 1f * this.value
                }
                break
            }
            myText.value = "吸气"
            // 吸气
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = type[0].toInt())
            ) {
                mySize = 1f * this.value // 变大
            }
            if(type[1] != 0L)
            {
                myText.value = "停顿"
                // 停顿
                delay(type[1])
            }
            if(ifVibrate.value)
            {
                vibrate(context,200)
            }

            myText.value = "呼气"
            // 呼气
            animatable.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = type[2].toInt())
            ) {
                mySize = 1f * this.value // 变小
            }
            if(type[3] != 0L)
            {
                myText.value = "停顿"
                // 停顿
                delay(type[3])
            }
            if(ifVibrate.value)
            {
                vibrate(context,200)
            }
        }
    }

    Box(modifier = modifier)
    {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val width = this.size.width
                val height = this.size.height

                val centerX = width / 2
                val centerY = height / 2

                val bigCircleSize = (width / 2) * 0.4
                val smallCircleSize = (width / 2) * 0.4 * mySize

                drawCircle(
                    color = Color.Gray,
                    radius = bigCircleSize.toFloat(),
                    center = Offset(centerX, centerY)
                )

                drawCircle(
                    color = Color.LightGray,
                    radius = smallCircleSize.toFloat(),
                    center = Offset(centerX, centerY)
                )
            }
            Text(text = myText.value)
        }
    }
}

@Composable
fun TextEditInBreathState(text:MutableState<String>,label:String,modifier: Modifier = Modifier){
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(label) },
        singleLine = true,
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    )
}

@Composable
fun LongInputInBreathStateBox(number:MutableLongState,label: String){
    val context = LocalContext.current
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
    {
        Text(text = label+": "+(number.longValue/1000).toString()+"s",
            fontSize = 25.sp)
        Row {
            ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp
                ),onClick = {
                vibrate(context,100)
                if(number.longValue >= 1000L)
                {
                    number.longValue -= 1000
                }
            }) {
                Text(text = "减1s")
            }
            Spacer(modifier = Modifier.width(10.dp))
            ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp
                ),onClick = {
                vibrate(context,100)
                number.longValue += 1000
            }) {
                Text(text = "加1s")
            }
        }
    }
}