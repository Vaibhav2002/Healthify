package com.vaibhav.healthify.ui.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.util.onBoardingList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    val onboardingList = onBoardingList

    private val _uiState = MutableStateFlow(OnBoardingScreenState())
    val uiState: StateFlow<OnBoardingScreenState> = _uiState

    private val _events = MutableSharedFlow<OnBoardingScreenEvents>()
    val events: SharedFlow<OnBoardingScreenEvents> = _events

    private val page = MutableStateFlow(0)

    init {
        collectPage()
    }

    private fun collectPage() = viewModelScope.launch {
        page.collect {
            val onboarding = onboardingList[it]
            _uiState.emit(
                OnBoardingScreenState(
                    title = onboarding.title,
                    subtitle = onboarding.subtitle,
                    isSkipButtonVisible = it != onBoardingList.size - 1
                )
            )
        }
    }

    fun onNextButtonPressed() = viewModelScope.launch {
        val newPage = page.value + 1
        if (newPage == onBoardingList.size) {
            saveOnBoardingComplete()
            _events.emit(OnBoardingScreenEvents.NavigateToLoginScreen)
        } else {
            _events.emit(OnBoardingScreenEvents.ShowNextPage(newPage))
            page.emit(newPage)
        }
    }

    fun onSKipButtonPressed() = viewModelScope.launch {
        saveOnBoardingComplete()
        _events.emit(OnBoardingScreenEvents.NavigateToLoginScreen)
    }

    suspend fun saveOnBoardingComplete() {
        authRepo.saveUserOnBoardingCompleted()
    }
}
