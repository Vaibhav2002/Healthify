package com.vaibhav.healthify.data.models.local

data class User(
    var username: String,
    val email: String,
    val profileImg: String,
    var exp: Long,
    var waterLimit: Int,
    var sleepLimit: Int,
    var age: Int,
    var weight: Int
)
