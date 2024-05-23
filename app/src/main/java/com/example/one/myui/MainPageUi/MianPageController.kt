package com.example.one.myui.MainPageUi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.one.data.MainPageState
import com.example.one.myui.UtilsUi.AlertDialogExample
import com.example.one.setting.Setting
import com.example.one.vm.AppViewModel

@Composable
fun MainPageController(
    changeType: (MainPageState) -> Unit,
    modifier: Modifier
){
    val showAlertDialog = remember {
        mutableStateOf(false)
    }
    val appViewModel = AppViewModel.getInstance()
    val ifBusy = appViewModel.ifHasTask

    if(showAlertDialog.value)
    {
        AlertDialogExample(
            onDismissRequest = {
                showAlertDialog.value = false
            },
            onConfirmation = { showAlertDialog.value = false},
            dialogTitle = "繁忙",
            dialogText =  "繁忙",
            icon = Icons.Rounded.Info
        )
    }
    Row(modifier = modifier
        .fillMaxSize()
        .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically) {
        ElevatedButton(onClick = {
            if(ifBusy.value)
            {
                showAlertDialog.value = true
            }else{
                changeType(MainPageState.BREATH)
            }
        },
            shape = RoundedCornerShape(Setting.WholeElevation),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp
            )) {
            Text(text = "呼吸")
        }
        Spacer(modifier = Modifier.width(10.dp))
        ElevatedButton(onClick = {
            if(ifBusy.value)
            {
                showAlertDialog.value = true
            }else {
                changeType(MainPageState.MEDITATION)
            }},
            shape = RoundedCornerShape(Setting.WholeElevation),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp
            )) {
            Text(text = "冥想")
        }
        Spacer(modifier = Modifier.width(10.dp))
        ElevatedButton(onClick = {
            if(ifBusy.value)
            {
                showAlertDialog.value = true
            }else{ changeType(MainPageState.CLOCK)}},
            shape = RoundedCornerShape(Setting.WholeElevation),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp
            )) {
            Text(text = "专注")
        }
        Spacer(modifier = Modifier.width(10.dp))
        ElevatedButton(onClick = {
            if(ifBusy.value)
            {
                showAlertDialog.value = true
            }else{ changeType(MainPageState.DRINK)}},
            shape = RoundedCornerShape(Setting.WholeElevation),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp
            )) {
            Text(text = "饮水")
        }
    }
}