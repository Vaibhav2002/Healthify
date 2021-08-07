package com.vaibhav.healthify.ui.userDetailsInput.userName

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.ui.userDetailsInput.userName.GetUserNameScreenEvents.NavigateToNextScreen
import com.vaibhav.healthify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetUserNameViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(GetUserNameScreenState())
    val uiState: StateFlow<GetUserNameScreenState> = _uiState

    private val _events = Channel<GetUserNameScreenEvents>()
    val events = _events.receiveAsFlow()

    fun onUserNameChange(username: String) = viewModelScope.launch {
        val isValid = username.isNotBlank() && username.isNotEmpty() && !uiState.value.isLoading
        _uiState.emit(_uiState.value.copy(username = username, isNextButtonEnabled = isValid))
    }

    fun onNextButtonClicked() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLoading = true, isNextButtonEnabled = false))
        val resource = authRepo.saveUserName(uiState.value.username)
        if (resource is Resource.Success)
            _events.send(NavigateToNextScreen)
        else {
            _uiState.emit(uiState.value.copy(isLoading = false, isNextButtonEnabled = true))
            _events.send(GetUserNameScreenEvents.ShowToast(resource.message))
        }
    }
}
