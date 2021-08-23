package com.vaibhav.healthify.ui.userDetailsInput.userName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.ui.userDetailsInput.userName.GetUserNameScreenEvents.NavigateToNextScreen
import com.vaibhav.healthify.util.ERROR_TYPE
import com.vaibhav.healthify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetUserNameViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(GetUserNameScreenState())
    val uiState: StateFlow<GetUserNameScreenState> = _uiState

    private val _events = MutableSharedFlow<GetUserNameScreenEvents>()
    val events = _events.asSharedFlow()

    fun onUserNameChange(username: String) = viewModelScope.launch {
        val isValid = username.isNotBlank() && username.isNotEmpty() && !uiState.value.isLoading
        _uiState.emit(_uiState.value.copy(username = username, isNextButtonEnabled = isValid))
    }

    fun onNextButtonClicked() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLoading = true, isNextButtonEnabled = false))
        val resource = authRepo.saveUserName(uiState.value.username)
        _uiState.emit(uiState.value.copy(isLoading = false))
        if (resource is Resource.Success)
            _events.emit(NavigateToNextScreen)
        else {
            _uiState.emit(uiState.value.copy(isNextButtonEnabled = true))
            handleError(resource as Resource.Error<Unit>)
        }
    }

    private fun handleError(resource: Resource.Error<Unit>) = viewModelScope.launch {
        val event = when (resource.errorType) {
            ERROR_TYPE.NO_INTERNET -> GetUserNameScreenEvents.ShowNoInternetDialog
            ERROR_TYPE.UNKNOWN -> GetUserNameScreenEvents.ShowToast(resource.message)
        }
        _events.emit(event)
    }
}
