package com.example.one.nav

import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.one.data.NavData.NavigationMenuScreen
import com.example.one.page.MainPage
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.one.helper.vibrate
import com.example.one.myui.UtilsUi.AlertDialogExample
import com.example.one.myui.HotMap.ComprehensiveHotMap
import com.example.one.myui.AudioUi.Player
import com.example.one.page.AnalysePage
import com.example.one.page.AudioListPage
import com.example.one.page.CharRiverPage
import com.example.one.page.ChatWithMePage
import com.example.one.page.DetailPage
import com.example.one.page.DiaryPage
import com.example.one.page.PermissionPage
import com.example.one.page.SettingPage
import com.example.one.vm.AppViewModel
import com.example.one.vm.HotMapViewModel
import com.example.one.vm.MyHotMapViewModelFactory
import kotlinx.coroutines.launch

/**
 * 侧边栏与底部栏集成navigation
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController)
{
    val hotMapViewModel: HotMapViewModel = viewModel(
        LocalContext.current as ComponentActivity
        ,factory = MyHotMapViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    // 观察app繁忙状态
    val appViewModel:AppViewModel = AppViewModel.getInstance()
    val ifBusy by appViewModel.ifHasTask
    val showBusyDialog = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val menuItems = listOf(
        NavigationMenuScreen.MainPage,
        NavigationMenuScreen.CharRiverPage,
        NavigationMenuScreen.OthersPage,
        NavigationMenuScreen.AnalysePage,
        NavigationMenuScreen.DiaryPage,
        NavigationMenuScreen.SettingPage
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        // 打开时启用手势
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            BackHandler(drawerState.isOpen) {
                scope.launch { drawerState.close() }
            }
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                menuItems.forEach{
                    NavigationDrawerItem(label = {
                        Text(text = it.title)
                    },
                        selected = currentRoute == it.route,
                        onClick = {
                            vibrate(context,100)
                            if(ifBusy){
                                showBusyDialog.value = true
                            }
                            else if(currentRoute != it.route)
                            {
                                navController.navigate(it.route)
                            }
                        },
                        icon = {
                            Icon(imageVector = it.icon,
                                contentDescription = it.title
                            )
                        }
                    )
                }
                Player()
                ComprehensiveHotMap(vm = hotMapViewModel)
                Spacer(modifier = Modifier.height(50.dp))
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter)
                {
                    Text(text = "yooo_fan ONEApp 北京理工大学 v1.0", fontSize = 10.sp)
                }
            }
        },
    ) {
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    actions = {
                        menuItems.forEach{
                            IconButton(onClick = {
                                vibrate(context,100)
                                if(ifBusy)
                                {
                                    showBusyDialog.value = true
                                }
                                else if(currentRoute != it.route)
                                {
                                    navController.navigate(it.route)
                                } },
                                colors =
                                if(it.route == currentRoute)
                                    IconButtonDefaults.iconButtonColors()
                                else
                                    IconButtonDefaults.iconButtonColors()
                            ) {
                                Icon(it.icon, contentDescription = "")
                            }
                        } },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            } },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.Menu, "Localized description")
                        }
                    }
                )
            }
        ) { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                Navigation(navController = navController)
            }
        }
    }
    if(showBusyDialog.value)
    {
        AlertDialogExample(
            onDismissRequest = {
                showBusyDialog.value = false
            },
            onConfirmation = {
                showBusyDialog.value = false
            },
            dialogTitle = "no！",
            dialogText = "您有一个任务正在进行，请先停止任务吧~",
            icon = Icons.Rounded.Warning
        )
    }
}
/**
 * 路由表
 */
@Composable
fun Navigation(navController: NavHostController)
{
    NavHost(navController = navController, startDestination = "MainPage" )
    {
        composable("MainPage"){
            MainPage()
        }
        composable("CharRiverPage")
        {
            CharRiverPage()
        }
        composable("OthersPage")
        {
            AudioListPage()
        }
        composable("AnalysePage")
        {
            AnalysePage()
        }
        composable("DiaryPage")
        {
            DiaryPage()
        }
        composable("SettingPage")
        {
            SettingPage(navController)
        }
        composable("DetailPage")
        {
            DetailPage(navController)
        }
        composable("PermissionPage")
        {
            PermissionPage(navController)
        }
        composable("ChatWithMePage")
        {
            ChatWithMePage(navController)
        }
    }
}