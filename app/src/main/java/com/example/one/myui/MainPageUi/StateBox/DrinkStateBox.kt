package com.example.one.myui.MainPageUi.StateBox

import android.annotation.SuppressLint
import android.app.Application
import android.view.MotionEvent
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ThumbUp
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.data.SQLite.entity.MyDrinkData
import com.example.one.data.SharedPreferences.DRINK_TIME_KEY
import com.example.one.data.SharedPreferences.MySharedPreference
import com.example.one.data.SharedPreferences.SharedPreferencesHelper
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentHour
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentYear
import com.example.one.myui.UtilsUi.AlertDialogExample
import com.example.one.myui.UtilsUi.MyChart
import com.example.one.vm.DataAnalyseViewModel
import com.example.one.vm.MyDataAnalyseViewModelFactory
import com.example.one.vm.SPViewModel
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

/**
 * @since 2024/5/13
 */
@SuppressLint("MutableCollectionMutableState")
@Composable
fun DrinkStateBox(
    updateHotMap: () -> Unit
) {
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

    val drinkData = dataViewMode.drinkData.observeAsState()
    val drinkOneDay = dataViewMode.drinkOneDay.observeAsState()

    var lastSum by remember { mutableFloatStateOf(0f) }
    val sumAnimatable = remember { Animatable(initialValue = lastSum) }

    var lastPercentage by remember { mutableFloatStateOf(0f) }
    val percentageAnimatable = remember { Animatable(initialValue = lastPercentage) }

    LaunchedEffect(drinkData.value) {
        val newSum = drinkData.value?.sumOf { it.value.toDouble() }?.toFloat() ?: 0f
        sumAnimatable.animateTo(
            targetValue = newSum,
            animationSpec = tween(durationMillis = 1000)
        )

        lastSum = newSum

        val newPercentage = newSum * 100 / drinkOneDay.value!!

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
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "今天",
                    fontSize = 35.sp
                )
                Text(text = "",
                    modifier = Modifier.padding(15.dp))
            }
            Text(
                text = "${"%.2f".format(sumAnimatable.value)}L  ${"%.2f".format(percentageAnimatable.value)}%  目标${drinkOneDay.value}L",
                fontSize = 20.sp
            )
            drinkOneDay.value?.let {
                MyChart(
                    drinkData.value?.map {
                        it.hour to it.value
                    },
                    it.toFloat(),
                    "L",
                    Modifier.weight(3f))
            }
            DrinkController(updateHotMap,dataViewMode::addDrinkData, Modifier.weight(1f))
        }
    }
}
/**
 * @since 2024/5/13
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrinkController(updateHotMap: () -> Unit, addDrinkData: (MyDrinkData)-> Unit, modifier: Modifier) {
    val sPViewModel: SPViewModel = viewModel(LocalContext.current as ComponentActivity)
    var count by remember { mutableFloatStateOf(0f) }
    var isButtonPressed by remember { mutableStateOf(false) }
    var timer: Timer? by remember { mutableStateOf(null) }
    val ifShowDialog = remember {
        mutableStateOf(false)
    }

    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround) {
        Text(
            text = "${"%.2f".format(count)}L",
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier
            .padding(16.dp)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isButtonPressed = true
                        if (timer == null) {
                            timer = Timer()
                            timer!!.scheduleAtFixedRate(1000, 100) {
                                count += 0.01f
                            }
                        }
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        isButtonPressed = false
                        timer?.cancel() // 在释放按钮时取消计时器
                        timer = null // 重置计时器
                        true
                    }

                    else -> false
                }
            }
        ) {
            Row(horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Rounded.ThumbUp, contentDescription = "drink")
            }
        }
        Row {
            IconButton(onClick = {
                if(count != 0f)
                {
                    addDrinkData(
                        MyDrinkData(
                        0,
                            getCurrentYear(),
                            getCurrentMonth(),
                            getCurrentDay(),
                            getCurrentHour(),
                            count
                        )
                    )
                    updateHotMap()
                    SharedPreferencesHelper.add(DRINK_TIME_KEY,(count*1000).toInt())
                    sPViewModel.onChange()
                    count = 0f
                }else{
                    ifShowDialog.value = true
                }
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add")
            }
            IconButton(onClick = {
                count = 0f
            }) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "cancel")
            }
        }
    }

    if(ifShowDialog.value)
    {
        AlertDialogExample(
            onDismissRequest = { ifShowDialog.value = false },
            onConfirmation = { ifShowDialog.value = false },
            dialogTitle = "请先倒杯水吧~",
            dialogText = "不倒水怎么喝",
            icon =  Icons.Rounded.CheckCircle
        )
    }
}

