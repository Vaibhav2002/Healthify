package com.vaibhav.healthify.data.repo

import com.vaibhav.healthify.data.local.dataSource.RoomSleepDataSource
import com.vaibhav.healthify.data.models.local.Sleep
import com.vaibhav.healthify.data.models.mapper.SleepMapper
import com.vaibhav.healthify.data.remote.sleep.FirestoreSleepDataSource
import com.vaibhav.healthify.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class SleepRepo @Inject constructor(
    private val sleepDataSource: RoomSleepDataSource,
    private val fireStoreSleepDataSource: FirestoreSleepDataSource,
    private val authRepo: AuthRepo,
    private val sleepMapper: SleepMapper
) {

    fun getTodaysSleepLogs() =
        sleepDataSource.getAllSleepAfterTime(getTodaysTime()).flowOn(Dispatchers.IO)

    fun getAllSleepsOfLastWeek() = sleepDataSource.getAllSleepAfterTime(getTimeOfLastWeek())
        .flowOn(Dispatchers.IO)

    suspend fun fetchAllSleepLogs(): Resource<Unit> = withContext(Dispatchers.IO) {
        return@withContext authRepo.getCurrentUser()?.let {
            val resource = fireStoreSleepDataSource.getAllSleepLogs(it.email)
            if (resource is Resource.Success) {
                dumpNewSleepLogsDataIntoDb(sleepMapper.toEntityList(resource.data!!))
                Resource.Success<Unit>()
            } else Resource.Error(resource.message, errorType = resource.errorType)
        } ?: Resource.Error(USER_DOES_NOT_EXIST)
    }

    suspend fun insertIntoSleepLog(sleep: Sleep): Resource<Unit> = withContext(Dispatchers.IO) {
        return@withContext authRepo.getCurrentUser()?.let {
            val sleepDTO = sleepMapper.toDTO(sleep)
            sleepDTO.userEmail = it.email
            val resource = fireStoreSleepDataSource.addSleep(it.email, sleepDTO)
            if (resource is Resource.Success) {
                insertSleepIntoDb(listOf(sleep))
                Resource.Success<Unit>()
            } else Resource.Error(resource.message, errorType = resource.errorType)
        } ?: Resource.Error(USER_DOES_NOT_EXIST)
    }

    fun getFOTD(): String {
        val day = getTodayDayNo()
        return sleepFOTD[day]
    }

    private suspend fun insertSleepIntoDb(sleeps: List<Sleep>) {
        sleepDataSource.insertSleep(sleeps)
    }

    private suspend fun dumpNewSleepLogsDataIntoDb(sleeps: List<Sleep>) {
        deleteAllSleepLogs()
        insertSleepIntoDb(sleeps)
    }

    private suspend fun deleteAllSleepLogs() = sleepDataSource.deleteAll()
}
