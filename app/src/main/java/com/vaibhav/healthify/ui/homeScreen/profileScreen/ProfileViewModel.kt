package com.vaibhav.healthify.ui.homeScreen.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.util.Resource
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
        viewModelScope.launch {
            loadLeaderBoard()
            collectUserData()
        }
    }

    suspend fun getUserRank(email: String) = authRepo.getUserRank(email)

    suspend fun collectUserData() {
        user.collect {
            it?.let { userData ->
                val rank = getUserRank(userData.email)
                _uiState.emit(
                    uiState.value.copy(
                        username = userData.username,
                        exp = userData.exp.toInt(),
                        profileImage = userData.profileImg,
                        age = userData.age,
                        weight = userData.weight,
                        rank = rank,
                        isLeaderBoardButtonEnabled = rank != 0
                    )
                )
            }
        }
    }

    fun onRefreshed() = viewModelScope.launch {
        loadLeaderBoard()
    }

    private suspend fun loadLeaderBoard() {
        _uiState.emit(uiState.value.copy(isLoading = true))
        val resource = authRepo.fetchLeaderBoard()
        _uiState.emit(uiState.value.copy(isLoading = false))
        if (resource is Resource.Error)
            _events.emit(ProfileScreenEvents.ShowToast(resource.message))
    }

    fun onAboutPressed() = viewModelScope.launch {
        _events.emit(ProfileScreenEvents.NavigateToAboutScreen)
    }

    fun onLogoutPressed() = viewModelScope.launch {
        disableLogoutButton()
        _events.emit(
            ProfileScreenEvents.ShowLogoutDialog(
                "Confirm Logout",
                "Are you sure that you want to logout"
            )
        )
    }

    fun onDialogClosed() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLogoutButtonEnabled = true))
    }

    fun onLogoutConfirmed() = viewModelScope.launch {
        _events.emit(ProfileScreenEvents.Logout)
    }

    fun onLogoutSuccess() = viewModelScope.launch {
        authRepo.logoutUser()
        _events.emit(ProfileScreenEvents.NavigateToAuthScreen)
    }

    fun disableLogoutButton() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLogoutButtonEnabled = false))
    }

    fun onLogoutFailed(exception: Exception) = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLogoutButtonEnabled = true))
        Timber.d(exception.toString())
        _events.emit(ProfileScreenEvents.ShowToast("Failed to logout"))
    }

    fun onLeaderBoardArrowClicked() = viewModelScope.launch {
        _events.emit(ProfileScreenEvents.NavigateToLeaderBoardScreen)
    }
}
