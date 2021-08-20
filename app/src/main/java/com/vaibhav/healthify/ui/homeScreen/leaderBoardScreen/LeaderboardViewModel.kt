package com.vaibhav.healthify.ui.homeScreen.leaderBoardScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.LeaderboardRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepo: LeaderboardRepo
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        collectLeaderboard()
    }

    fun collectLeaderboard() = viewModelScope.launch {
        leaderboardRepo.getLeaderboard().collect {
            _uiState.emit(uiState.value.copy(leaderboardItems = it))
        }
    }
}
