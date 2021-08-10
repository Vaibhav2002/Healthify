package com.vaibhav.healthify.data.repo

import com.vaibhav.healthify.data.local.dataSource.RoomWaterDataSource
import com.vaibhav.healthify.data.models.local.Water
import com.vaibhav.healthify.data.models.mapper.WaterMapper
import com.vaibhav.healthify.data.remote.water.FirestoreWaterDataSource
import com.vaibhav.healthify.util.Resource
import com.vaibhav.healthify.util.USER_DOES_NOT_EXIST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class WaterRepo @Inject constructor(
    private val waterDataSource: RoomWaterDataSource,
    private val firebaseWaterDataSource: FirestoreWaterDataSource,
    private val authRepo: AuthRepo,
    private val waterMapper: WaterMapper
) {

    private fun getTodaysTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        return calendar.timeInMillis
    }

    fun getTodaysWaterLogs() = waterDataSource.getAllWaterLogsAfterTime(getTodaysTime())

    suspend fun fetchAllWaterLogs(): Resource<Unit> = withContext(Dispatchers.IO) {
        return@withContext authRepo.getCurrentUser()?.let {
            val resource = firebaseWaterDataSource.getAllWaterLogs(it.email)
            if (resource is Resource.Success) {
                insertWaterIntoDb(waterMapper.toEntityList(resource.data!!))
                Resource.Success<Unit>()
            } else Resource.Error(resource.message)
        } ?: Resource.Error(USER_DOES_NOT_EXIST)
    }

    suspend fun insertIntoWaterLog(water: Water): Resource<Unit> = withContext(Dispatchers.IO) {
        return@withContext authRepo.getCurrentUser()?.let {
            val resource = firebaseWaterDataSource.addWater(it.email, waterMapper.toDTO(water))
            if (resource is Resource.Success) {
                insertWaterIntoDb(listOf(water))
                Resource.Success<Unit>()
            } else Resource.Error(resource.message)
        } ?: Resource.Error(USER_DOES_NOT_EXIST)
    }

    private suspend fun insertWaterIntoDb(water: List<Water>) {
        waterDataSource.insertWater(water)
    }
}
