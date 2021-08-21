package com.vaibhav.healthify.data.repo

import com.vaibhav.healthify.data.local.dataSource.LeaderboardDataSource
import com.vaibhav.healthify.data.models.local.LeaderBoardItem
import com.vaibhav.healthify.data.models.mapper.LeaderBoardItemMapper
import com.vaibhav.healthify.data.remote.auth.FirebaseAuthDataSource
import com.vaibhav.healthify.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LeaderboardRepo @Inject constructor(
    private val leaderboardDataSource: LeaderboardDataSource,
    private val authDataSource: FirebaseAuthDataSource,
    private val leaderBoardItemMapper: LeaderBoardItemMapper
) {

    suspend fun getUserRank(email: String) = leaderboardDataSource.getUserRank(email)

    fun getLeaderboard() = leaderboardDataSource.getLeaderBoard()

    suspend fun fetchLeaderBoard(): Resource<Unit> = withContext(Dispatchers.IO) {
        val resource = authDataSource.fetchAllUsers()
        return@withContext if (resource is Resource.Success && resource.data != null) {
            dumpAllLeaderBoardUsersIntoDB(leaderBoardItemMapper.toEntityList(resource.data!!))
            Resource.Success()
        } else {
            Resource.Error(errorType = resource.errorType, message = "Cannot load leaderboard")
        }
    }

    private suspend fun dumpAllLeaderBoardUsersIntoDB(leaderBoardItems: List<LeaderBoardItem>) {
        deleteAllFromLeaderBoardDB()
        val leaderBoard = leaderBoardItems.sortedByDescending {
            it.exp
        }
        var index = 1
        leaderBoard.forEach {
            it.id = index
            index++
        }
        leaderboardDataSource.insertAll(leaderBoard)
    }

    private suspend fun deleteAllFromLeaderBoardDB() {
        leaderboardDataSource.deleteAll()
    }
}
