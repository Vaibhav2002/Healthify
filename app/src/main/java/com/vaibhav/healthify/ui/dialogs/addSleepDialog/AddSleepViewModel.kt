package com.vaibhav.healthify.ui.dialogs.addSleepDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.util.getMinutesFromHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSleepViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AddSleepDialogScreenState())
    val uiState: StateFlow<AddSleepDialogScreenState> = _uiState

    fun onHoursChanged(hours: Int) = viewModelScope.launch {
        _uiState.emit(
            uiState.value.copy(
                hours = hours,
                isButtonEnabled = shouldButtonBeEnabled(hours)
            )
        )
    }

    fun onMinutesChanged(minutes: Int) = viewModelScope.launch {
        _uiState.emit(
            uiState.value.copy(
                minutes = minutes,
                isButtonEnabled = shouldButtonBeEnabled(minutes)
            )
        )
    }

    fun getMinutes(): Int {
        return uiState.value.hours.getMinutesFromHours() + uiState.value.minutes
    }

    fun shouldButtonBeEnabled(value: Int) = value != 0
}
