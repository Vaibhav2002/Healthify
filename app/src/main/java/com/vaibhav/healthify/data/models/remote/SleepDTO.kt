package com.vaibhav.healthify.data.models.remote

data class SleepDTO(
    var userEmail: String = "",
    val duration: Int = 0,
    val timeStamp: Long = 0
)
