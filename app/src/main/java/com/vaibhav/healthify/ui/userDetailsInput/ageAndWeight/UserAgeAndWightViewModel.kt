package com.vaibhav.healthify.ui.userDetailsInput.ageAndWeight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAgeAndWightViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(UserAgeAndWeightScreenState())
    val uiState: StateFlow<UserAgeAndWeightScreenState> = _uiState

    private val _events = Channel<UserAgeAndWeightScreenEvents>()
    val events = _events.receiveAsFlow()

    fun shouldButtonBeEnabled() = !uiState.value.isLoading &&
        uiState.value.age != 0 && uiState.value.weight != 0f

    fun onAgeChange(age: String) = viewModelScope.launch {
        val newAge = age.toIntOrNull() ?: 0
        _uiState.emit(_uiState.value.copy(age = newAge))
        _uiState.emit(_uiState.value.copy(isButtonEnabled = shouldButtonBeEnabled()))
    }

    fun onWeightChange(weight: String) = viewModelScope.launch {
        val newWeight = weight.toFloatOrNull() ?: 0f
        _uiState.emit(_uiState.value.copy(weight = newWeight))
        _uiState.emit(_uiState.value.copy(isButtonEnabled = shouldButtonBeEnabled()))
    }

    fun onContinueButtonPressed() = viewModelScope.launch {
        _uiState.emit(_uiState.value.copy(isLoading = true, isButtonEnabled = false))
        val resource =
            authRepo.saveUserAgeAndWeight(age = uiState.value.age, weight = uiState.value.weight)
        _uiState.emit(_uiState.value.copy(isLoading = false))
        if (resource is Resource.Success) {
            _events.send(UserAgeAndWeightScreenEvents.ShowToast("Details updated successfully"))
            _events.send(UserAgeAndWeightScreenEvents.NavigateToNextScreen)
        } else {
            _uiState.emit(_uiState.value.copy(isLoading = true))
            _events.send(UserAgeAndWeightScreenEvents.ShowToast("Failed to update details"))
        }
    }
}
