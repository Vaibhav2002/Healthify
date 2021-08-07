package com.vaibhav.healthify.ui.userDetailsInput.ageAndWeight

sealed class UserAgeAndWeightScreenEvents {
    object NavigateToNextScreen : UserAgeAndWeightScreenEvents()
    data class ShowToast(val message: String) : UserAgeAndWeightScreenEvents()
}
