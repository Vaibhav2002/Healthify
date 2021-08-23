package com.vaibhav.healthify.ui.userDetailsInput.weight

sealed class UserWeightScreenEvents {
    object NavigateToNextScreen : UserWeightScreenEvents()
    data class ShowToast(val message: String) : UserWeightScreenEvents()
    object ShowNoInternetDialog : UserWeightScreenEvents()
}
