package com.vaibhav.healthify.ui.homeScreen.profileScreen

sealed class ProfileScreenEvents {

    object NavigateToAboutScreen : ProfileScreenEvents()

    data class ShowLogoutDialog(
        val title: String,
        val description: String
    ) : ProfileScreenEvents()

    data class ShowToast(val message: String) : ProfileScreenEvents()

    object ShowNoInternetDialog : ProfileScreenEvents()

    object Logout : ProfileScreenEvents()

    object NavigateToAuthScreen : ProfileScreenEvents()

    object NavigateToLeaderBoardScreen : ProfileScreenEvents()

    data class OpenSleepLimitDialog(
        val onTimeSelected: (Int) -> Unit,
        val onDismiss: () -> Unit
    ) : ProfileScreenEvents()

    data class OpenWaterLimitDialog(
        val onQuantitySelected: (Int) -> Unit,
        val onDismiss: () -> Unit
    ) : ProfileScreenEvents()
}
