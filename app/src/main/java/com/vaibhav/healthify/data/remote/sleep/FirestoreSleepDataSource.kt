package com.vaibhav.healthify.data.remote.sleep

import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhav.healthify.data.models.remote.SleepDTO
import com.vaibhav.healthify.util.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreSleepDataSource @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val internetChecker: InternetChecker
) : SleepDataSource {
    override suspend fun getAllSleepLogs(email: String): Resource<List<SleepDTO>> = try {
        if (internetChecker.hasInternetConnection()) {
            val sleepLog =
                fireStore.collection(USER_COLLECTION).document(email).collection(SLEEP_COLLECTION)
                    .get()
                    .await()
                    .toObjects(SleepDTO::class.java)
            Resource.Success(sleepLog)
        } else
            Resource.Error(
                errorType = ERROR_TYPE.NO_INTERNET,
                message = NO_INTERNET_MESSAGE
            )
    } catch (e: Exception) {
        Resource.Error(e.message.toString())
    }

    override suspend fun addSleep(email: String, sleepDTO: SleepDTO): Resource<SleepDTO> = try {
        if (internetChecker.hasInternetConnection()) {
            fireStore.collection(USER_COLLECTION).document(email).collection(SLEEP_COLLECTION)
                .add(sleepDTO).await()
            Resource.Success(sleepDTO)
        } else
            Resource.Error(
                errorType = ERROR_TYPE.NO_INTERNET,
                message = NO_INTERNET_MESSAGE
            )
    } catch (e: Exception) {
        Resource.Error(e.message.toString())
    }
}
