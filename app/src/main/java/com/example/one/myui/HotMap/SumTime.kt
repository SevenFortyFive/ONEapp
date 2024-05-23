package com.example.one.myui.HotMap

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.setting.Setting
import com.example.one.vm.SPViewModel

@Composable
fun SumTime(modifier: Modifier = Modifier){
    val vm:SPViewModel = viewModel(LocalContext.current as ComponentActivity)
    val breathTime by vm.breathTime.observeAsState()
    val clockTime by vm.clockTime.observeAsState()
    val drinkTime by vm.drinkTime.observeAsState()
    val meditationTime by vm.meditationTime.observeAsState()

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
        Text(text = "时长统计", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                SmallSumTime("深呼吸",breathTime,"分",modifier.weight(1f))
                SmallSumTime("专注",clockTime,"分",modifier.weight(1f))
            }
            Row {
                SmallSumTime(type = "冥想", value = meditationTime,"分",modifier.weight(1f))
                SmallSumTime("饮水",drinkTime,"毫升",modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SmallSumTime(type:String,value: Int?,label:String = "",modifier: Modifier = Modifier) {

    Column(modifier = Modifier.then(modifier)) {
        Text(text = type, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = value.toString()+label)
    }

}