package com.vaibhav.healthify.ui.homeScreen.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.data.repo.LeaderboardRepo
import com.vaibhav.healthify.util.ERROR_TYPE
import com.vaibhav.healthify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val leaderboardRepo: LeaderboardRepo
) : ViewModel() {

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
                        email = userData.email,
                        isLeaderBoardButtonEnabled = rank != 0
                    )
                )
            }
        }
    }

    suspend fun getUserRank(email: String) = leaderboardRepo.getUserRank(email)

    fun onRefreshed() = viewModelScope.launch {
        loadLeaderBoard()
        updateRank()
    }

    suspend fun updateRank() {
        val rank = getUserRank(uiState.value.email)
        _uiState.emit(uiState.value.copy(rank = rank, isLeaderBoardButtonEnabled = rank != 0))
    }

    private suspend fun loadLeaderBoard() {
        _uiState.emit(uiState.value.copy(isLoading = true))
        val resource = leaderboardRepo.fetchLeaderBoard()
        _uiState.emit(uiState.value.copy(isLoading = false))
        if (resource is Resource.Error)
            handleError(resource)
    }

    private fun handleError(resource: Resource.Error<*>) = viewModelScope.launch {
        val event = when (resource.errorType) {
            ERROR_TYPE.NO_INTERNET -> ProfileScreenEvents.ShowNoInternetDialog
            ERROR_TYPE.UNKNOWN -> ProfileScreenEvents.ShowToast(resource.message)
        }
        _events.emit(event)
    }

    private fun updateUserSleepLimit(limit: Int) = viewModelScope.launch {
        Timber.d("Editing sleep Limit in VM")
        _uiState.emit(uiState.value.copy(isLoading = true))
        val resource = authRepo.updateUserSleepLimit(limit)
        _uiState.emit(uiState.value.copy(isLoading = false))
        if (resource is Resource.Success)
            _events.emit(ProfileScreenEvents.ShowToast("Updated successfully"))
        else
            handleError(resource = resource as Resource.Error<*>)
    }

    private fun updateUserWaterLimit(limit: Int) = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLoading = true))
        val resource = authRepo.updateUserWaterLimit(limit)
        _uiState.emit(uiState.value.copy(isLoading = false))
        if (resource is Resource.Success)
            _events.emit(ProfileScreenEvents.ShowToast("Updated successfully"))
        else
            handleError(resource = resource as Resource.Error<*>)
    }

    fun onAboutPressed() = viewModelScope.launch {
        _events.emit(ProfileScreenEvents.NavigateToAboutScreen)
    }

    fun onLogoutPressed() = viewModelScope.launch {
        disableLogoutButton()
        _events.emit(
            ProfileScreenEvents.ShowLogoutDialog(
                "Confirm Logout",
                "Are you sure that you want to logout?"
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

    fun onLeaderBoardClicked() = viewModelScope.launch {
        _events.emit(ProfileScreenEvents.NavigateToLeaderBoardScreen)
    }

    fun editWaterLimitButtonState(isEnabled: Boolean) = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isEditWaterQuantityButtonEnabled = isEnabled))
    }

    fun editSleepLimitButtonState(isEnabled: Boolean) = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isEditSleepLimitButtonEnabled = isEnabled))
    }

    fun onEditWaterLimitPressed() = viewModelScope.launch {
        editWaterLimitButtonState(false)
        _events.emit(
            ProfileScreenEvents.OpenWaterLimitDialog(
                onQuantitySelected = {
                    updateUserWaterLimit(it)
                },
                onDismiss = {
                    editWaterLimitButtonState(true)
                }
            )
        )
    }

    fun onEditSleepLimitPressed() = viewModelScope.launch {
        editSleepLimitButtonState(false)
        _events.emit(
            ProfileScreenEvents.OpenSleepLimitDialog(
                onTimeSelected = {
                    updateUserSleepLimit(it)
                },
                onDismiss = {
                    editSleepLimitButtonState(true)
                }
            )
        )
    }
}
