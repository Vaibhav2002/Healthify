package com.vaibhav.healthify.util

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vaibhav.chatofy.util.setMarginTop
import com.vaibhav.healthify.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Long.getFormattedTime(): String {
    val pattern = "hh:mm a"
    val dateFormat = SimpleDateFormat(pattern)
    return dateFormat.format(this)
}

fun Int.getWaterQuantity(): Int {
    val limit = (this.toFloat() / 30) * 1000f
    return limit.roundOff().toInt()
}

fun Float.roundOff(): Float {
    val format = DecimalFormat("#.##")
    format.roundingMode = RoundingMode.CEILING
    return format.format(this).toFloat()
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
    return (this / 60F).roundOff()
}

fun Int.getMinutesFromHours(): Int {
    return this * 60
}

fun View.setMarginTopForFullScreen() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        this.setMarginTop(insets.systemWindowInsetTop)
        insets.consumeSystemWindowInsets()
    }
}

fun Calendar.getFormattedDate(): String {
    val suffix = this[Calendar.DAY_OF_MONTH].getDayNumberSuffix()
    return this.timeInMillis.formatDate(suffix)
}

fun Int.getDayNumberSuffix(): String {
    return if (this in 11..13) {
        "th"
    } else when (this % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}

fun Long.formatDate(dayNumberSuffix: String): String {
    val pattern = "d'$dayNumberSuffix' MMM"
    val sdf = SimpleDateFormat(pattern)
    return sdf.format(this)
}

fun Context.showDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val dialog = MaterialAlertDialogBuilder(this)
        .setBackground(getDrawable(R.drawable.alert_dialog_bg))
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Confirm") { _, _ ->
            onConfirm()
        }
        .setNegativeButton("Cancel", null)
        .create()
    dialog.setOnDismissListener {
        onDismiss()
    }
    dialog.show()
}

fun String.getFirstName(): String {
    return this.substringBefore(' ')
}

fun Context.isDarkModeOn(): Boolean {
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}
