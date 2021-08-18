package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.models.local.Sleep
import com.vaibhav.healthify.data.models.local.User
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.data.repo.SleepRepo
import com.vaibhav.healthify.util.Resource
import com.vaibhav.healthify.util.SLEEP_EXP
import com.vaibhav.healthify.util.getGreeting
import com.vaibhav.healthify.util.getHoursFromMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepDashboardViewModel @Inject constructor(
    private val sleepRepo: SleepRepo,
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(SleepDashboardScreenState())
    val uiState: StateFlow<SleepDashboardScreenState> = _uiState

    private val _events = MutableSharedFlow<SleepDashboardScreenEvents>()
    val events: SharedFlow<SleepDashboardScreenEvents> = _events

    private var user = MutableStateFlow<User?>(null)

    init {
        viewModelScope.launch {
            getUserInfo()
            collectSleepLogs()
        }
        collectUserData()
        getFotd()
    }

    private fun getFotd() = viewModelScope.launch {
        val fotd = sleepRepo.getFOTD()
        _uiState.emit(uiState.value.copy(factOfTheDay = fotd))
    }

    fun onAddSleepPressed() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isAddSleepButtonEnabled = false))
        _events.emit(SleepDashboardScreenEvents.OpenAddSleepDialog)
    }

    private fun startLoading() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLoading = true))
    }

    private fun stopLoading() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLoading = false))
    }

    fun onDialogClosed() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isAddSleepButtonEnabled = true))
    }

    fun onSleepSelected(sleepDuration: Int) = viewModelScope.launch {
        addSleep(sleepDuration)
        addExp()
    }

    private suspend fun addSleep(sleepDuration: Int) {
        startLoading()
        val resource = sleepRepo.insertIntoSleepLog(getSleepModelClass(sleepDuration))
        stopLoading()
        if (resource is Resource.Error)
            _events.emit(SleepDashboardScreenEvents.ShowToast(resource.message))
    }

    private suspend fun addExp() {
        authRepo.increaseUserExp(SLEEP_EXP)
    }

    private fun collectUserData() = viewModelScope.launch {
        user.collect { user ->
            user?.let {
                _uiState.emit(
                    uiState.value.copy(
                        username = it.username,
                        totalAmount = it.sleepLimit.getHoursFromMinutes(),
                    )
                )
            }
        }
    }

    private suspend fun getUserInfo() {
        user.emit(authRepo.getCurrentUser())
    }

    private suspend fun collectSleepLogs() {
        sleepRepo.getTodaysSleepLogs().collect { sleepLogs ->
            val totalSlept = sleepLogs.sumOf { sleep ->
                sleep.sleepDuration
            }
            val progress = (totalSlept.toFloat() / user.value!!.sleepLimit) * 100f
            _uiState.emit(
                uiState.value.copy(
                    sleepLog = sleepLogs,
                    completedAmount = totalSlept.getHoursFromMinutes(),
                    progress = progress,
                    greeting = getGreeting(progress)
                )
            )
        }
    }

    private fun getSleepModelClass(minutes: Int) = Sleep(
        sleepDuration = minutes,
        timeStamp = System.currentTimeMillis()
    )
}
