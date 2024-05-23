package com.example.one.myui.AudioUi

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.one.R
import com.example.one.data.SharedPreferences.BALANCE
import com.example.one.data.SharedPreferences.MySharedPreference
import com.example.one.data.SharedPreferences.SharedPreferencesHelper
import com.example.one.data.StoreData.ExtAudioData
import com.example.one.data.StoreData.getExtAudioList
import com.example.one.helper.ifBought
import com.example.one.helper.unlockAudio
import com.example.one.myui.UtilsUi.AlertDialogExample
import com.example.one.setting.Setting
import com.example.one.vm.StoreViewModel

@Composable
fun AudioStoreUi(modifier: Modifier = Modifier){

    val vm:StoreViewModel = viewModel()
    val balance = vm.balance.observeAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)) {
        Column(modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            StoreHeader(balance = balance)
            Spacer(modifier = Modifier.height(10.dp))
            StoreBody(vm::onBalanceChange)
        }
    }
}

@Composable
fun StoreBody(onChangeBalance: () -> Unit) {
    LazyColumn {
        items(getExtAudioList())
        {
            Item(it,onChangeBalance)
        }
    }
}

@Composable
fun Item(data: ExtAudioData, onChangeBalance: () -> Unit){
    val showEnoughDialog = remember {
        mutableStateOf(false)
    }
    val showNotEnoughDialog = remember {
        mutableStateOf(false)
    }
    // 余额不足对话框
    when{
        showNotEnoughDialog.value ->
            AlertDialogExample(
                onDismissRequest = {showNotEnoughDialog.value = false },
                onConfirmation = {
                    showNotEnoughDialog.value = false
                },
                dialogTitle = "购买失败",
                dialogText = "您的余额"+MySharedPreference.getInt(BALANCE)+"不足",
                icon = Icons.Default.ShoppingCart
            )
    }
    // 确认购买对话框
    when{
        showEnoughDialog.value ->
            AlertDialogExample(
                onDismissRequest = {showEnoughDialog.value = false },
                onConfirmation = {
                    showEnoughDialog.value = false
                    // 解锁播放
                    unlockAudio(data)
                    // 余额处理
                    SharedPreferencesHelper.sub(BALANCE,data.cost)
                    // 通知商店余额变化
                    onChangeBalance()
                },
                dialogTitle = "确认购买",
                dialogText = "确认购买"+data.name,
                icon = Icons.Default.ShoppingCart
            )
    }
    Card( modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .animateContentSize()
        .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically) {
            CDInStore(data.surface,Modifier)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.width(120.dp)) {
                Text(text = data.name)
                Spacer(modifier = Modifier.padding(1.dp))
                Text(text = data.cost.toString()+"元")
            }
            Box(contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth())
            {
                ElevatedButton(shape = RoundedCornerShape(Setting.WholeElevation),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp
                    ),
                    onClick = {
                        Log.d("store","${MySharedPreference.getInt(BALANCE)} ${data.cost}")
                        if(MySharedPreference.getInt(BALANCE) >= data.cost && !ifBought(data)){
                            // 激活确认购买对话框
                            showEnoughDialog.value = true
                        }else{
                            // 激活余额不足对话框
                            showNotEnoughDialog.value = true
                        }
                              },
                    modifier = Modifier.padding(20.dp),
                    enabled = !ifBought(data)
                ) {
                    Text(text ="购买")
                }
            }
        }
    }
}

@Composable
fun StoreHeader(balance: State<Int?>) {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround) {
        Text(text = "余额:"+balance.value.toString())
    }
}

@SuppressLint("ResourceType")
@Composable
fun CDInStore(imgID: Int, modifier: Modifier) {
    Box(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        //歌曲封面
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(imgID).apply(block = fun ImageRequest.Builder.() {
                    transformations(CircleCropTransformation())
                }).build()
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .matchParentSize()
                .aspectRatio(1.0f)
                .padding(20.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.bet),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .matchParentSize()
                .aspectRatio(1.0f)
                .padding(10.dp),
        )
    }
}
