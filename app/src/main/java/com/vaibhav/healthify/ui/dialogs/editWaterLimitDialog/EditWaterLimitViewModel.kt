package com.vaibhav.healthify.ui.dialogs.editWaterLimitDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditWaterLimitViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EditWaterLimitScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EditWaterDialogEvents>()
    val events = _events.asSharedFlow()

    fun onQuantityChange(quantity: Int) = viewModelScope.launch {
        _uiState.emit(
            uiState.value.copy(
                quantitySelected = quantity,
                isButtonEnabled = quantity != 0
            )
        )
    }

    fun onSaveButtonPressed() = viewModelScope.launch {
        _events.emit(EditWaterDialogEvents.NavigateBack(uiState.value.quantitySelected))
    }
}
