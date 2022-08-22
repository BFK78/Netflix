package com.example.netflix.common

import android.util.Log


fun compareHour(time1: String, time2: String): Boolean {

    val month1 = time1.split(" ")[1]
    val day1 = time1.split(" ")[2]

    val month2 = time2.split(" ")[1]
    val day2 = time2.split(" ")[2]

    if (month2 != month1 || day1 != day2) {
        return false
    }

    val timeSplit1 = time1.split(" ")[3].split(":")
    val timeSplit2 = time2.split(" ")[3].split(":")

    val hour1 = timeSplit1[0]
    val hour2 = timeSplit2[0]
    val minute1 = timeSplit1[1]
    val minute2 = timeSplit2[1]

    val totalMinutes1 = hour1.toInt() * 60 + minute1.toInt()
    val totalMinutes2 = hour2.toInt() * 60 + minute2.toInt()

    val differenceInMinute = kotlin.math.abs(totalMinutes1 - totalMinutes2)

    return differenceInMinute > 30

}