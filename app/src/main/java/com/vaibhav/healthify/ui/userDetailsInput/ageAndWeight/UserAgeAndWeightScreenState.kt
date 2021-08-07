package com.vaibhav.healthify.ui.userDetailsInput.ageAndWeight

data class UserAgeAndWeightScreenState(
    val isLoading: Boolean = false,
    val age: Int = 0,
    val weight: Float = 0f,
    val isButtonEnabled: Boolean = false
)
