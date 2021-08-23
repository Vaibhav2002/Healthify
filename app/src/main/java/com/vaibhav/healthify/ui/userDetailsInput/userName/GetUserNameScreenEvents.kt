package com.vaibhav.healthify.ui.userDetailsInput.userName

sealed class GetUserNameScreenEvents {
    object NavigateToNextScreen : GetUserNameScreenEvents()
    data class ShowToast(val message: String) : GetUserNameScreenEvents()
    object ShowNoInternetDialog : GetUserNameScreenEvents()
}
