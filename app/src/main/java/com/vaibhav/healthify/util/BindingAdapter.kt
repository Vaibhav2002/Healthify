package com.vaibhav.healthify.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("loadImage")
fun ImageView.loadCoilImage(image: Int) {
    load(image) {
        crossfade(true)
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
