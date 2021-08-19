package com.vaibhav.healthify.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vaibhav.healthify.data.models.local.LeaderBoardItem
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaderBoardDao {

    @Insert
    suspend fun insertAllItems(leaderBoardItem: List<LeaderBoardItem>)

    @Query("SELECT * FROM leaderboard_table")
    fun getLeaderBoard(): Flow<List<LeaderBoardItem>>

    @Query("SELECT id FROM leaderboard_table WHERE userEmail = :email")
    suspend fun getUserRank(email: String): Int

    @Query("DELETE FROM leaderboard_table")
    suspend fun deleteAll()
}
