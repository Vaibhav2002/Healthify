package com.vaibhav.healthify.data.remote.sleep

import com.vaibhav.healthify.data.models.remote.SleepDTO
import com.vaibhav.healthify.util.Resource

interface SleepDataSource {

    suspend fun getAllSleepLogs(email: String): Resource<List<SleepDTO>>

    suspend fun addSleep(email: String, sleepDTO: SleepDTO): Resource<SleepDTO>
}
