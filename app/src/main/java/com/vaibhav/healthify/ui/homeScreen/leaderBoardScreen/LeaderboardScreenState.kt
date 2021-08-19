package com.vaibhav.healthify.ui.homeScreen.leaderBoardScreen

import com.vaibhav.healthify.data.models.local.LeaderBoardItem

data class LeaderboardScreenState(
    val leaderboardItems: List<LeaderBoardItem> = emptyList()
)
