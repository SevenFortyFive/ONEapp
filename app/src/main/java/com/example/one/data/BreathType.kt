package com.example.one.data

import com.example.one.data.SQLite.entity.MyBreathTypeData

/**
 * @since 2024/5/16
 */
fun getBreathTypeList() = listOf(
    // 4s停顿 -> 4s吸气 -> 4s停顿 -> 4s呼气
    MyBreathTypeData(1,"正方形呼吸法","战术镇定",
        "帮助平衡呼吸，提高专注力和情绪调节能力",
        4000L,4000L,4000L,4000L),
    // 4吸气 -> 4s停顿 -> 4s呼气 -> 4s吸气
    MyBreathTypeData(2,"三角形呼吸法","解压器",
        "想象用呼吸描画一个等边三角形，缓解压力和焦虑",
        4000L,4000L,4000L,0L),
    // 4吸气 -> 4s呼气 -> 4s吸气
    MyBreathTypeData(3,"古代瑜伽呼吸法","生命力",
        "增加氧和作用并增强体内机能，让生命力在体内循环" ,
        4000L,0L,4000L,0L),
    // 4吸气 -> 7s停顿 -> 8s呼气 -> 4s吸气
    MyBreathTypeData(4,"放松呼吸","轻松入睡","快速入眠，为睡个好觉做准备",
        4000L,7000L,8000L,0L)
)

/**
 * 呼吸时长
 */
fun getBreathTimeList() = listOf(
    1,
    2,
    5,
    10,
    15,
    16,
    32
)