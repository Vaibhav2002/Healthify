package com.vaibhav.healthify.ui.homeScreen.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileScreenEvents>()
    val events = _events.asSharedFlow()

    private val user =
        authRepo.getUserDataFlow().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        collectUserData()
    }

    fun collectUserData() = viewModelScope.launch {
        user.collect {
            it?.let { userData ->
                _uiState.emit(
                    uiState.value.copy(
                        username = userData.username,
                        exp = userData.exp.toInt(),
                        profileImage = userData.profileImg,
                        age = userData.age,
                        weight = userData.weight,
                    )
                )
            }
        }
    }

    fun onAboutPressed() = viewModelScope.launch {
        _events.emit(ProfileScreenEvents.NavigateToAboutScreen)
    }

    fun onLogoutPressed() = viewModelScope.launch {
        _events.emit(
            ProfileScreenEvents.ShowLogoutDialog(
                "Confirm Logout",
                "Are you sure that you want to logout"
            )
        )
    }

    fun onLogoutConfirmed() = viewModelScope.launch {
        _events.emit(ProfileScreenEvents.Logout)
    }

    fun onLogoutSuccess() = viewModelScope.launch {
        authRepo.logoutUser()
        _events.emit(ProfileScreenEvents.NavigateToAuthScreen)
    }

    fun onLogoutFailed(exception: Exception) = viewModelScope.launch {
        Timber.d(exception.toString())
        _events.emit(ProfileScreenEvents.ShowToast("Failed to logout"))
    }
}
