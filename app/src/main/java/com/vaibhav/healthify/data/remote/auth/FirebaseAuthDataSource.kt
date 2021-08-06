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
        val USER_COLLECTION = "user"
    }

    override suspend fun getUserDataFromFirestore(email: String): Resource<UserDTO> =
        try {
            val user = fireStore.collection(USER_COLLECTION).document(email).get().await()
                .toObject(UserDTO::class.java)
            user?.let {
                Resource.Success(user)
            } ?: Resource.Error(message = USER_DOES_NOT_EXIST)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }

    override suspend fun saveUserDataInFireStore(user: UserDTO): Resource<UserDTO> =
        try {
            fireStore.collection(USER_COLLECTION).document(user.email)
                .set(user).await()
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
}