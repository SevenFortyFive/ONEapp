package com.example.one.data.CharRiverData

data class CharRiverData(
    val label:String,
    val precision:Int
)

fun getCharRiverPrecisionDataList() = listOf(
    CharRiverData("较低",30),
    CharRiverData("标准",40),
    CharRiverData("较高",50)
)