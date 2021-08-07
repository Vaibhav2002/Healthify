package com.vaibhav.healthify.data.remote.auth

import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhav.healthify.data.models.remote.UserDTO
import com.vaibhav.healthify.util.Resource
import com.vaibhav.healthify.util.USER_DOES_NOT_EXIST
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val fireStore: FirebaseFirestore
) : AuthDataSource {

    companion object {
        private const val USER_COLLECTION = "users"
    }

    override suspend fun getUserData(email: String): Resource<UserDTO> =
        try {
            val user = fireStore.collection(USER_COLLECTION).document(email).get().await()
                .toObject(UserDTO::class.java)
            user?.let {
                Resource.Success(user)
            } ?: Resource.Error(message = USER_DOES_NOT_EXIST)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }

    override suspend fun saveUserData(user: UserDTO): Resource<UserDTO> =
        try {
            fireStore.collection(USER_COLLECTION).document(user.email)
                .set(user).await()
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }

    override suspend fun saveUserName(username: String, email: String): Resource<Unit> =
        try {
            fireStore.collection(USER_COLLECTION).document(email).update("username", username)
                .await()
            Resource.Success()
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }

    override suspend fun saveUserAgeAndWeight(
        age: Int,
        weight: Float,
        email: String
    ): Resource<Unit> =
        try {
            fireStore.collection(USER_COLLECTION).document(email)
                .update("age", age, "weight", weight).await()
            Resource.Success()
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
}
