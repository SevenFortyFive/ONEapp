package com.example.one.myui.MainPageUi.StateBox

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.data.MainPageState
import com.example.one.myui.UtilsUi.MyTimer
import com.example.one.setting.Setting
import com.example.one.vm.TimerViewModel

/**
 * @since 2024/5/13
 * MainPage StateBox
 */
@Composable
fun StateBox(updateHotMap: ()->Unit, mainPageState: State<MainPageState?>, modifier: Modifier = Modifier){
    val timerViewModel: TimerViewModel = viewModel(LocalContext.current as ComponentActivity)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)
    ){
        when(mainPageState.value)
        {
            MainPageState.DRINK -> DrinkStateBox(updateHotMap = updateHotMap)
            MainPageState.MEDITATION -> MeditationStateBox(timerViewModel = timerViewModel)
            MainPageState.CLOCK -> ClockStateBox(timerViewModel = timerViewModel)
            MainPageState.BREATH -> BreathStateBox(timerViewModel = timerViewModel)
            else -> MyTimer(tiemrViewModel = timerViewModel)
        }
    }
}
