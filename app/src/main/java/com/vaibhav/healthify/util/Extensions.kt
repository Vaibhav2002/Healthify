package com.vaibhav.healthify.util

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Long.getFormattedTime(): String {
    val pattern = "hh:mm a"
    val dateFormat = SimpleDateFormat(pattern)
    return dateFormat.format(this)
}
