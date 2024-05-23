package com.example.one.myui.MainPageUi.StateBox

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.helper.TimeFormatUtils
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentYear
import com.example.one.myui.UtilsUi.AlertDialogExample
import com.example.one.myui.UtilsUi.MyChart
import com.example.one.myui.UtilsUi.ProgressCircle
import com.example.one.setting.Setting
import com.example.one.vm.AppViewModel
import com.example.one.vm.DataAnalyseViewModel
import com.example.one.vm.MyDataAnalyseViewModelFactory
import com.example.one.vm.TimerViewModel

/**
 * @since 2024/5/15
 */
@Composable
fun ClockStateBox(timerViewModel: TimerViewModel){
    // 获取全局appViewModel监控反转状态
    val appViewModel = AppViewModel.getInstance()
    val ifUpsetDown = appViewModel.idUpsetDown.observeAsState()

    val ifShowDialog = remember {
        mutableStateOf(false)
    }


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

    val clockOneDay = dataViewMode.clockOneDay.observeAsState()
    val data = dataViewMode.clockData.observeAsState()

    var lastSum by remember { mutableFloatStateOf(0f) }
    val sumAnimatable = remember { Animatable(initialValue = lastSum) }

    var lastPercentage by remember { mutableFloatStateOf(0f) }
    val percentageAnimatable = remember { Animatable(initialValue = lastPercentage) }

    LaunchedEffect(data.value) {
        val newSum = data.value?.sumOf { it.value.toDouble() }?.toFloat() ?: 0f
        sumAnimatable.animateTo(
            targetValue = newSum,
            animationSpec = tween(durationMillis = 1000)
        )

        lastSum = newSum

        // 百分比
        val newPercentage = newSum*100 / clockOneDay.value!!

        percentageAnimatable.animateTo(
            targetValue = newPercentage,
            animationSpec = tween(durationMillis = 1000)
        )

        lastPercentage = newPercentage
    }

    Box(modifier = Modifier.fillMaxSize())
    {
        Column {
            Row(modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = "今天",
                    fontSize = 35.sp
                )
                Text(text = TimeFormatUtils.formatTime(timerViewModel.timeLeft),
                    fontSize = 25.sp,
                    modifier = Modifier.padding(15.dp))
            }
            Text(
                text = "${"%.2f".format(sumAnimatable.value)}min  ${"%.2f".format(percentageAnimatable.value)}%  目标${clockOneDay.value}min",
                fontSize = 20.sp
            )
            clockOneDay.value?.let { clockOneDayValue ->
                MyChart(
                    data.value?.map {
                        it.hour to it.value
                    }, clockOneDayValue.toFloat(),"min",
                    modifier = Modifier.weight(3f))
            }
            ClockStateBoxController(
                ifUpsetDown,
                ifShowDialog,
                timerViewModel = timerViewModel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ClockStateBoxController(
    ifUpsetDown: State<Boolean?>,
    ifShowDialog:MutableState<Boolean>,
    timerViewModel: TimerViewModel,
    modifier: Modifier = Modifier
){
    val tryToBegin = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(ifUpsetDown.value,tryToBegin.value) {
        if(tryToBegin.value && ifUpsetDown.value == true)
        {
            // 如果尝试开始并且翻转，那么取消提示并且开始
            ifShowDialog.value = false
            timerViewModel.status.clickStartButton()
        }else if(tryToBegin.value && ifUpsetDown.value == false)
        {
            // 如果尝试开始但是没有翻转，那么暂停并且提示
            timerViewModel.status.clickPauseButton()
            ifShowDialog.value = true
        }
    }

    if(ifShowDialog.value)
    {
        AlertDialogExample(
            onDismissRequest = {
                ifShowDialog.value = false
                tryToBegin.value = false
                timerViewModel.status.clickStopButton()
            },
            onConfirmation = {
            },
            dialogTitle = "请翻转手机",
            dialogText = "若不翻转手机那么无法开始\n若时长已经结束请点击取消",
            icon = Icons.Default.Favorite
        )
    }

    Row(modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround) {
        ProgressCircle(viewModel = timerViewModel,modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier
            .weight(1.5f)
            .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally){
            DataInputInClockStateBox(timerViewModel::updateValue,Modifier.weight(1f))
            Row(modifier = Modifier.weight(1.5f),
                verticalAlignment = Alignment.CenterVertically){
                ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp
                    ),onClick = {
                    // 如果设置了时长
                    if(timerViewModel.timeLeft != 0L)
                    {
                        tryToBegin.value = true
                        // 提示进行翻转或者平放
                        ifShowDialog.value = true
                    }
                }) {
                    Text(text = timerViewModel.status.startButtonDisplayString())
                }
                Spacer(modifier = Modifier.width(10.dp))
                ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp
                    ),onClick = {
                    timerViewModel.status.clickStopButton()
                },
                    enabled = timerViewModel.status.stopButtonEnabled()) {
                    Text(text = "停止")
                }
            }
        }
    }
}

@Composable
fun DataInputInClockStateBox(setLeftTime:(String) -> Unit, modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column(modifier = modifier.fillMaxWidth()) {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it
                            setLeftTime(it.toString())},
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 3,
            valueRange = 0f..6 * 60f
        )
        Text(text = sliderPosition.toString())
    }
}