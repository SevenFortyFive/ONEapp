package com.example.one.page

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentYear
import com.example.one.helper.getEnCaraWithLe
import com.example.one.helper.getMilliseconds
import com.example.one.helper.getYearMonthDay
import com.example.one.myui.UtilsUi.MyChart
import com.example.one.setting.Setting
import com.example.one.vm.DataAnalyseViewModel
import com.example.one.vm.MyDataAnalyseViewModelFactory

/**
 * @since 2025/5/17
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AnalysePage(){

    val dataViewMode : DataAnalyseViewModel = viewModel(
    LocalContext.current as ComponentActivity,
    factory = MyDataAnalyseViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    val selectedYear = dataViewMode.year.observeAsState()
    val selectedMonth = dataViewMode.month.observeAsState()
    val selectedDay = dataViewMode.day.observeAsState()

    val showBottomSheet = remember {
        mutableStateOf(false)
    }
    val timePicked = remember {
        mutableLongStateOf(
            getMilliseconds(
                getCurrentYear(),
                getCurrentMonth(),
                getCurrentDay()
            )
        )
    }

     LaunchedEffect(timePicked.longValue) {
         // 如果选择日期，更改ViewModel
         val date = getYearMonthDay(timePicked.longValue)
        dataViewMode.setTime(
            date.first,
            date.second,
            date.third
        )
     }

    val text = remember {
        mutableStateOf("")
    }

    LaunchedEffect(timePicked.longValue) {
        text.value = getYearMonthDay(timePicked.longValue).toString()
    }
    Scaffold { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            DayAnalyse(
                dataViewMode,
                showBottomSheet,
                selectedYear,
                selectedMonth,
                selectedDay
            )
        }

        if (showBottomSheet.value) {
            ModalBottomSheet(onDismissRequest = {
                showBottomSheet.value = false
            }) {
                MyDatePicker(timePicked)
            }
        }
    }
}
@Composable
fun DayAnalyse(
    dataViewMode: DataAnalyseViewModel,
    showBottomSheet: MutableState<Boolean>,
    selectYear: State<Int?>,
    selectMonth: State<Int?>,
    selectDay: State<Int?>
) {
    val breathOneDay = dataViewMode.breathOneDay.observeAsState()
    val clockOneDay = dataViewMode.clockOneDay.observeAsState()
    val meditationOneDay = dataViewMode.meditationOneDay.observeAsState()
    val drinkOneDay = dataViewMode.drinkOneDay.observeAsState()

    val drinkData = dataViewMode.drinkData.observeAsState()
    val clockData = dataViewMode.clockData.observeAsState()
    val breathData = dataViewMode.breathData.observeAsState()
    val meditationData = dataViewMode.meditationData.observeAsState()

    if(
        drinkData.value == null ||
        clockData.value == null ||
        breathData.value ==null ||
        meditationData.value == null ||
        breathOneDay.value == null ||
        clockData.value == null||
        meditationOneDay.value == null||
        drinkOneDay.value == null)
    {
        return
    }
    Box(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn (contentPadding = PaddingValues(10.dp)){
                item {
                    Row(modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        Row(modifier = Modifier,
                            verticalAlignment = Alignment.Top) {
                            Text(text = selectDay.value.toString(), fontSize = 30.sp)
                            Text(text = "${selectMonth.value?.let { getEnCaraWithLe(it) }} ${selectYear.value}")
                        }
                        IconButton(onClick = {
                            showBottomSheet.value = true
                        }) {
                            Icon(imageVector = Icons.Rounded.DateRange, contentDescription = "select date")
                        }
                    }
                }
                item {
                    Card(modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .padding(10.dp),
                        elevation =  CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(Setting.WholeElevation)){
                        Column(modifier = Modifier.height(200.dp)) {
                            Text(text = "今日总览", fontSize = 25.sp)
                            TodayTotalTime(
                                data1 = breathData.value!!.size,
                                data2 = meditationData.value!!.size,
                                data3 = clockData.value!!.size,
                                data4 = drinkData.value!!.size,
                                modifier = Modifier.height(200.dp)
                            )
                        }
                    }
                }

                item {
                    Card(modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .padding(10.dp),
                        elevation =  CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(Setting.WholeElevation)) {
                        breathData.value?.let {
                            var sum = 0f
                            it.forEach{data->
                                sum+=data.value}
                            TimeTotalBox(
                                number = it.size,
                                sumValue = sum,
                                percent = sum * 100 / breathOneDay.value!!,
                                oneDay = breathOneDay.value!!,
                                "呼吸总览",
                                "次",
                                "今日次数",
                                "分",
                                "今日时长")
                        }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .height(300.dp)
                            .padding(10.dp))
                        {
                            breathOneDay.value?.let { breathOneDayValue ->
                                MyChart(data = breathData.value?.map {
                                    it.hour to it.value
                                }, target = breathOneDayValue.toFloat(), label = "min", modifier = Modifier)
                            }

                        }
                    }
                }
                item {
                    Card(modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .padding(10.dp),
                        elevation =  CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(Setting.WholeElevation)) {
                        meditationData.value?.let {
                            var sum = 0f
                            it.forEach{data->
                                sum+=data.value}
                            TimeTotalBox(
                                number = it.size,
                                sumValue = sum,
                                sum * 100/ meditationOneDay.value!!,
                                meditationOneDay.value!!,
                                "冥想总览",
                                "次",
                                "今日次数",
                                "分",
                                "今日时长")
                        }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .height(300.dp)
                            .padding(10.dp))
                        {
                            meditationOneDay.value?.let { meditationOneDayValue ->
                                MyChart(
                                    data = meditationData.value?.map {
                                        it.hour to it.value
                                    },
                                    target = meditationOneDayValue.toFloat(), label = "min", modifier = Modifier)
                            }

                        }
                    }
                }

                item {
                    Card(modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .padding(10.dp),
                        elevation =  CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(Setting.WholeElevation)) {
                        clockData.value?.let {
                            var sum = 0f
                            it.forEach{data->
                                sum+=data.value}
                            TimeTotalBox(
                                number = it.size,
                                sumValue = sum,
                                sum * 100/ clockOneDay.value!!,
                                clockOneDay.value!!,
                                "专注总览",
                                "次",
                                "今日次数",
                                "分",
                                "今日时长")
                        }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .height(300.dp)
                            .padding(10.dp))
                        {
                            clockOneDay.value?.let { clockOneDayValue ->
                                MyChart(
                                    clockData.value?.map {
                                        it.hour to it.value
                                    }, clockOneDayValue.toFloat(),"min",
                                    modifier = Modifier)
                            }
                        }
                    }
                }

                item {
                    Card(modifier = Modifier
                        .fillMaxSize()
                        .animateContentSize()
                        .padding(10.dp),
                        elevation =  CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = RoundedCornerShape(Setting.WholeElevation)) {
                        drinkData.value?.let {
                            var sum = 0f
                            it.forEach{data->
                                sum+=data.value}
                            TimeTotalBox(
                                number = it.size,
                                sumValue = sum,
                                sum * 100/ drinkOneDay.value!!,
                                drinkOneDay.value!!,
                                "饮水总览",
                                "次",
                                "今日次数",
                                "升",
                                "今日饮量")
                        }

                        Box(modifier = Modifier
                            .fillMaxSize()
                            .height(300.dp)
                            .padding(10.dp))
                        {
                            drinkOneDay.value?.let { drinkOneDayValue ->
                                MyChart(
                                    data = drinkData.value?.map {
                                        it.hour to it.value
                                    },
                                    target = drinkOneDayValue.toFloat(),
                                    label = "L",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }

                item {
                    Row {
                        Text(text =
                        "数据来自:${selectYear.value}年${selectMonth.value}月${selectDay.value}", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(date:MutableLongState){
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis =
        date.longValue)
        DatePicker(state = datePickerState, modifier = Modifier.padding(16.dp))
        LaunchedEffect(datePickerState.selectedDateMillis) {
            date.longValue = datePickerState.selectedDateMillis!!
        }
        Text(
            "选择日期: ${datePickerState.selectedDateMillis ?: "没有选择"}",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun TimeTotalBox(
    number:Int,
    sumValue:Float,
    percent:Float,
    oneDay: Int,
    title:String = "总览",
    labelOne:String = "次",
    labelTwo:String = "今日次数",
    labelThree:String = "分钟",
    contentString: String = "今日",
    contentString2: String = "今日完成"){
    Column {
        Row {
            Text(text = title, fontSize = 25.sp)
//            Text(text = oneDay.toString())
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth()){
            Column(modifier = Modifier.weight(1f)){
                Row {
                    Text(text = number.toString(), fontSize = 30.sp)
                    Text(text = labelOne, fontSize = 15.sp)
                }
                Text(text = labelTwo)
            }
            Column(modifier = Modifier.weight(1f)){
                Row {
                    Text(text = "%.1f".format(sumValue), fontSize = 30.sp)
                    Text(text = labelThree, fontSize = 15.sp)
                }
                Text(text = contentString)
            }
            Column(modifier = Modifier.weight(1f)) {
                Row {
                    Text(text = "%.1f".format(percent), fontSize = 30.sp)
                    Text(text = "%", fontSize = 15.sp)
                }
                Text(text = contentString2)
            }
        }
    }
}

@Composable
fun TodayTotalTime(data1:Int,data2:Int,data3:Int,data4:Int, modifier: Modifier = Modifier) {
    val sum1 = remember { Animatable(initialValue = 0f) }
    val sum2 = remember { Animatable(initialValue = 0f) }
    val sum3 = remember { Animatable(initialValue = 0f) }
    val sum4 = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(data1,data2,data3,data4) {
        sum1.animateTo(
            targetValue = data1.toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
        sum2.animateTo(
            targetValue = data2.toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
        sum3.animateTo(
            targetValue = data3.toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
        sum4.animateTo(
            targetValue = data4.toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Column(modifier = modifier
        .fillMaxHeight()
        .padding(10.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            Row(modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "深呼吸", fontSize = 20.sp)
                Text(text = sum1.value.toInt().toString()+ "次")
            }
            Row(modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "冥想",fontSize = 20.sp)
                Text(text = sum2.value.toInt().toString()+ "次")
            }
        }
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            Row(modifier = Modifier
                .weight(1f)
                .weight(1f),
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "专注",fontSize = 20.sp)
                Text(text = sum3.value.toInt().toString()+ "次")
            }
            Row(modifier = Modifier
                .weight(1f)
                .weight(1f),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "饮水",fontSize = 20.sp)
                Text(text = sum4.value.toInt().toString() + "次")
            }
        }
    }
}