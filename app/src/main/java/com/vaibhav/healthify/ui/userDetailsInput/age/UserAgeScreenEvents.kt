package com.vaibhav.healthify.ui.userDetailsInput.age

sealed class UserAgeScreenEvents {
    data class ShowToast(val message: String) : UserAgeScreenEvents()
    object NavigateToHomeScreen : UserAgeScreenEvents()
    object ShowNoInternetDialog : UserAgeScreenEvents()
}
