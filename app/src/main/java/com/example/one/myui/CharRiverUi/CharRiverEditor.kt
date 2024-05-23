package com.example.one.myui.CharRiverUi

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.one.data.CharRiverData.getCharRiverPrecisionDataList
import com.example.one.myui.UtilsUi.LoadingIndicator
import com.example.one.setting.Setting

@Composable
fun CharRiverEditor(
    modifier: Modifier,
    onLoading: State<Boolean?>,
    start: () -> Unit,
    stop: () -> Unit,
    setData: (String, Int) -> Unit
){
    val text = remember {
        mutableStateOf("")
    }

    val precision = remember {
        mutableIntStateOf(30)
    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
            Card( modifier = modifier
                .size(400.dp, 300.dp)
                .animateContentSize()
                .padding(10.dp),
                elevation =  CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
                shape = RoundedCornerShape(Setting.WholeElevation)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(32.dp))
                {
                    LoadingIndicator(loading = onLoading)
                }
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()) {
                    InputText(text = text,precision)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        ElevatedButton(onClick = { start() }) {
                            Text(text = "开始")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        ElevatedButton(onClick = {
                            if(text.value != "")
                            {
                                setData(text.value,precision.intValue)
                                text.value = ""
                                precision.intValue = 30
                            }
                        }
                        ) {
                            Text(text = "设置字符")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        ElevatedButton(onClick = {stop()}) {
                            Text(text = "结束")
                        }
                    }
                }
        }
    }
}

@Composable
fun InputText(text: MutableState<String>, precision: MutableIntState)
{
    TextField(value = text.value,
        onValueChange = {
        text.value = it
        },
        label = {
            Text(text = "输入字符")
        },
        placeholder = {
            Text(text = "在此处输入")
        },
        modifier = Modifier
            .padding(2.dp)
            .width(200.dp),
        maxLines = 1,
        singleLine = true,
        trailingIcon = { Icon(Icons.Filled.Settings, contentDescription = "Localized description") }
    )
    Spacer(modifier = Modifier.height(10.dp))
    PrecisionInput(precision)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrecisionInput(precision: MutableIntState) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = getCharRiverPrecisionDataList()
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, data ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    selectedIndex = index
                    precision.intValue = data.precision },
                selected = index == selectedIndex
            ) {
                Text(text = data.label)
            }
        }
    }
}