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
    return format.format(limit).toDouble().toInt()
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
    } * 60
}

fun Int.formatDuration(): String {
    return if (this / 60 == 0)
        String.format("%02dmin", this)
    else String.format("%dhrs %02dmin", this / 60, this % 60)
}

fun Int.getHoursFromMinutes(): Float {
    return this / 60F
}

fun Int.getMinutesFromHours(): Int {
    return this * 60
}
