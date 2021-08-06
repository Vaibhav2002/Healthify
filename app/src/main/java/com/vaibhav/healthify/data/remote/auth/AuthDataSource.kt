package com.vaibhav.healthify.data.remote.auth

import com.vaibhav.healthify.data.models.remote.UserDTO
import com.vaibhav.healthify.util.Resource

interface AuthDataSource {

    suspend fun getUserDataFromFirestore(email: String): Resource<UserDTO>

    suspend fun saveUserDataInFireStore(user: UserDTO): Resource<UserDTO>
}
