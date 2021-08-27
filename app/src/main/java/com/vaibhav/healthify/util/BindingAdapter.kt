package com.vaibhav.healthify.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.size.Scale
import com.vaibhav.healthify.R

@BindingAdapter("loadImage")
fun ImageView.loadCoilImage(image: Int) {
    load(image) {
        scale(Scale.FILL)
        crossfade(true)
    }
}

@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImageUrl(image: String) {
    load(image) {
        scale(Scale.FILL)
        crossfade(true)
        error(R.drawable.avatar)
    }
}

@BindingAdapter("setWaterQuantity")
fun TextView.setWaterQuantity(quantity: Int) {
    text = "$quantity mL"
}

@BindingAdapter("setTime")
fun TextView.setTime(time: Long) {
    text = time.getFormattedTime()
}

@BindingAdapter("setDuration")
fun TextView.setTimeDuration(minutes: Int) {
    text = minutes.formatDuration()
}
