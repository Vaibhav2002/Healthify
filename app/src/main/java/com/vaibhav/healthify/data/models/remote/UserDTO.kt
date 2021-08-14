package com.vaibhav.healthify.data.models.remote

data class UserDTO(
    val username: String = "",
    val email: String = "",
    val profileImg: String = "",
    val exp: Long = 0L,
    val waterLimit: Int = 0,
    val sleepLimit: Int = 0,
    val age: Int = 0,
    val weight: Int = 0
)
