package com.example.one.myui.CharRiverUi

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.helper.LocalDpHelper
import com.example.one.myui.UtilsUi.LoadingIndicator
import com.example.one.vm.CharRiverViewmodel


@Composable
fun CharRiver(){
    val vm: CharRiverViewmodel = viewModel(LocalContext.current as ComponentActivity)

    val data by vm.data.observeAsState()

    val onLoading = vm.onLoading.observeAsState()

    val precision by vm.precision.observeAsState()

    val ifShow = remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onLongPress = {
                    ifShow.value = !ifShow.value
                }
            )
        },
      contentAlignment = Alignment.Center
    )
    {
        LoadingIndicator(loading = onLoading)
        Box(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
            contentAlignment = Alignment.Center){
            if(data != null)
            {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    Column {
                        for(row in data?.indices!!)
                        {
                            Row {
                                for( col in data!![row].indices)
                                {
                                    CellInCharRiver(value = data!![row][col],precision)
                                }
                            }
                        }
                    }
                }
            }
            else{

                Column(modifier = Modifier.fillMaxSize(),
                    Arrangement.Center,
                    Alignment.CenterHorizontally) {
                    Text(text = "长按界面来编辑并且启用此功能")
                    Text(text = "初次加载时间较长")
                }
            }
        }
        if(ifShow.value)
        {
            CharRiverEditor(Modifier,onLoading,vm::start,vm::stop,vm::setData)
        }
    }
}

@Composable
fun CellInCharRiver(value: Int, pre:Int? = 30)
{
    val color = if(value != 0)
    {
        Color.Green
    }
    else{
        Gray
    }
    Box(
        modifier = Modifier
            .padding(1.dp)
            .size((LocalDpHelper.getDpHeight().dp - 100.dp) / pre!! - 2.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color)
    )
}