package com.vaibhav.healthify.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderboard_table")
data class LeaderBoardItem(
    val userEmail: String,
    val exp: Int,
    val userProfileImage: String,
    val username: String,
    @PrimaryKey
    var id: Int = 0
)
