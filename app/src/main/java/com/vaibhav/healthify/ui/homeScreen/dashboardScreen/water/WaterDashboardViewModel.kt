package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.models.local.Water
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.data.repo.WaterRepo
import com.vaibhav.healthify.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaterDashboardViewModel @Inject constructor(
    private val waterRepo: WaterRepo,
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(WaterDashboardScreenState())
    val uiState: StateFlow<WaterDashboardScreenState> = _uiState

    private val _events = MutableSharedFlow<WaterDashboardScreenEvents>()
    val events: SharedFlow<WaterDashboardScreenEvents> = _events

    private var user =
        authRepo.getUserDataFlow().stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        collectUserData()
        collectWaterLogs()
        getFotd()
    }

    private fun getFotd() = viewModelScope.launch {
        val fotd = waterRepo.getFOTD()
        _uiState.emit(uiState.value.copy(factOfTheDay = fotd))
    }

    fun onAddWaterPressed() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isAddWaterButtonEnabled = false))
        _events.emit(WaterDashboardScreenEvents.OpenAddWaterDialog)
    }

    private fun startLoading() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLoading = true))
    }

    private fun stopLoading() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLoading = false))
    }

    fun onWaterSelected(water: WATER) = viewModelScope.launch {
        addWater(water)
        addExp()
    }

    fun onDialogClosed() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isAddWaterButtonEnabled = true))
    }

    private suspend fun addWater(water: WATER) {
        startLoading()
        val resource = waterRepo.insertIntoWaterLog(getWaterModelClass(water))
        stopLoading()
        if (resource is Resource.Error)
            handleError(resource)
        else
            _events.emit(WaterDashboardScreenEvents.CreateAlarm)
    }

    private suspend fun addExp() {
        authRepo.increaseUserExp(WATER_EXP)
    }

    private fun collectUserData() = viewModelScope.launch {
        user.collect { user ->
            user?.let {
                _uiState.emit(
                    uiState.value.copy(
                        username = it.username.substringBefore(' ', it.username),
                        totalAmount = it.waterLimit,
                    )
                )
            }
        }
    }

    private fun collectWaterLogs() = viewModelScope.launch {
        waterRepo.getTodaysWaterLogs().collect { waterLogs ->
            val totalDrinked = waterLogs.sumOf { water ->
                water.quantity.quantity
            }
            user.value?.let {
                val progress = totalDrinked.toFloat() / user.value!!.waterLimit * 100f
                _uiState.emit(
                    uiState.value.copy(
                        waterLog = waterLogs,
                        completedAmount = totalDrinked,
                        progress = progress,
                        greeting = getGreeting(progress),
                        mainGreeting = getMainGreeting(progress)
                    )
                )
            }
        }
    }

    private fun getWaterModelClass(water: WATER) = Water(
        quantity = water,
        timeStamp = System.currentTimeMillis()
    )

    private suspend fun handleError(resource: Resource.Error<*>) {
        val event = when (resource.errorType) {
            ERROR_TYPE.NO_INTERNET -> WaterDashboardScreenEvents.ShowNoInternetDialog
            ERROR_TYPE.UNKNOWN -> WaterDashboardScreenEvents.ShowToast(resource.message)
        }
        _events.emit(event)
    }
}
