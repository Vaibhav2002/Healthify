package com.vaibhav.healthify.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.data.repo.WaterRepo
import com.vaibhav.healthify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val waterRepo: WaterRepo,
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _events = MutableSharedFlow<MainActivityScreenEvents>()
    val events: SharedFlow<MainActivityScreenEvents> = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadAllWaterLogs()
    }

    private fun loadAllWaterLogs() = viewModelScope.launch {
        _isLoading.emit(true)
        val resource = waterRepo.fetchAllWaterLogs()
        _isLoading.emit(false)
        if (resource is Resource.Error)
            _events.emit(MainActivityScreenEvents.ShowToast(resource.message))
    }
}
