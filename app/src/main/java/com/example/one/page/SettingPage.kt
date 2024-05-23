package com.example.one.page

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.one.data.SharedPreferences.BREATH_ONE_DAY
import com.example.one.data.SharedPreferences.CLOCK_ONE_DAY
import com.example.one.data.SharedPreferences.DRINK_ONE_DAY
import com.example.one.data.SharedPreferences.MEDITATION_ONE_DAY
import com.example.one.helper.Toaster
import com.example.one.helper.vibrate
import com.example.one.myui.HotMap.SumTime
import com.example.one.setting.Setting
import com.example.one.vm.AppViewModel
import com.example.one.vm.DataAnalyseViewModel
import com.example.one.vm.HotMapViewModel
import com.example.one.vm.MyDataAnalyseViewModelFactory
import com.example.one.vm.MyHotMapViewModelFactory
import com.example.one.vm.NoticeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingPage(navController: NavHostController) {
    val appViewModel = AppViewModel.getInstance()
    val noticeViewModel:NoticeViewModel = viewModel(LocalContext.current as ComponentActivity)
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 在回调函数中设置
            noticeViewModel.autoSetNoticeSchedule(context)
        } else {
            // 提示设置失败
            appViewModel.setNotHasNoticeSchedule()
            Toaster.showShortToaster(context,"没有获取到权限，不能发送")
        }
    }

    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val hour = remember {
        mutableIntStateOf(noticeViewModel.hour)
    }
    val minute = remember {
        mutableIntStateOf(noticeViewModel.minute)
    }

    LaunchedEffect(hour.intValue, minute.intValue) {
        // 保存数据到viewModel
        noticeViewModel.setTime(hour.intValue, minute.intValue)
    }
    Scaffold {paddingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues))
        {
            SettingUi(navController,showBottomSheet)
        }

        // 用于设置noticeTime的sheet
        if (showBottomSheet.value) {
            ModalBottomSheet(
                modifier = Modifier,
                onDismissRequest = {
                    showBottomSheet.value = false
                },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center)
                {
                    Text(text = "设置任务提醒时间", fontSize = 30.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    TimePickerSetting(hour, minute)
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically) {
                        ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp
                            ),onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet.value = false
                                }
                            }
                        }) {
                            Text(text = "取消")
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp
                            ),onClick = {
                            // 检查权限
                            if(ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                )!= PackageManager.PERMISSION_GRANTED)
                            {
                                // 不存在权限申请权限
                                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }else{
                                // 存在权限，直接设置
                                noticeViewModel.autoSetNoticeSchedule(context)
                            }
                        }) {
                            Text(text = "保存")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingUi(navController: NavHostController, showBottomSheet: MutableState<Boolean>) {
    LazyColumn {
        item {
            Column(modifier = Modifier){
                Text(text = "设置", fontSize = 35.sp)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(10.dp),
                    elevation =  CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(Setting.WholeElevation)
                ) {
                    SumTime()
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(10.dp),
                    elevation =  CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(Setting.WholeElevation)
                ) {
                    SettingOne(modifier = Modifier,showBottomSheet)
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(10.dp),
                    elevation =  CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(Setting.WholeElevation)) {
                    SettingTwo()
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(10.dp),
                    elevation =  CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    shape = RoundedCornerShape(Setting.WholeElevation)) {
                    SettingThree()
                }
               Card(
                   modifier = Modifier
                       .fillMaxWidth()
                       .animateContentSize()
                       .padding(10.dp),
                   elevation =  CardDefaults.cardElevation(
                       defaultElevation = 2.dp
                   ),
                   shape = RoundedCornerShape(Setting.WholeElevation)) {
                   SettingFour(navController,modifier = Modifier)
               }
            }
        }
    }
}

