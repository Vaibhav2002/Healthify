package com.vaibhav.healthify.ui.homeScreen.profileScreen

data class ProfileScreenState(
    val username: String = "",
    val profileImage: String = "",
    val age: Int = 0,
    val weight: Int = 0,
    val exp: Int = 0,
    val rank: Int = 0,
    val isLoading: Boolean = false,
    val isLogoutButtonEnabled: Boolean = true
)
