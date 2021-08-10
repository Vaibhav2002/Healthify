package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.models.local.User
import com.vaibhav.healthify.data.models.local.Water
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.data.repo.WaterRepo
import com.vaibhav.healthify.util.Resource
import com.vaibhav.healthify.util.WATER
import com.vaibhav.healthify.util.getGreeting
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

    private var user = MutableStateFlow<User?>(null)

    init {
        viewModelScope.launch {
            getUserInfo()
            collectWaterLogs()
        }
        collectUserData()
    }

    fun onAddWaterPressed() = viewModelScope.launch {
        _events.emit(WaterDashboardScreenEvents.OpenAddWaterDialog)
    }

    private fun startLoading() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLoading = true))
    }

    private fun stopLoading() = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(isLoading = false))
    }

    fun onWaterSelected(water: WATER) = viewModelScope.launch {
        user.value?.let { user ->
            startLoading()
            val resource = waterRepo.insertIntoWaterLog(getWaterModelClass(water))
            stopLoading()
            if (resource is Resource.Error)
                _events.emit(WaterDashboardScreenEvents.ShowToast(resource.message))
            else
                _events.emit(WaterDashboardScreenEvents.ShowToast("Water added successfully"))
        }
    }

    private fun collectUserData() = viewModelScope.launch {
        user.collect { user ->
            user?.let {
                _uiState.emit(
                    uiState.value.copy(
                        username = it.username,
                        totalAmount = it.waterLimit,
                    )
                )
            }
        }
    }

    private suspend fun getUserInfo() {
        user.emit(authRepo.getCurrentUser())
    }

    private suspend fun collectWaterLogs() {
        waterRepo.getTodaysWaterLogs().collect { waterLogs ->
            val totalDrinked = waterLogs.sumOf { water ->
                water.quantity.quantity
            }
            val progress = totalDrinked.toFloat() / user.value!!.waterLimit * 100f
            _uiState.emit(
                uiState.value.copy(
                    waterLog = waterLogs,
                    completedAmount = totalDrinked,
                    progress = progress,
                    greeting = getGreeting(progress)
                )
            )
        }
    }

    private fun getWaterModelClass(water: WATER) = Water(
        quantity = water,
        timeStamp = System.currentTimeMillis()
    )
}
