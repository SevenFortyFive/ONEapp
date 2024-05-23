package com.example.one.myui.UtilsUi

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.example.one.helper.getCurrentHour

/**
 * @since 2024/5/18
 */
@SuppressLint("UnrememberedAnimatable")
@Composable
fun MyChart(
    data: List<Pair<Float,Float>>?,
    target:Float,
    label: String,
    modifier: Modifier,
) {
    var previousSum by remember {
        mutableFloatStateOf(0f)
    }
    val sumLineAnimatable = remember {
        Animatable(previousSum)
    }
    val pointAlphaAnimatable = remember {
        Animatable(0f)
    }

    val sum = calculateSumWithData(data)
    // 计算sum目标值
    val sumValue: Float = if( sum  > target) {
        target - 0.5f
    } else {
        sum
    }

    LaunchedEffect(data) {
        sumLineAnimatable.animateTo(
            targetValue = sumValue,
            animationSpec = tween(durationMillis = 1000), // 设置动画持续时间
        )
        previousSum = sum
    }

    LaunchedEffect(data?.size) {
        pointAlphaAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .then(modifier)) {
        val myTextSize = 36f
        val myTextLength = myTextSize * (target.toString().length + label.length) / 2
        val startY1 = this.size.height / 5
        val startY2 = this.size.height * 2 / 5
        val startY3 = this.size.height * 3 / 5
        val startY4 = this.size.height * 4 / 5
        val endX = this.size.width - 10f

        val textY = startY4 + myTextSize
        val text0X = 0f
        val text6f = this.size.width / 4
        val text12f = this.size.width / 2
        val text24f = this.size.width * 3 / 4

        // 创建并配置 Paint 对象
        val paint = Paint().apply {
            color = Color.Black.toArgb()
            textSize = myTextSize // 设置字体大小
        }
        val pointPaint = Paint().apply {
            color = Color.Gray.toArgb()
            strokeWidth = 10f
            alpha = (pointAlphaAnimatable.value * 255).toInt()
        }
        // 虚线1
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        // 虚线2
        val pathEffect2 = PathEffect.dashPathEffect(floatArrayOf(10f,5f),0f)

        // 第一条水平线
        drawLine(
            color = Color.Black,
            start = Offset(myTextLength, startY1),
            end = Offset(endX, startY1),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )
        // 第二条水平线
        drawLine(
            color = Color.Black,
            start = Offset(myTextLength, startY2),
            end = Offset(endX, startY2),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )
        // 第三条水平线
        drawLine(
            color = Color.Black,
            start = Offset(myTextLength, startY3),
            end = Offset(endX, startY3),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )
        // 第四条水平线
        drawLine(
            color = Color.Black,
            start = Offset(10f, startY4),
            end = Offset(endX, startY4),
            strokeWidth = 4f,
            pathEffect = pathEffect2
        )

        val timeLineX =  this.size.width * getCurrentHour() / 24 + myTextSize
        drawLine(
            color = Color.Black,
            start = Offset(timeLineX, startY4+10f),
            end = Offset(timeLineX, startY1 - 50f),
            strokeWidth = 4f,
            pathEffect = pathEffect2
        )

        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawText("%.1f".format(target /4.0)+label, 0f,startY3 + 10, paint)
            canvas.nativeCanvas.drawText("%.1f".format(target /2.0) + label, 0f, startY2 + 10, paint)
            canvas.nativeCanvas.drawText("%.1f".format(target *3.0 / 4) + label, 0f, startY1 + 10, paint)
            canvas.nativeCanvas.drawText("0:00", text0X, textY, paint)
            canvas.nativeCanvas.drawText("6:00", text6f, textY, paint)
            canvas.nativeCanvas.drawText("12:00", text12f, textY, paint)
            canvas.nativeCanvas.drawText("18:00", text24f, textY, paint)

            if (data != null) {
                if (data.isNotEmpty()) {
                    data.forEach {
                        val x = this.size.width * it.first / 24 + myTextSize
                        val y = (1.0 - it.second / target) * (4.0 / 5.0 * this.size.height)
                        if (x >= 0 && x <= this.size.width && y >= 0 && y <= this.size.height) {
                            canvas.nativeCanvas.drawCircle(x, y.toFloat(), 10f, pointPaint)
                        }
                    }
                }
            }

            var sumLineY = this.size.height * (1.0 - sumLineAnimatable.value / target) * (4.0 / 5.0)
            if(sumLineY < 10f)
            {
                sumLineY = 0.0
            }

            drawLine(
                color = Color.Red,
                start = Offset(10f, sumLineY.toFloat()),
                end = Offset(endX, sumLineY.toFloat()),
                strokeWidth = 4f,
                pathEffect = pathEffect2
            )
        }
    }
}

/**
 * 计算最终值
 */
fun calculateSumWithData(data: List<Pair<Float, Float>>?): Float {
    var sum = 0f
    data?.forEach{
        sum += it.second
    }
    return sum
}

/**
 * @since 2024/5/18
 * 输入日，数量，value，绘制某一个月的数据
 */
@Composable
fun VerticalCart(data:Triple<Int,Int,Float>,modifier: Modifier = Modifier){
    Canvas(modifier = Modifier
        .fillMaxSize()
        .then(modifier)) {
        val width = this.size.width
        val height = this.size.height
        val textSize = 36f
        val beginX = 10f
        val line1X1 = beginX
        val line1Y1 = beginX
        val line1X2 = beginX
        val line1Y2 = width * 4 / 5

        drawLine(
            color = Color.Black,
            start = Offset(line1X1,line1Y1),
            end = Offset(line1X2,line1Y2)

        )
    }
}
