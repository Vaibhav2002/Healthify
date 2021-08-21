package com.vaibhav.healthify.ui.dialogs.editWaterLimitDialog

sealed class EditWaterDialogEvents {
    data class NavigateBack(val quantity: Int) : EditWaterDialogEvents()
}
