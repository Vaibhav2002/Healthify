package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.water

sealed class WaterDashboardScreenEvents {
    data class ShowToast(val message: String) : WaterDashboardScreenEvents()
    object OpenAddWaterDialog : WaterDashboardScreenEvents()
    object CreateAlarm : WaterDashboardScreenEvents()
    object ShowNoInternetDialog : WaterDashboardScreenEvents()
}
