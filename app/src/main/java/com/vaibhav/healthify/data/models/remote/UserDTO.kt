package com.vaibhav.healthify.data.models.remote

data class UserDTO(
    val username: String = "",
    val email: String = "",
    val profileImg: String = "",
    val exp: Int = 0,
    val waterLimit: Int = 0,
    val sleepLimit: Int = 0,
    val age: Int = 0,
    val weight: Float = 0f
)
