package com.vaibhav.healthify.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vaibhav.healthify.R

@Entity(tableName = "sleep_table")
data class Sleep(
    val sleepDuration: Int,
    val timeStamp: Long,
    val sleepImage: Int = R.drawable.sleep,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
