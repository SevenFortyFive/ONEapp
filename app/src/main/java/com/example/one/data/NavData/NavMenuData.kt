package com.example.one.data.NavData

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationMenuScreen (
    val route: String,
    val icon: ImageVector,
    val title: String
){
    data object MainPage : NavigationMenuScreen("MainPage", Icons.Rounded.Home,"主页")
    data object CharRiverPage : NavigationMenuScreen("CharRiverPage", Icons.Rounded.MoreVert,"词牌")
    data object OthersPage : NavigationMenuScreen("OthersPage", Icons.Rounded.Menu,"用户")
    data object AnalysePage : NavigationMenuScreen("AnalysePage",Icons.Rounded.DateRange,"分析")
    data object DiaryPage : NavigationMenuScreen("DiaryPage",Icons.Rounded.Create,"小记")
    data object SettingPage: NavigationMenuScreen("SettingPage",Icons.Rounded.Settings,"设置")
}