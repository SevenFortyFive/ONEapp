package com.example.one.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionPage(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "返回") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                })
        }
    ) {paddingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
            Column(modifier = Modifier.fillMaxSize().padding(15.dp)) {
                Text(text = "权限说明", fontSize = 35.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(text = "权限名称", modifier = Modifier.weight(1f))
                    Text(text = "权限功能", modifier = Modifier.weight(1f))
                    Text(text = "应用场景", modifier = Modifier.weight(1f))
                }
                Divider(thickness = 5.dp)
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(text = "android.permission.MODIFY_AUDIO_SETTINGS", modifier = Modifier.weight(1f))
                    Text(text = "允许应用程序修改全局音频设置，如音量和音频路由", modifier = Modifier.weight(1f))
                    Text(text = "调整媒体音量、电话音量或警报音量", modifier = Modifier.weight(1f))
                }
                Divider(thickness = 2.dp)
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(text = "android.permission.POST_NOTIFICATIONS", modifier = Modifier.weight(1f))
                    Text(text = "允许应用程序发布通知到设备的状态栏", modifier = Modifier.weight(1f))
                    Text(text = "定时通知等", modifier = Modifier.weight(1f))
                }
                Divider(thickness = 2.dp)
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(text = "android.permission.SCHEDULE_EXACT_ALARM", modifier = Modifier.weight(1f))
                    Text(text = "允许应用程序调度精确的闹钟，即使设备处于深度睡眠模式下也可以唤醒设备", modifier = Modifier.weight(1f))
                    Text(text = "定时通知时系统可以唤醒应用", modifier = Modifier.weight(1f))
                }
                Divider(thickness = 2.dp)
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(text = "android.permission.VIBRATE", modifier = Modifier.weight(1f))
                    Text(text = "允许应用程序控制设备的振动功能", modifier = Modifier.weight(1f))
                    Text(text = "震动效果", modifier = Modifier.weight(1f))
                }
                Divider(thickness = 2.dp)
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(text = "android.permission.WAKE_LOCK", modifier = Modifier.weight(1f))
                    Text(text = "允许应用程序防止设备进入睡眠模式，保持设备的唤醒状态", modifier = Modifier.weight(1f))
                    Text(text = "音频播放等，防止设备进入睡眠模式中断播放", modifier = Modifier.weight(1f))
                }
                Divider(thickness = 2.dp)
                Row(modifier = Modifier.fillMaxWidth()){
                    Text(text = "android.permission.ACCESS_NOTIFICATION_POLICY", modifier = Modifier.weight(1f))
                    Text(text = "管理通知的优先级，允许或者组织通知，通知过滤", modifier = Modifier.weight(1f))
                    Text(text = "用于设置通知channel，进行定时通知", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}