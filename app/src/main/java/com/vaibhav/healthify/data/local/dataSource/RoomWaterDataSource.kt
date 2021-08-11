package com.vaibhav.healthify.data.local.dataSource

import com.vaibhav.healthify.data.local.room.WaterDao
import com.vaibhav.healthify.data.models.local.Water
import javax.inject.Inject

class RoomWaterDataSource @Inject constructor(private val waterDao: WaterDao) {

    fun getAllWaterLogsAfterTime(time: Long) = waterDao.getAllAfterTime(time)

    suspend fun insertWater(water: List<Water>) = waterDao.insertWater(water)

    suspend fun deleteAllWaterLogs() = waterDao.deleteAll()
}
