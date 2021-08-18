package com.vaibhav.healthify.ui.homeScreen.profileScreen

sealed class ProfileScreenEvents {
    object NavigateToAboutScreen : ProfileScreenEvents()
    data class ShowLogoutDialog(
        val title: String,
        val description: String
    ) : ProfileScreenEvents()

    data class ShowToast(val message: String) : ProfileScreenEvents()
    object Logout : ProfileScreenEvents()
    object NavigateToAuthScreen : ProfileScreenEvents()
}
