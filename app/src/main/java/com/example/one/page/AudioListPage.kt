package com.example.one.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.one.myui.AudioUi.AudioList

@Composable
fun AudioListPage()
{
    Column(modifier = Modifier.fillMaxSize()) {
        AudioList()
    }
}