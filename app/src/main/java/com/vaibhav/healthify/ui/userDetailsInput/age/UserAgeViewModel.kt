package com.vaibhav.healthify.ui.userDetailsInput.age

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.util.ERROR_TYPE
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
class UserAgeViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(UserAgeScreenState())
    val uiState: StateFlow<UserAgeScreenState> = _uiState

    private val _events = MutableSharedFlow<UserAgeScreenEvents>()
    val events: SharedFlow<UserAgeScreenEvents> = _events

    fun onAgeChange(age: Int) = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(age = age))
    }

    fun onContinueButtonPressed() = viewModelScope.launch {
        saveUserAge()
    }

    private suspend fun saveUserAge() {
        _uiState.emit(uiState.value.copy(isLoading = true, isButtonEnabled = false))
        val resource = authRepo.saveUserAge(uiState.value.age)
        _uiState.emit(uiState.value.copy(isLoading = false))
        if (resource is Resource.Success) {
            authRepo.saveUserDataEntryCompleted()
            _events.emit(UserAgeScreenEvents.ShowToast(USER_DETAILS_UPDATED))
            _events.emit(UserAgeScreenEvents.NavigateToHomeScreen)
        } else {
            _uiState.emit(uiState.value.copy(isButtonEnabled = true))
            handleError(resource as Resource.Error<Unit>)
        }
    }

    private fun handleError(resource: Resource.Error<Unit>) = viewModelScope.launch {
        val event = when (resource.errorType) {
            ERROR_TYPE.NO_INTERNET -> UserAgeScreenEvents.ShowNoInternetDialog
            ERROR_TYPE.UNKNOWN -> UserAgeScreenEvents.ShowToast(USER_DETAILS_UPDATE_FAILED)
        }
        _events.emit(event)
    }
}
