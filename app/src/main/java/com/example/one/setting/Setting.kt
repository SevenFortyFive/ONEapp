package com.example.one.setting

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.example.one.helper.LocalDpHelper

object Setting{
    // HotMap中cell padding
    val CellPadding = 3.dp
    // HotMap中cell大小
    val CellSize = 15.dp
    // HotMap中月份字体大小
    val CellTextSize = 7.sp
    // CellCorner
    val CellCornerSize = 2.dp

    // 综合HotMap中Cell大小
    val ComprehensiveHotMapCellSize = 24.dp
    // padding大小
    val ComprehensiveHotMapCellPadding = 4.dp
    // 字体大小
    val ComprehensiveTextSize = 14.sp
    // 转角大小
    val ComprehensiveCoronerSize = 4.dp

    // CharRiver的最小大小
    const val CharRiverMinSize = 100

    // 保存整体的
    val WholeElevationLiveData: MutableLiveData<Dp> = MutableLiveData(2.dp)

    // 保存ui的圆角
    val WholeElevation = 10.dp

    // 商店项目间隔字体大小
    val StoreItemDividerFontSize = 20.sp

    // 分割线高度
    val StoreItemDividerHeight = 2.dp
}