@Composable
fun SettingOne(modifier: Modifier = Modifier, showBottomSheet: MutableState<Boolean>){
    val dataAnalyseViewModel:DataAnalyseViewModel = viewModel(LocalContext.current as ComponentActivity)
    val drinkOneDay = dataAnalyseViewModel.drinkOneDay.observeAsState()
    val clockOneDay = dataAnalyseViewModel.clockOneDay.observeAsState()
    val meditationOneDay = dataAnalyseViewModel.meditationOneDay.observeAsState()
    val breathOneDay = dataAnalyseViewModel.breathOneDay.observeAsState()

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .then(modifier)) {
        Text(text = "程序设置", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        HowMachItem(
            title = "每日呼吸任务",
            label = "分钟",
            8f,
            120f,
            1,
            breathOneDay,
            dataAnalyseViewModel::setOneDay, BREATH_ONE_DAY)
        HowMachItem(
            title = "每日专注任务",
            label = "分钟",
            8f,
            368f,
            15,
            clockOneDay,
            dataAnalyseViewModel::setOneDay, CLOCK_ONE_DAY
        )
        HowMachItem(
            title = "每日冥想任务",
            label = "分钟",
            8f,
            120f,
            1,
            meditationOneDay,
            dataAnalyseViewModel::setOneDay, MEDITATION_ONE_DAY
        )
        HowMachItem(
            title = "每日饮水任务",
            label = "升",
            1f,
            6f,
            1,
            drinkOneDay,
            dataAnalyseViewModel::setOneDay, DRINK_ONE_DAY
        )
        GetItem(title = "提醒设置",Icons.Rounded.Notifications){
            showBottomSheet.value = true
        }
    }
}
@Composable
fun SettingTwo(modifier: Modifier = Modifier){
    val noticeViewModel:NoticeViewModel = viewModel(LocalContext.current as ComponentActivity)
    val appViewModel = AppViewModel.getInstance()
    val ifDark = appViewModel.ifDark
    val ifCanVibrate = appViewModel.ifCanVibrate
    val ifNotice = appViewModel.ifHasNoticeSchedule
    val ifFollowSystem = appViewModel.ifFollowSystem
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 在回调函数中设置
            noticeViewModel.autoSetNoticeSchedule(context)
        } else {
            // 提示设置失败
            appViewModel.setNotHasNoticeSchedule()
            Toaster.showShortToaster(context,"没有获取到权限，不能发送")
        }
    }

    // 如果提醒设置变更
    LaunchedEffect(ifNotice.value) {
        if (ifNotice.value)
        {
            // 检查权限
            if(ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                )!= PackageManager.PERMISSION_GRANTED)
            {
                // 不存在权限申请权限
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }else{
                // 存在权限，直接设置
                noticeViewModel.autoSetNoticeSchedule(context)
            }
        }else{
            noticeViewModel.cancelNoticeSchedule(context)
        }
    }
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .then(modifier)){
        Text(text = "通用", fontSize = 20.sp)
        YesOrNo(title = "每日提醒", state = ifNotice,appViewModel::setHasNoticeSchedule,appViewModel::setNotHasNoticeSchedule)
        YesOrNo(title = "跟随系统", state = ifFollowSystem, yes = appViewModel::setFollowSystem,no = appViewModel::setNotFollowSystem)
        YesOrNo(title = "暗黑模式",state = ifDark,appViewModel::setDark,appViewModel::setLight)
        YesOrNo("震动", state = ifCanVibrate,appViewModel::setCanVibrate,appViewModel::setCannotVibrate)
    }
}

@Composable
fun SettingThree(modifier: Modifier = Modifier){
    val hotMapViewModel: HotMapViewModel = viewModel(
        LocalContext.current as ComponentActivity
        ,factory = MyHotMapViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val dataAnalyseViewModel:DataAnalyseViewModel = viewModel(
        LocalContext.current as ComponentActivity,
        factory = MyDataAnalyseViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .then(modifier)) {
        Text(text = "资料", fontSize = 20.sp)
        GetItem(title = "热图资料导出", Icons.Rounded.Share) { hotMapViewModel.shareDetailHotMapMessage(context) }
        GetItem(title = "详细资料导出", Icons.Rounded.Share){ dataAnalyseViewModel.shareMessage(context)}
    }
}

@Composable
fun SettingFour(navController: NavHostController, modifier: Modifier = Modifier){
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(10.dp)
        .then(modifier)){
        Text(text = "STEADY", fontSize = 20.sp)
        GetItem(title = "呼吸信息", icon = Icons.Rounded.Info) {
            navController.navigate("DetailPage")
        }
        GetItem(title = "反馈和建议", icon = Icons.Rounded.Email) {
            navController.navigate("ChatWithMePage")
        }
        GetItem(title = "应用权限", icon = Icons.Rounded.Info) {
            navController.navigate("PermissionPage")
        }
    }
}

@Composable
fun YesOrNo(title: String, state: State<Boolean>, yes:()->Unit, no:()->Unit){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, fontSize = 20.sp)
        SettingSwitchMinimal(state,yes,no)
    }
}

@Composable
fun GetItem(title: String, icon: ImageVector, function: () -> Unit){
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, fontSize = 20.sp)
        IconButton(onClick = { function() }) {
            Icon(imageVector = icon, contentDescription = "setting")
        }
    }
}

/**
 * @since 2024/5/19
 * 进度条设置每日任务
 */
@Composable
fun HowMachItem(
    title: String,
    label: String = "",
    from: Float,
    to: Float,
    oneStep:Int,
    drinkOneDay: State<Int?>,
    setValue: (String, Int) -> Unit,
    key: String
){
    val context = LocalContext.current
    if(drinkOneDay.value == null)
    {
        return
    }
    Column {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom) {
            Text(text = "$title:", fontSize = 20.sp)
            Text(text = drinkOneDay.value!!.toInt().toString()+label, fontSize = 15.sp)
        }
        Slider(
            value = drinkOneDay.value!!.toFloat(),
            onValueChange = {
                vibrate(context,1)
                setValue(key,it.toInt()) },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = (to.toInt() - from.toInt() - 1)/oneStep,
            valueRange = from..to
        )
    }
}

@Composable
fun SettingSwitchMinimal(state: State<Boolean>, yes: () -> Unit, no: () -> Unit) {
    val s = remember {
        mutableStateOf(state.value)
    }
    Switch(
        checked = s.value,
        onCheckedChange = {
            s.value = it
            if(it)
            {
                yes()
            }else{
                no()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerSetting(hour: MutableIntState, min: MutableIntState) {
    val timeState = rememberTimePickerState(hour.intValue, min.intValue)
    LaunchedEffect(timeState.hour ,timeState.minute,timeState.is24hour) {
        hour.intValue = timeState.hour
        min.intValue = timeState.minute
    }
    TimePicker(state = timeState)
}

