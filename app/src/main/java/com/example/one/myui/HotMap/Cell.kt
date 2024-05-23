package com.example.one.myui.HotMap

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.one.helper.getColorForValue
import com.example.one.setting.Setting

@Composable
fun Cell(value: Int, isToday:Boolean? = false)
{
    val color =  getColorForValue(value)
    val modifier = if(isToday == true)
    {
        Modifier
            .size(Setting.CellSize, Setting.CellSize)
            .clip(RoundedCornerShape(Setting.CellCornerSize))
            .border(BorderStroke(width = 1.dp, color = Color.Green))
            .background(color)
    }
    else{
        Modifier
            .size(Setting.CellSize, Setting.CellSize)
            .clip(RoundedCornerShape(Setting.CellCornerSize))
            .background(color)
    }
    Box(
        modifier
    )
}

@Composable
fun EmptyCell(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .size(Setting.CellSize, Setting.CellSize)
            .clip(RoundedCornerShape(Setting.CellCornerSize))
    )
}

@Composable
fun TextCell(string: String){
    Box(
        modifier = Modifier
            .size(Setting.CellSize, Setting.CellSize)
            .clip(RoundedCornerShape(Setting.CellCornerSize)),
        contentAlignment = Alignment.Center
    ){
        Text(text = string,
            textAlign = TextAlign.Center,
            fontSize = Setting.CellTextSize)
    }
}

@Composable
fun ComprehensiveHotMapCell(value: Int, isToday:Boolean? = false){
    val color =  getColorForValue(value)
    val modifier = if(isToday == true)
    {
        Modifier
            .size(Setting.ComprehensiveHotMapCellSize, Setting.ComprehensiveHotMapCellSize)
            .clip(RoundedCornerShape(Setting.ComprehensiveCoronerSize))
            .border(BorderStroke(width = 1.dp, color = Color.Green))
            .background(color)
    }
    else{
        Modifier
            .size(Setting.ComprehensiveHotMapCellSize, Setting.ComprehensiveHotMapCellSize)
            .clip(RoundedCornerShape(Setting.ComprehensiveCoronerSize))
            .background(color)
    }
    Box(
        modifier
    )
}
@Composable
fun ComprehensiveEmptyCell(modifier:Modifier = Modifier){
    Box(
        modifier = modifier
            .size(Setting.ComprehensiveHotMapCellSize, Setting.ComprehensiveHotMapCellSize)
            .clip(RoundedCornerShape(Setting.ComprehensiveCoronerSize))
    )
}

@Composable
fun ComprehensiveTextCell(string:String){
    Box(
        modifier = Modifier
            .size(Setting.ComprehensiveHotMapCellSize, Setting.ComprehensiveHotMapCellSize)
            .clip(RoundedCornerShape(Setting.ComprehensiveCoronerSize)),
        contentAlignment = Alignment.Center
    ){
        Text(text = string,
            textAlign = TextAlign.Center,
            fontSize = Setting.ComprehensiveTextSize)
    }
}