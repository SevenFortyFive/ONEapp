package com.example.one.myui.HotMap

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.one.data.MainPageState
import com.example.one.data.SQLite.entity.MyHotMapData
import com.example.one.helper.CurrentIntDay
import com.example.one.helper.CurrentIntMonth
import com.example.one.helper.CurrentIntYear
import com.example.one.helper.findPreviousMonth
import com.example.one.helper.getCaraWithLe
import com.example.one.helper.getCurrentDay
import com.example.one.helper.getCurrentMonth
import com.example.one.helper.getCurrentMonthBeginWithWhichInMonth
import com.example.one.helper.getCurrentYear
import com.example.one.helper.getEnCaraWithLe
import com.example.one.helper.getNumOfDay
import com.example.one.setting.Setting
import com.example.one.vm.HotMapViewModel


@Composable
fun HotMap(vm: HotMapViewModel, mainPageState: State<MainPageState?>, modifier:Modifier = Modifier)
{
    val dataList = listOf(
        vm.dataListOne.observeAsState(),
        vm.dataListTwo.observeAsState(),
        vm.dataListThree.observeAsState(),
        vm.dataListFour.observeAsState()
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)) {
            Row(modifier = Modifier
//                .fillMaxSize()
                .padding(5.dp)
                .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
                ) {
                for (i in 0..3) {
                    val yearAndMonth = findPreviousMonth(getCurrentYear(), getCurrentMonth(),3-i)
                    val year = yearAndMonth.first
                    val month = yearAndMonth.second
                    // 获取某一个月的天数
                    val numOfDay =
                        getNumOfDay(year, month)

                    // 某一个月需要的列数
                    val numOfColumn = (numOfDay / 7) + 1

                    // 标记某月的第几天
                    var currentDay = 1

                    Row {
                        for (k in 1..numOfColumn) {
                            Column {
                                if (k == 1)
                                    TextCell(string = getCaraWithLe(CurrentIntMonth - 3 + i))
                                else
                                    EmptyCell()
                                Spacer(modifier = Modifier.height(Setting.CellPadding + 1.dp))

                                for (j in 1..7) {
                                    if(dataList[i].value ==null || dataList[i].value?.size!! < currentDay)
                                    {
                                        return@Card
                                    }
                                    val template = when(mainPageState.value)
                                    {
                                        MainPageState.BREATH -> dataList[i].value?.get(currentDay-1)?.breath
                                        MainPageState.DRINK -> dataList[i].value?.get(currentDay-1)?.drink
                                        MainPageState.CLOCK -> dataList[i].value?.get(currentDay-1)?.clock
                                        MainPageState.MEDITATION -> dataList[i].value?.get(currentDay-1)?.meditation
                                        null -> -1
                                    }
                                    if (template != null) {
                                        Cell(
                                            template,
                                            i == 3 && currentDay == CurrentIntDay
                                        )
                                    }
                                    if (currentDay++ == numOfDay)
                                        break;
                                    if (j != 7)
                                        Spacer(modifier = Modifier.height(Setting.CellPadding))
                                }
                            }
                            if (k != numOfColumn || i != 3)
                                Spacer(modifier = Modifier.width(Setting.CellPadding))
                        }
                    }
                }
            }
        }
}

