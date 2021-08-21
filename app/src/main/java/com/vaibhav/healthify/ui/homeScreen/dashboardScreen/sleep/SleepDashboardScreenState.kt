package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.sleep

import com.vaibhav.healthify.data.models.local.Sleep

data class SleepDashboardScreenState(
    val username: String = "",
    val mainGreeting: String = "",
    val greeting: String = "",
    val completedAmount: Float = 0F,
    val totalAmount: Float = 0F,
    val progress: Float = 0F,
    val factOfTheDay: String = "",
    val isLoading: Boolean = false,
    val isAddSleepButtonEnabled: Boolean = true,
    val sleepLog: List<Sleep> = emptyList()
)
