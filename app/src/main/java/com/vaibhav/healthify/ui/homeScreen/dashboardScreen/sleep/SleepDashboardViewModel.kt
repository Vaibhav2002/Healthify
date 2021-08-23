package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.sleep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.models.local.Sleep
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.data.repo.SleepRepo
import com.vaibhav.healthify.util.*
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

    private var user =
        authRepo.getUserDataFlow().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        collectUserData()
        collectSleepLogs()
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
            handleError(resource)
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

    private fun collectSleepLogs() = viewModelScope.launch {
        sleepRepo.getTodaysSleepLogs().collect { sleepLogs ->
            val totalSlept = sleepLogs.sumOf { sleep ->
                sleep.sleepDuration
            }
            user.value?.let {
                val progress = (totalSlept.toFloat() / it.sleepLimit) * 100f
                _uiState.emit(
                    uiState.value.copy(
                        sleepLog = sleepLogs,
                        completedAmount = totalSlept.getHoursFromMinutes(),
                        progress = progress,
                        greeting = getGreeting(progress),
                        mainGreeting = getMainGreeting(progress)
                    )
                )
            }
        }
    }

    private fun getSleepModelClass(minutes: Int) = Sleep(
        sleepDuration = minutes,
        timeStamp = System.currentTimeMillis()
    )

    private fun handleError(resource: Resource.Error<*>) = viewModelScope.launch {
        val event = when (resource.errorType) {
            ERROR_TYPE.NO_INTERNET -> SleepDashboardScreenEvents.ShowNoInternetDialog
            ERROR_TYPE.UNKNOWN -> SleepDashboardScreenEvents.ShowToast(resource.message)
        }
        _events.emit(event)
    }
}
