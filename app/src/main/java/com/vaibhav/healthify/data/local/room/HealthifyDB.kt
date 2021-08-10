package com.vaibhav.healthify.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vaibhav.healthify.data.models.local.Water

@Database(entities = arrayOf(Water::class), version = 1, exportSchema = false)
abstract class HealthifyDB : RoomDatabase() {

    abstract fun getWaterDao(): WaterDao
}
