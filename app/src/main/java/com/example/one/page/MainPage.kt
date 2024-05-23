package com.example.one.page

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.myui.MainPageUi.StateBox.StateBox
import com.example.one.myui.MainPageUi.HelloBox
import com.example.one.myui.HotMap.HotMap
import com.example.one.myui.MainPageUi.MainPageController
import com.example.one.vm.HotMapViewModel
import com.example.one.vm.MyHotMapViewModelFactory

@Composable
fun MainPage()
{
    // 提升
    val hotMapViewModel: HotMapViewModel = viewModel(LocalContext.current as ComponentActivity
        ,factory = MyHotMapViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    val mainPageState = hotMapViewModel.type.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.6f)) {
            HelloBox(mainPageState,Modifier.fillMaxSize())
        }
        Box(modifier = Modifier.weight(9f))
        {
            Column (modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                HotMap(hotMapViewModel,mainPageState,Modifier.weight(1.6f))
                MainPageController(hotMapViewModel::changeType,Modifier.weight(0.6f))
                StateBox(hotMapViewModel::update,mainPageState, modifier = Modifier.weight(4f))
            }
        }
    }
}

