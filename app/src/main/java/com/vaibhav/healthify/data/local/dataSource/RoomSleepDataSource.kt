package com.vaibhav.healthify.data.local.dataSource

import com.vaibhav.healthify.data.local.room.SleepDao
import com.vaibhav.healthify.data.models.local.Sleep
import javax.inject.Inject

class RoomSleepDataSource @Inject constructor(private val sleepDao: SleepDao) {

    fun getAllSleepAfterTime(time: Long) = sleepDao.getAllAfterTime(time)

    suspend fun insertSleep(sleeps: List<Sleep>) = sleepDao.insertSleep(sleeps)

    suspend fun deleteAll() = sleepDao.deleteAll()
}
