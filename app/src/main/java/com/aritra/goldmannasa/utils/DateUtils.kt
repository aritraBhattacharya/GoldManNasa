package com.aritra.goldmannasa.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


fun getTodaysDate(): String {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val date = Date(System.currentTimeMillis())
    return dateFormat.format(date)
}