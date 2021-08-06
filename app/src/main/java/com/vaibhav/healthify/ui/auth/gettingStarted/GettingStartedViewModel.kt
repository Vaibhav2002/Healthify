package com.vaibhav.healthify.ui.auth.gettingStarted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.result.UserProfile
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.ui.auth.gettingStarted.GettingStartedScreenEvents.NavigateFurther
import com.vaibhav.healthify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GettingStartedScreenState(
    val isLoading: Boolean = false
)

sealed class GettingStartedScreenEvents {
    class Error(val message: String) : GettingStartedScreenEvents()
    object NavigateFurther : GettingStartedScreenEvents()
    object Logout : GettingStartedScreenEvents()
}

@HiltViewModel
class GettingStartedViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val user = MutableStateFlow<UserProfile?>(null)

    private val _uiState = MutableStateFlow(GettingStartedScreenState())
    val uiState: StateFlow<GettingStartedScreenState> = _uiState

    private val _events = Channel<GettingStartedScreenEvents>()
    val events = _events.receiveAsFlow()

    fun saveUser(userProfile: UserProfile) = viewModelScope.launch {
        user.emit(userProfile)
    }

    fun startLoading() = viewModelScope.launch {
        _uiState.emit(_uiState.value.copy(isLoading = true))
    }

    private fun stopLoading() = viewModelScope.launch {
        _uiState.emit(_uiState.value.copy(isLoading = false))
    }

    fun sendError(message: String) = viewModelScope.launch {
        stopLoading()
        _events.send(GettingStartedScreenEvents.Error(message))
    }

    fun loginComplete() = viewModelScope.launch {
        user.value?.let {
            val resource = authRepo.continueAfterLogin(it)
            stopLoading()
            if (resource is Resource.Success) {
                _events.send(NavigateFurther)
            } else {
                sendError(resource.message)
                _events.send(GettingStartedScreenEvents.Logout)
            }
        }
    }
}
