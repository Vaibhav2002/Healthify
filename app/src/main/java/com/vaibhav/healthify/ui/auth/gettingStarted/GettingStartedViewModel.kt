package com.vaibhav.healthify.ui.auth.gettingStarted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.result.UserProfile
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GettingStartedScreenState(
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = true
)

sealed class GettingStartedScreenEvents {
    data class ShowToast(val message: String) : GettingStartedScreenEvents()
    object NavigateToUserDetailsScreen : GettingStartedScreenEvents()
    object NavigateToHomeScreen : GettingStartedScreenEvents()
    object Logout : GettingStartedScreenEvents()
}

@HiltViewModel
class GettingStartedViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    companion object {
        private const val FAILED_TO_LOGIN = "Failed to log you in. Please try again"
        private const val LOGIN_SUCCESS = "User logged in successfully"
    }

    private val user = MutableStateFlow<UserProfile?>(null)

    private val _uiState = MutableStateFlow(GettingStartedScreenState())
    val uiState: StateFlow<GettingStartedScreenState> = _uiState

    private val _events = MutableSharedFlow<GettingStartedScreenEvents>()
    val events: SharedFlow<GettingStartedScreenEvents> = _events

    fun saveUser(userProfile: UserProfile) = viewModelScope.launch {
        user.emit(userProfile)
    }

    fun startLoading() = viewModelScope.launch {
        _uiState.emit(_uiState.value.copy(isLoading = true, isButtonEnabled = false))
    }

    private fun stopLoading() = viewModelScope.launch {
        _uiState.emit(_uiState.value.copy(isLoading = false, isButtonEnabled = true))
    }

    fun sendError(message: String) = viewModelScope.launch {
        stopLoading()
        _events.emit(GettingStartedScreenEvents.ShowToast(message))
    }

    fun loginComplete() = viewModelScope.launch {
        user.value?.let {
            val isUserAlreadyRegistered = authRepo.isUserRegistered(it)
            if (isUserAlreadyRegistered is Resource.Error) {
                sendError(isUserAlreadyRegistered.message)
                _events.emit(GettingStartedScreenEvents.Logout)
                return@launch
            }
            val resource = authRepo.continueAfterLogin(it)
            stopLoading()
            if (resource is Resource.Success) {
                _events.emit(GettingStartedScreenEvents.ShowToast(LOGIN_SUCCESS))
                if (isUserAlreadyRegistered.data!!) {
                    authRepo.saveUserDataEntryCompleted()
                    _events.emit(GettingStartedScreenEvents.NavigateToHomeScreen)
                } else
                    _events.emit(GettingStartedScreenEvents.NavigateToUserDetailsScreen)
            } else {
                sendError(resource.message)
                _events.emit(GettingStartedScreenEvents.Logout)
            }
        }
    }

    fun logoutFailed() = viewModelScope.launch {
        authRepo.logoutUser()
        _events.emit(GettingStartedScreenEvents.ShowToast(FAILED_TO_LOGIN))
    }

    fun logoutComplete() = viewModelScope.launch {
        authRepo.logoutUser()
        _events.emit(GettingStartedScreenEvents.ShowToast(FAILED_TO_LOGIN))
    }
}
