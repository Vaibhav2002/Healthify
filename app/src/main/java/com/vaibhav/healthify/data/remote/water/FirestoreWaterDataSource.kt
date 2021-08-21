package com.vaibhav.healthify.data.remote.water

import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhav.healthify.data.models.remote.WaterDTO
import com.vaibhav.healthify.util.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreWaterDataSource @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val internetChecker: InternetChecker
) : WaterDataSource {

    override suspend fun getAllWaterLogs(email: String): Resource<List<WaterDTO>> = try {
        if (internetChecker.hasInternetConnection()) {
            val waterLog =
                fireStore.collection(USER_COLLECTION).document(email).collection(WATER_COLLECTION)
                    .get()
                    .await()
                    .toObjects(WaterDTO::class.java)
            Resource.Success(waterLog)
        } else
            Resource.Error(
                errorType = ERROR_TYPE.NO_INTERNET,
                message = NO_INTERNET_MESSAGE
            )
    } catch (e: Exception) {
        Resource.Error(e.message.toString())
    }

    override suspend fun addWater(email: String, waterDTO: WaterDTO): Resource<WaterDTO> = try {
        if (internetChecker.hasInternetConnection()) {
            fireStore.collection(USER_COLLECTION).document(email).collection(WATER_COLLECTION)
                .add(waterDTO).await()
            Resource.Success(waterDTO)
        } else
            Resource.Error(
                errorType = ERROR_TYPE.NO_INTERNET,
                message = NO_INTERNET_MESSAGE
            )
    } catch (e: Exception) {
        Resource.Error(e.message.toString())
    }
}
