package com.example.one.myui.UtilsUi

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.one.helper.TimeFormatUtils
import com.example.one.setting.Setting
import com.example.one.vm.TimerViewModel
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyTimer(tiemrViewModel: TimerViewModel, modifier: Modifier = Modifier) {

    Card (
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)
    ){
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            CompletedText(tiemrViewModel)
            TimeLeftText(tiemrViewModel)
            ProgressCircle(tiemrViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            EditText(tiemrViewModel)
            Spacer(modifier = Modifier.padding(2.dp))
            Row {
                StartButton(tiemrViewModel)
                StopButton(tiemrViewModel)
            }
        }
    }
}

@Composable
private fun TimeLeftText(viewModel: TimerViewModel) {
    Text(
        text = TimeFormatUtils.formatTime(viewModel.timeLeft),
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
private fun EditText(viewModel: TimerViewModel) {
    Box(
        modifier = Modifier
            .size(150.dp, 50.dp),
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.status.showEditText()) {
            TextField(
                modifier = Modifier
                    .size(100.dp, 100.dp),
                value = if (viewModel.totalTime == 0L) "" else viewModel.totalTime.toString(),
                onValueChange = viewModel::updateValue,
                label = {  },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
private fun StartButton(viewModel: TimerViewModel) {
    ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .padding(2.dp),
        enabled = viewModel.totalTime > 0,
        onClick = viewModel.status::clickStartButton
    ) {
        Text(text = viewModel.status.startButtonDisplayString())
    }
}

@Composable
private fun StopButton(viewModel: TimerViewModel) {
    ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .padding(2.dp),
        enabled = viewModel.status.stopButtonEnabled(),
        onClick = viewModel.status::clickStopButton
    ) {
        Text(text = "Stop")
    }
}

@Composable
fun ProgressCircle(viewModel: TimerViewModel,modifier: Modifier = Modifier){
    val sweepAngle = viewModel.status.progressSweepAngle()
    Box(modifier = modifier,
        contentAlignment = Alignment.Center){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val CoreX = size.width / 2
            val CoreY = size.height / 2
            drawCircle(
                center = Offset(
                    CoreX,
                    CoreY),
                color = Color.Gray,
                radius = (min(size.width,size.height) * 0.5 * 0.9).toFloat(),
                style = Stroke(
                    width = minOf(size.width,size.height)*0.06.toFloat(),
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(1.dp.toPx(), 6.dp.toPx())
                    )
                )
            )
            val angle = (sweepAngle) / 180 * Math.PI
            val pointerLength = minOf(size.width,size.height) * 0.5 * 0.8
            drawLine(
                color = Color.Gray,
                start = Offset(
                    CoreX,
                    CoreY),
                end = Offset(
                    (CoreX + pointerLength * sin(angle)).toFloat(),
                    (CoreY -pointerLength * cos(angle)).toFloat()),
                strokeWidth = minOf(size.width,size.height)*0.01.toFloat()
            )
            drawCircle(
                center = Offset(CoreX,CoreY),
                color = Color.Gray,
                radius = minOf(size.width,size.height)*0.02.toFloat()
            )
            drawCircle(
                center = Offset(CoreX,CoreY),
                color = Color.LightGray,
                radius = minOf(size.width,size.height)*0.01.toFloat()
            )
        }
    }

}
