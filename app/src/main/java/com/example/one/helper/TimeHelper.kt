package com.example.one.helper

import android.annotation.SuppressLint
import androidx.room.util.newStringBuilder
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId

/**
 * 获取当前时间描述
 */
fun getTimeScr():String{
    val hour = LocalTime.now().hour

    return if(hour < 10)
        "早上"
    else if(hour < 12)
        "上午"
    else if(hour < 18)
        "下午"
    else
        "傍晚"
}

val CurrentIntYear:Int = getCurrentYear()
val CurrentIntMonth:Int = getCurrentMonth()
val CurrentIntDay:Int = getCurrentDay()

/**
 * 获取当前是当前月份的哪一天
 */
fun getCurrentDay():Int{
    return LocalDate.now().dayOfMonth
}

/**
 * 返回当前是某年某月
 */
fun getCurrentMonthAndYear(): Pair<Int,Int>{
    val currentData = LocalDate.now()
    return Pair(currentData.year,currentData.monthValue)
}

/**
 * 返回当前年份
 */
fun getCurrentYear():Int{
    return LocalDate.now().year
}

/**
 * 返回当前月份
 */
fun getCurrentMonth():Int{
    return LocalDate.now().monthValue
}

/**
 * 获取当前时间
 */
fun getCurrentHour():Float{
    val time = LocalTime.now()
    return (time.hour + time.minute/60.0).toFloat()
}

/**
 * 返回当前月份一日是星期几
 */
fun getCurrentMonthBeginWithWhichInMonth(year: Int = CurrentIntYear,month: Int = CurrentIntMonth): Int {
    return LocalDate.of(year, month,1).dayOfWeek.value
}

/**
 * 返回例如20240513的日期串
 */
@SuppressLint("RestrictedApi")
fun getDataString(year: Int, month: Int, day: Int): String {
    val stringBuilder:StringBuilder = newStringBuilder()
    stringBuilder.append(year)
    if(month<10)
    {
        stringBuilder.append(0)
    }
    stringBuilder.append(month)
    stringBuilder.append(day)
    return stringBuilder.toString()
}

/**
 * 检查输入日期是否存在
 */
fun isData(year:Int,month:Int,day:Int):Boolean
{
    return try {
        LocalDate.of(year,month,day)
        true
    }catch(e: DateTimeException)
    {
        false
    }
}

/**
 * 返回某年某月向前数几个月是某年某月
 */
fun findPreviousMonth(inputYear: Int, inputMonth: Int, numMonthsBack: Int): Pair<Int, Int> {
    // 创建 YearMonth 对象表示输入的年份和月份
    val currentYearMonth = YearMonth.of(inputYear, inputMonth)

    // 计算目标月份
    val targetYearMonth = currentYearMonth.minusMonths(numMonthsBack.toLong())

    // 获取目标月份的年份和月份值
    val targetYear = targetYearMonth.year
    val targetMonth = targetYearMonth.monthValue

    return Pair(targetYear, targetMonth)
}

/**
 * 获取当前年月共有几天
 */
fun getNumOfDay(year:Int,month:Int):Int{
    val yearMonth = YearMonth.of(year,month)
    return yearMonth.lengthOfMonth()
}

/**
 * 通过毫秒数获得年月日
 */
fun getYearMonthDay(milliseconds: Long): Triple<Int, Int, Int> {
    val instant = Instant.ofEpochMilli(milliseconds)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    return Triple(localDate.year, localDate.monthValue, localDate.dayOfMonth)
}

/**
 * 从年月日获得毫秒数
 */
fun getMilliseconds(year: Int, month: Int, day: Int): Long {
    val localDate = LocalDate.of(year, month, day)
    val instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
    return instant.toEpochMilli()
}

fun getCaraWithLe(letter: Int):String{
    return when(letter)
    {
        1 -> "一"
        2 -> "二"
        3 -> "三"
        4 -> "四"
        5 -> "五"
        6 -> "六"
        7 -> "七"
        8 -> "八"
        9 -> "九"
        10 -> "拾"
        11 -> "冬"
        13 -> "腊"
        else -> "?"
    }
}

fun getEnCaraWithLe(letter: Int): String {
    return when (letter) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> ""
    }
}
