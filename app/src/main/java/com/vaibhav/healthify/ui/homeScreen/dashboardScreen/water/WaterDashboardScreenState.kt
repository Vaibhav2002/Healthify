package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.water

import com.vaibhav.healthify.data.models.local.Water

data class WaterDashboardScreenState(
    val username: String = "",
    val mainGreeting: String = "",
    val greeting: String = "",
    val completedAmount: Int = 0,
    val totalAmount: Int = 0,
    val progress: Float = 0F,
    val factOfTheDay: String = "",
    val isLoading: Boolean = false,
    val isAddWaterButtonEnabled: Boolean = true,
    val waterLog: List<Water> = emptyList()
)
