package com.example.one.myui.AudioUi

import android.app.Application
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.one.data.PlayerData.PlayerState
import com.example.one.data.PlayerData.getAudioDataList
import com.example.one.data.SQLite.entity.MyAudioData
import com.example.one.setting.Setting
import com.example.one.vm.MyAudioViewModelFactory
import com.example.one.vm.PlayerViewModel

@Composable
fun AudioList(){
    val vm: PlayerViewModel = viewModel(LocalContext.current as ComponentActivity
        ,factory = MyAudioViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val showStore = remember {
        mutableStateOf(false)
    }
    val dataList by vm.dataList.observeAsState()
    val currentData by vm.currentAudioData.observeAsState()
    val playerState by vm.currentState.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        AudioListHeader(currentData!!,Modifier.weight(1f),vm::changeItemLoveState,
            showStore)
        if(showStore.value)
        {
            AudioStoreUi(modifier = Modifier.weight(2f))
            AudioListBody(dataList!!, modifier = Modifier.weight(2f),vm::setAudioItemWithAudioData,vm::start)
        }else{
            AudioListBody(dataList!!, modifier = Modifier.weight(4f),vm::setAudioItemWithAudioData,vm::start)
        }
        AudioControllerInAudioList(modifier = Modifier.weight(1f),
            vm::nextAudio,vm::start,vm::pause,vm::preAudio,
            playerState)
    }
}

@Composable
fun AudioListHeader(
    data: MyAudioData,
    modifier: Modifier,
    changeLoveState: (MyAudioData) -> Unit,
    showStore: MutableState<Boolean>
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)) {
        Column(modifier = Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                MyAudioListItem(data,modifier = Modifier.size(100.dp))
                Row(modifier = Modifier.padding(15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        changeLoveState(data)
                    }) {
                        if(data.love)
                        {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Rounded.Favorite,
                                contentDescription = "Love Audio",
                                tint = Color.Red
                            )
                        }else{
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Rounded.FavoriteBorder,
                                contentDescription = "Love Audio"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                    
                    IconButton(onClick = { showStore.value = !showStore.value}) {
                        Icon(imageVector = Icons.Rounded.Menu, contentDescription = "Menu")
                    }
                }
            }
        }
    }

}

@Composable
fun AudioListBody(
    data: List<MyAudioData> = getAudioDataList(),
    modifier: Modifier,
    changeTo: (MyAudioData) -> Unit,
    start:()->Unit
){
    Card(        modifier = modifier
        .fillMaxWidth()
        .animateContentSize()
        .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)) {
        LazyColumn(modifier = Modifier
            .padding(10.dp)
            .then(modifier)) {
            items(data)
            {
                MyAudioListItemWithButton(data = it, modifier = Modifier.size(50.dp),changeTo,start)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun MyAudioListItemWithButton(
    data: MyAudioData,
    modifier: Modifier,
    changeTo: (MyAudioData) -> Unit,
    start: () -> Unit
){
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween){
        MyAudioListItem(data = data, modifier = modifier)
        // 支持点击切换目录的item
        IconButton(onClick = { changeTo(data)
            start()
        }) {
            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = "player this audio")
        }
    }
}

@Composable
fun MyAudioListItem(data:MyAudioData, modifier: Modifier){
    Row(verticalAlignment = Alignment.CenterVertically) {
        AudioSurfaceInAudioList(imgID = data.surfaceId,
            modifier = modifier)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = data.name)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = data.author)
        }
    }
}

@Composable
fun AudioControllerInAudioList(
    modifier: Modifier,
    next: () -> Unit,
    start: () -> Unit,
    pause: () -> Unit,
    pre: () -> Unit,
    playerState: PlayerState?
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically){
                IconButton(onClick = { pre() }) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "big fore",
                        modifier = Modifier.size(100.dp))
                }
                IconButton(onClick = {
                    if(playerState == PlayerState.PLAYING)
                    {
                        pause()
                    }else{
                        start()
                    }
                }) {
                    if(playerState == PlayerState.PLAYING)
                    {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = "big play",
                            modifier = Modifier.size(100.dp))
                    }else{
                        Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = "big play",
                            modifier = Modifier.size(100.dp))
                    }
                }
                IconButton(onClick = { next() }) {
                    Icon(imageVector = Icons.Rounded.ArrowForward, contentDescription = "big next",
                        modifier = Modifier.size(100.dp))
                }
            }
        }
    }
}

/**
 * 播放器列表中的所有封面
 */
@Composable
fun AudioSurfaceInAudioList(imgID:Int,modifier: Modifier = Modifier){
    Box(modifier = Modifier.then(modifier)){
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(imgID).apply(block = fun ImageRequest.Builder.() {
                    transformations()
                }).build()
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .matchParentSize()
                .aspectRatio(1.0f)
                .clip(RoundedCornerShape(8.dp))
                .then(modifier)
        )
    }
}