@Composable
fun ComprehensiveHotMap(vm: HotMapViewModel,modifier: Modifier = Modifier){
    val data = vm.dataListFour.observeAsState()
    if(data.value == null || data.value!!.size < getNumOfDay(getCurrentYear(), getCurrentMonth()))
    {
        return
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp)
            .animateContentSize()
            .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)
    ) {
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)){
            Text(text = "${getEnCaraWithLe(getCurrentMonth())} ${getCurrentYear()}", fontSize = 30.sp)
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier){
                    ComprehensiveTextCell(string = "日")
                    Spacer(modifier = Modifier.width(Setting.ComprehensiveHotMapCellPadding))
                    ComprehensiveTextCell(string = "一")
                    Spacer(modifier = Modifier.width(Setting.ComprehensiveHotMapCellPadding))
                    ComprehensiveTextCell(string = "二")
                    Spacer(modifier = Modifier.width(Setting.ComprehensiveHotMapCellPadding))
                    ComprehensiveTextCell(string = "三")
                    Spacer(modifier = Modifier.width(Setting.ComprehensiveHotMapCellPadding))
                    ComprehensiveTextCell(string = "四")
                    Spacer(modifier = Modifier.width(Setting.ComprehensiveHotMapCellPadding))
                    ComprehensiveTextCell(string = "五")
                    Spacer(modifier = Modifier.width(Setting.ComprehensiveHotMapCellPadding))
                    ComprehensiveTextCell(string = "六")
                }
                Spacer(modifier = Modifier.height(Setting.ComprehensiveHotMapCellPadding))
                // 获取当前月份天数
                val numOfDay = getNumOfDay(CurrentIntYear, CurrentIntMonth)
                // 当前天数
                var currentDay = 1
                // 计算有几行
                val numOfRow = numOfDay/7 + 1
                // 计算当前月份第一天是星期几
                val numOfEmpty = when(getCurrentMonthBeginWithWhichInMonth()){
                    7 -> 0
                    else -> getCurrentMonthBeginWithWhichInMonth()
                }
                Column {
                    for(i in 1..numOfRow)
                    {
                        // 第一行
                        if(i == 1)
                        {
                            Row {
                                for(j in 1..numOfEmpty)
                                {
                                    ComprehensiveEmptyCell()
                                    Spacer(modifier = Modifier.width(Setting.ComprehensiveHotMapCellPadding))
                                }
                                for(j in 1..7-numOfEmpty)
                                {
                                    ComprehensiveHotMapCell(value =
                                    (data.value?.get(currentDay-1)?.breath ?: 0) +
                                            (data.value?.get(currentDay-1)?.meditation ?: 0) +
                                            (data.value?.get(currentDay-1)?.drink ?: 0) +
                                            (data.value?.get(currentDay-1)?.clock ?: 0),
                                        currentDay == CurrentIntDay
                                    )
                                    if(j != 7 -numOfEmpty)
                                    {
                                        Spacer(modifier = Modifier.width(Setting.ComprehensiveHotMapCellPadding))
                                    }
                                    currentDay++
                                }
                            }
                        }else{
                            Row {
                                for(j in 1..7)
                                {
                                    ComprehensiveHotMapCell(value =
                                    (data.value?.get(currentDay-1)?.breath ?: 0) +
                                            (data.value?.get(currentDay-1)?.meditation ?: 0) +
                                            (data.value?.get(currentDay-1)?.drink ?: 0) +
                                            (data.value?.get(currentDay-1)?.clock ?: 0),
                                        currentDay == getCurrentDay()
                                    )
                                    if(j != 7)
                                    {
                                        Spacer(modifier = Modifier.width(Setting.ComprehensiveHotMapCellPadding))
                                    }
                                    currentDay++
                                    if(currentDay == numOfDay)
                                    {
                                        break
                                    }
                                }
                            }
                        }
                        if(i != numOfRow)
                        {
                            Spacer(modifier = Modifier.height(Setting.ComprehensiveHotMapCellPadding))
                        }
                    }
                }
            }

            Row(modifier = Modifier.weight(2f)){
                ComprehensiveHotMapDetail(data)
            }
        }
    }
}

@Composable
fun ComprehensiveHotMapDetail(data: State<List<MyHotMapData>?>,modifier: Modifier = Modifier) {
    val sum1 = remember { Animatable(initialValue = 0f) }
    val sum2 = remember { Animatable(initialValue = 0f) }
    val sum3 = remember { Animatable(initialValue = 0f) }
    val sum4 = remember { Animatable(initialValue = 0f) }
    val currentData = data.value?.get(getCurrentDay() - 1) ?: return
    LaunchedEffect(data.value) {
        sum1.animateTo(
            targetValue = currentData.breath.toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
        sum2.animateTo(
            targetValue = currentData.clock.toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
        sum3.animateTo(
            targetValue = currentData.meditation.toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
        sum4.animateTo(
            targetValue = currentData.drink.toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Column(modifier = modifier
        .fillMaxHeight()
        .padding(10.dp)) {
        Box(modifier = Modifier.weight(1f))
        {
            Row(modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top) {
                Text(text = getCurrentDay().toString(), fontSize = 30.sp)
                Text(text = "${getEnCaraWithLe(getCurrentMonth())} ${getCurrentYear()}")
            }
        }
//        Divider()
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            Row(modifier = Modifier.weight(1f),
//                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "深呼吸", fontSize = 20.sp)
                Text(text = sum1.value.toInt().toString()+ "次")
            }
            Row(modifier = Modifier.weight(1f),
//                horizontalArrangement = Arrangement.Center,
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
//                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "专注",fontSize = 20.sp)
                Text(text = sum3.value.toInt().toString()+ "次")
            }
            Row(modifier = Modifier
                .weight(1f)
                .weight(1f),
//                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "饮水",fontSize = 20.sp)
                Text(text = sum4.value.toInt().toString() + "次")
            }
        }
    }
}