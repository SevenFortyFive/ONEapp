package com.example.one.myui.AudioUi

import com.example.one.vm.PlayerViewModel
import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.one.R
import com.example.one.data.PlayerData.PlayerState
import com.example.one.data.SQLite.entity.MyAudioData
import com.example.one.setting.Setting
import com.example.one.vm.MyAudioViewModelFactory

@Composable
fun Player(){

    val vm: PlayerViewModel = viewModel(LocalContext.current as ComponentActivity
        ,factory = MyAudioViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    val playerState = vm.currentState.observeAsState()
    val currentData by vm.currentAudioData.observeAsState()

    Card(
        modifier = Modifier
            .animateContentSize()
            .padding(10.dp),
        elevation =  CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(Setting.WholeElevation)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
            contentAlignment = Alignment.Center)
        {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CD(isPlaying = (playerState.value == PlayerState.PLAYING),

                    imgID = currentData?.surfaceId ?: R.raw.blackbird,

                    modifier = Modifier)
                Spacer(modifier = Modifier.height(2.dp))
                Row(modifier = Modifier
                    .fillMaxWidth(),){
                    Column(modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(text = currentData?.name ?: "尚未加载", maxLines = 1, fontWeight = FontWeight.Bold)

                        Text(text = "无名", maxLines = 1)
                    }
                    Spacer(modifier = Modifier.width(10.dp))


                    var checked by remember {
                        mutableStateOf(false)
                    }

                    Row(modifier = Modifier.weight(1f),
                        Arrangement.Center) {
                        IconButton(onClick = {
                            vm.changeItemLoveState(currentData!!)
                        }) {
                            if(currentData?.love == true)
                            {
                                Icon(imageVector = Icons.Rounded.Favorite, contentDescription = "Love Audio", tint = Color.Red)
                            }else{
                                Icon(imageVector = Icons.Rounded.FavoriteBorder, contentDescription = "Love Audio")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row (modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly){
                    IconButton(onClick = { vm.preAudio() }) {
                        Icon(imageVector = Icons.Rounded.KeyboardArrowLeft, contentDescription = "fore audio")
                    }
                    IconButton(onClick = {
                        if(playerState.value == PlayerState.STOP)
                        {
                            vm.start()
                        }else{
                            vm.pause()
                        }
                    }) {
                        if (playerState.value == PlayerState.PLAYING)
                        {
                            Icon(imageVector = Icons.Rounded.Close, contentDescription = "pause")
                        }else{
                            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = "begin")
                        }
                    }
                    IconButton(onClick = { vm.nextAudio() }) {
                        Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = "next audio")
                    }
                }
            }
        }
    }
}


@Composable
fun MyListItem(data:MyAudioData){
    ListItem(
//        modifier = Modifier.height(20.dp),
        headlineContent = { Text(data.name) },
        supportingContent = { Text(data.author) },
        trailingContent = { Icon(imageVector = Icons.Rounded.List, contentDescription = "Audio List Item") },
        leadingContent = {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data.surfaceId).apply(block = fun ImageRequest.Builder.() {
                        transformations(CircleCropTransformation())
                    }).build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
//                    .matchParentSize()
                    .aspectRatio(1.0f)
                    .padding(20.dp)
            )

        }
    )
    HorizontalDivider()
}

@SuppressLint("ResourceType")
@Composable
private fun CD(isPlaying: Boolean, imgID: Int, modifier: Modifier) {
    Box(
        modifier = modifier.padding(horizontal = 36.dp)
    ) {
        //唱片旋转角度
        val rotation = infiniteRotation(isPlaying)
        //唱针旋转角度
        val stylusRotation by animateFloatAsState(targetValue = if (isPlaying) 0f else -30f,
            label = "CD_ROUND"
        )
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
                .graphicsLayer {
                    rotationZ = rotation.value
                }
        )
        //唱片边框
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
        //唱片针
        Image(
            painter = painterResource(id = R.drawable.bgm),
            contentDescription = null,
            modifier = Modifier
                .align(BiasAlignment(0.3f, -1f))
                .graphicsLayer {
                    rotationZ = stylusRotation
                    transformOrigin = TransformOrigin(0f, 0f)
                }
        )
    }
}

/**
 * 无限循环的旋转动画
 */
@Composable
private fun infiniteRotation(
    startRotate: Boolean,
    duration: Int = 15 * 1000
): Animatable<Float, AnimationVector1D> {
    var rotation by remember { mutableStateOf(Animatable(0f)) }
    LaunchedEffect(key1 = startRotate, block = {
        if (startRotate) {
            //从上次的暂停角度 -> 执行动画 -> 到目标角度（+360°）
            rotation.animateTo(
                (rotation.value % 360f) + 360f, animationSpec = infiniteRepeatable(
                    animation = tween(duration, easing = LinearEasing)
                )
            )
        } else {
            rotation.stop()
            //初始角度取余是为了防止每次暂停后目标角度无限增大
            rotation = Animatable(rotation.value % 360f)
        }
    })
    return rotation
}