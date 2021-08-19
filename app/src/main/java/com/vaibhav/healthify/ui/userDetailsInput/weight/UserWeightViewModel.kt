package com.vaibhav.healthify.ui.userDetailsInput.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.util.Resource
import com.vaibhav.healthify.util.USER_DETAILS_UPDATED
import com.vaibhav.healthify.util.USER_DETAILS_UPDATE_FAILED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserWeightViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    val DEFAULT_WEIGHT = 40

    val weightList = (1..200).map { it.toString() }

    private val _uiState = MutableStateFlow(UserWeightScreenState())
    val uiState: StateFlow<UserWeightScreenState> = _uiState

    private val _events = MutableSharedFlow<UserWeightScreenEvents>()
    val events: SharedFlow<UserWeightScreenEvents> = _events

    fun onWeightChange(index: Int) = viewModelScope.launch {
        val newWeight = weightList[index].toInt()
        _uiState.emit(_uiState.value.copy(weight = newWeight))
    }

    fun onNextButtonPressed() = viewModelScope.launch {
        _uiState.emit(_uiState.value.copy(isLoading = true, isButtonEnabled = false))
        val resource =
            authRepo.saveUserWeight(weight = uiState.value.weight)
        _uiState.emit(_uiState.value.copy(isLoading = false, isButtonEnabled = true))
        if (resource is Resource.Success) {
            _events.emit(UserWeightScreenEvents.ShowToast(USER_DETAILS_UPDATED))
            _events.emit(UserWeightScreenEvents.NavigateToNextScreen)
        } else {
            _events.emit(UserWeightScreenEvents.ShowToast(USER_DETAILS_UPDATE_FAILED))
        }
    }
}
