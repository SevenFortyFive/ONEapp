package com.example.one.page

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.one.helper.Toaster
import com.example.one.setting.Setting

const val MY_EMAIL_ADDRESS = "yooo_fan@163.com"
const val MY_GITHUB_URL = "https://github.com/SevenFortyFive/ONEapp.git"
const val MY_WECHAT = "Yooo_Fan"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatWithMePage(navController: NavHostController) {
    val context = LocalContext.current
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
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "通过邮件联系我", fontSize = 25.sp)
                    ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp
                        ),onClick = {
                            val mailIntent = Intent(Intent.ACTION_SENDTO)
                            mailIntent.data = android.net.Uri.parse("mailto:${MY_EMAIL_ADDRESS}")
                            context.startActivity(mailIntent) }) {
                        Text(text = "跳转")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "GitHub")
                    ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp
                        ),onClick = {
                            val gitHubIntent = Intent(Intent.ACTION_VIEW)
                            gitHubIntent.data = android.net.Uri.parse(MY_GITHUB_URL)
                            context.startActivity(gitHubIntent)
                        }) {
                        Text(text = "跳转")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "微信")
                    ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp
                        ),onClick = {
                            val clipboardManager = context.getSystemService(ClipboardManager::class.java)
                            val clipData = ClipData.newPlainText("wechat", MY_WECHAT)
                            clipboardManager.setPrimaryClip(clipData)
                            // 提示信息，可选
                            Toaster.showShortToaster(context,"微信已复制到剪切板")
                            Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                        }) {
                        Text(text = "获取")
                    }
                }

            }

        }
    }
}