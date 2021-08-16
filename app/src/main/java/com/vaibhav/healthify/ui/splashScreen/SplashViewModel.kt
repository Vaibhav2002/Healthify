package com.vaibhav.healthify.ui.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _events = MutableSharedFlow<SplashScreenEvents>()
    val events: SharedFlow<SplashScreenEvents> = _events

    private suspend fun isUserLoggedIn() = authRepo.isUserLoggedIn()

    private suspend fun isOnBoardingComplete() = authRepo.isUserOnBoardingComplete()

    private suspend fun isUserDetailsCompleted() = authRepo.isUserDataEntryCompleted()

    fun onTimerComplete() = viewModelScope.launch {
        if (!isOnBoardingComplete())
            _events.emit(SplashScreenEvents.NavigateToOnBoarding)
        else if (!isUserLoggedIn())
            _events.emit(SplashScreenEvents.NavigateToLoginScreen)
        else if (!isUserDetailsCompleted())
            _events.emit(SplashScreenEvents.NavigateToUserDetailsScreen)
        else
            _events.emit(SplashScreenEvents.NavigateToHomeScreen)
    }
}
