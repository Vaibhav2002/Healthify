package com.vaibhav.healthify.data.remote.sleep

import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhav.healthify.data.models.remote.SleepDTO
import com.vaibhav.healthify.util.Resource
import com.vaibhav.healthify.util.SLEEP_COLLECTION
import com.vaibhav.healthify.util.USER_COLLECTION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreSleepDataSource @Inject constructor(
    private val fireStore: FirebaseFirestore
) : SleepDataSource {
    override suspend fun getAllSleepLogs(email: String): Resource<List<SleepDTO>> = try {
        val sleepLog =
            fireStore.collection(USER_COLLECTION).document(email).collection(SLEEP_COLLECTION).get()
                .await()
                .toObjects(SleepDTO::class.java)
        Resource.Success(sleepLog)
    } catch (e: Exception) {
        Resource.Error(e.message.toString())
    }

    override suspend fun addSleep(email: String, sleepDTO: SleepDTO): Resource<SleepDTO> = try {
        fireStore.collection(USER_COLLECTION).document(email).collection(SLEEP_COLLECTION)
            .add(sleepDTO).await()
        Resource.Success(sleepDTO)
    } catch (e: Exception) {
        Resource.Error(e.message.toString())
    }
}
