package com.vaibhav.healthify.util

import android.content.Context
import android.widget.Toast
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Long.getFormattedTime(): String {
    val pattern = "hh:mm a"
    val dateFormat = SimpleDateFormat(pattern)
    return dateFormat.format(this)
}

fun Int.getWaterQuantity(): Int {
    val format = DecimalFormat("#.##")
    format.roundingMode = RoundingMode.CEILING
    val limit = (this.toFloat() / 30) * 1000f
    return format.format(limit).toInt()
}

fun Int.getSleepQuantity(): Int {
    return when {
        this >= 65 -> 8
        this >= 26 -> 9
        this >= 18 -> 9
        this >= 14 -> 10
        this >= 6 -> 11
        this >= 3 -> 13
        else -> 15
    }
}
