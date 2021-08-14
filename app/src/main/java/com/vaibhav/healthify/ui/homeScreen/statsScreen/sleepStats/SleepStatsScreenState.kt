package com.vaibhav.healthify.ui.homeScreen.statsScreen.sleepStats

data class SleepStatsScreenState(
    val weeklyPercentage: Float = 0F,
    val expGained: Long = 0L,
    val weekDate: String = "",
    val barChartData: List<Pair<String, Float>> = mutableListOf(),
    val lineChartData: List<Pair<String, Float>> = mutableListOf()
)
