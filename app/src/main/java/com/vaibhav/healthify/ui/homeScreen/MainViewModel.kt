package com.vaibhav.healthify.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.data.repo.SleepRepo
import com.vaibhav.healthify.data.repo.WaterRepo
import com.vaibhav.healthify.util.ERROR_TYPE
import com.vaibhav.healthify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val waterRepo: WaterRepo,
    private val sleepRepo: SleepRepo,
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _events = MutableStateFlow<MainActivityScreenEvents>(MainActivityScreenEvents.Empty)
    val events: StateFlow<MainActivityScreenEvents> = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadAllWaterLogs()
        loadAllSleepLogs()
    }

    private fun loadAllWaterLogs() = viewModelScope.launch {
        startLoading()
        val resource = waterRepo.fetchAllWaterLogs()
        stopLoading()
        if (resource is Resource.Error)
            handleError(resource)
    }

    private fun loadAllSleepLogs() = viewModelScope.launch {
        startLoading()
        val resource = sleepRepo.fetchAllSleepLogs()
        stopLoading()
        if (resource is Resource.Error)
            handleError(resource)
    }

    private suspend fun stopLoading() {
        _isLoading.emit(false)
    }

    private suspend fun startLoading() {
        _isLoading.emit(true)
    }

    private suspend fun handleError(resource: Resource.Error<*>) {
        val event = when (resource.errorType) {
            ERROR_TYPE.NO_INTERNET -> MainActivityScreenEvents.ShowNoInternetDialog
            ERROR_TYPE.UNKNOWN -> MainActivityScreenEvents.ShowToast(resource.message)
        }
        _events.emit(event)
    }
}
