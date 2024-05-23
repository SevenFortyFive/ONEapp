package com.example.one.myui.MainPageUi.StateBox

import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.data.PlayerData.PlayerState
import com.example.one.helper.TimeFormatUtils
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentYear
import com.example.one.helper.vibrate
import com.example.one.myui.UtilsUi.MyChart
import com.example.one.setting.Setting
import com.example.one.timer.TimerStatus.IStatus
import com.example.one.timer.TimerStatus.StartedStatus
import com.example.one.vm.DataAnalyseViewModel
import com.example.one.vm.MyAudioViewModelFactory
import com.example.one.vm.MyDataAnalyseViewModelFactory
import com.example.one.vm.PlayerViewModel
import com.example.one.vm.TimerViewModel

/**
 * @since 2024/5/14
 */
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("ResourceType")
@Composable
fun MeditationStateBox(timerViewModel: TimerViewModel) {
    val dataViewMode : DataAnalyseViewModel = viewModel(
        LocalContext.current as ComponentActivity,
        factory = MyDataAnalyseViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    // 如果曾经查询过，重新设置为今天
    if(dataViewMode.ifDataSet)
    {
        dataViewMode.setTime(getCurrentYear(), getCurrentMonth(), getCurrentDay())
        dataViewMode.ifDataSet = false
    }

    val meditationData = dataViewMode.meditationData.observeAsState()
    val meditationOneDay = dataViewMode.meditationOneDay.observeAsState()

    var lastSum by remember { mutableFloatStateOf(0f) }
    val sumAnimatable = remember { Animatable(initialValue = lastSum) }

    var lastPercentage by remember { mutableFloatStateOf(0f) }
    val percentageAnimatable = remember { Animatable(initialValue = lastPercentage) }

    LaunchedEffect(meditationData.value) {
        val newSum = meditationData.value?.sumOf { (it.value).toDouble() }?.toFloat() ?: 0f
        sumAnimatable.animateTo(
            targetValue = newSum,
            animationSpec = tween(durationMillis = 1000)
        )

        lastSum = newSum

        val newPercentage = 100 * newSum / meditationOneDay.value!!

        percentageAnimatable.animateTo(
            targetValue = newPercentage,
            animationSpec = tween(durationMillis = 1000)
        )

        lastPercentage = newPercentage
    }
    val playerViewModel:PlayerViewModel = viewModel(LocalContext.current as ComponentActivity
        ,factory = MyAudioViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val dataList = playerViewModel.dataList.observeAsState()
    val currentData by playerViewModel.currentAudioData.observeAsState()
    val playerState = playerViewModel.currentState.observeAsState()
    if(dataList.value == null)
    {
        return
    }
    val verticalPagerState = rememberPagerState(pageCount = {
        dataList.value!!.size
    })
    // 同步标签和动画
    LaunchedEffect(verticalPagerState.currentPage){
        playerViewModel.setAudioItemWithIndex(verticalPagerState.currentPage)
    }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "今天",
                    fontSize = 35.sp
                )
                // 显示剩余时间
                Text(
                    text = TimeFormatUtils.formatTime(timerViewModel.timeLeft),
                    fontSize = 25.sp,
                    modifier = Modifier.padding(15.dp)
                )
            }
            Text(
                text = "${"%.2f".format(sumAnimatable.value)}h  ${"%.2f".format(percentageAnimatable.value)}%  目标${meditationOneDay.value}h",
                fontSize = 20.sp
            )
            meditationOneDay.value?.let { meditationOneDayValue ->
                MyChart(
                    meditationData.value?.map {
                        it.hour to it.value
                    },
                    meditationOneDayValue.toFloat(),
                    "min",
                    Modifier.weight(3f))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)){
                    VerticalPager(state = verticalPagerState,
                        modifier = Modifier.fillMaxSize(),
                        userScrollEnabled = timerViewModel.status.dragEnable()) {
                        Card(modifier = Modifier
                            .fillMaxSize()
                            .animateContentSize()
                            .padding(10.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            ),
                            shape = RoundedCornerShape(Setting.WholeElevation)
                        ) {
                            Row (modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Column(modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center) {
                                    Text(text = dataList.value!![it].name, fontSize = 20.sp)
                                    Text(text = dataList.value!![it].author, fontSize = 10.sp)
                                }
                                Row(modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        vibrate(context,100)
                                        playerViewModel.changeItemLoveState(currentData!!)
                                    }) {
                                        if(currentData?.love == true)
                                        {
                                            Icon(imageVector = Icons.Rounded.Favorite, contentDescription = "Love Audio", tint = Color.Red)
                                        }else{
                                            Icon(imageVector = Icons.Rounded.FavoriteBorder, contentDescription = "Love Audio")
                                        }
                                    }
                                    IconButton(onClick = {
                                        vibrate(context,100)
                                        if(playerState.value == PlayerState.STOP)
                                        {
                                            playerViewModel.start()
                                        }else{
                                            playerViewModel.pause()
                                        }
                                    }) {
                                        if (playerState.value == PlayerState.PLAYING)
                                        {
                                            Icon(imageVector = Icons.Rounded.Close, contentDescription = "pause")
                                        }else{
                                            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = "begin")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                TimerInMeditationStateBox(timerViewModel,modifier = Modifier.weight(1f))
            }
        }
    }
}

/**
 * @since 2024/5/14
 */
@Composable
fun TimerInMeditationStateBox(
    timerViewModel: TimerViewModel,
    modifier: Modifier = Modifier
){
    // 视频开始时将背景音乐关闭
    val vm: PlayerViewModel = viewModel(
        LocalContext.current as ComponentActivity
        ,factory = MyAudioViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val context = LocalContext.current
    Box(modifier = modifier)
    {
        Row (modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically){

            // 时间输入
            MeditationStateDataPicker(timerViewModel.status,timerViewModel::updateValue, modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.width(10.dp))
            Column {
                // 开始按键
                ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp
                    ),onClick = {
                        vibrate(context,100)
                    vm.pause()
                    if(timerViewModel.status is StartedStatus)
                    {
                        timerViewModel.status.clickPauseButton()
                    }else{
                        timerViewModel.status.clickStartButton()
                    }
                },
                    enabled = timerViewModel.totalTime > 0
                ) {
                    Text(text = timerViewModel.status.startButtonDisplayString())
                }
                // 暂停按键
                ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp
                    ),onClick = {
                        vibrate(context,100)
                    timerViewModel.status.clickStopButton()
                },
                    enabled = timerViewModel.status.stopButtonEnabled()
                ) {
                    Text(text = "停止")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MeditationStateDataPicker(
    isStatus: IStatus,
    updateValue: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val dataList = listOf(25,20,15,10,5)
    val verticalPagerState = rememberPagerState(pageCount = {
        dataList.size
    })
    LaunchedEffect(verticalPagerState.currentPage) {
        // 更新时间
        updateValue((dataList[verticalPagerState.currentPage] * 60).toString())
    }

    Card( modifier = modifier
        .animateContentSize()
        .padding(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            VerticalPager(state = verticalPagerState,
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                userScrollEnabled = isStatus.dragEnable()){
                Box(modifier = Modifier.fillMaxSize()){
                    Text(text = dataList[it].toString(), fontSize = 20.sp, modifier = Modifier.align(
                        Alignment.Center))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "分", fontSize = 25.sp, modifier = Modifier.weight(1f))
        }
    }
}