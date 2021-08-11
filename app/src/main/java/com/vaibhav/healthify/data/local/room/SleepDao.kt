package com.vaibhav.healthify.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vaibhav.healthify.data.models.local.Sleep
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {

    @Query("SELECT * FROM sleep_table WHERE timeStamp>:time ORDER BY timeStamp DESC")
    fun getAllAfterTime(time: Long): Flow<List<Sleep>>

    @Insert
    suspend fun insertSleep(sleeps: List<Sleep>)

    @Query("DELETE FROM sleep_table")
    suspend fun deleteAll()
}
