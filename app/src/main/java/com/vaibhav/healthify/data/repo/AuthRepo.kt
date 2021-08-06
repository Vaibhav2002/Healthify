package com.vaibhav.healthify.data.repo

import com.auth0.android.result.UserProfile
import com.vaibhav.healthify.data.models.local.User
import com.vaibhav.healthify.data.models.mapper.UserMapper
import com.vaibhav.healthify.data.models.remote.UserDTO
import com.vaibhav.healthify.data.remote.auth.FirebaseAuthDataSource
import com.vaibhav.healthify.util.Resource
import com.vaibhav.healthify.util.USER_DOES_NOT_EXIST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val userMapper: UserMapper
) {

    suspend fun continueAfterLogin(userProfile: UserProfile) = withContext(Dispatchers.IO) {
        val userResource = authDataSource.getUserDataFromFirestore(userProfile.email.toString())
        return@withContext if (userResource is Resource.Error) {
            if (userResource.message == USER_DOES_NOT_EXIST) addUserToFirestore(
                createUserDTO(userProfile)
            )
            else userResource
        } else {
            saveUserIntoPreferences(userMapper.toEntity(userResource.data!!))
            Resource.Success(userResource.data)
        }
    }

    suspend fun logoutUser() = withContext(Dispatchers.IO) {
        removeUserFromPreferences()
    }

    private suspend fun addUserToFirestore(userDTO: UserDTO): Resource<UserDTO> {
        val resource = authDataSource.saveUserDataInFireStore(userDTO)
        if (resource is Resource.Success) saveUserIntoPreferences(userMapper.toEntity(userDTO))
        return resource
    }

    private fun createUserDTO(userProfile: UserProfile) = UserDTO(
        username = userProfile.name ?: "",
        email = userProfile.email ?: "",
        profileImg = userProfile.pictureURL ?: ""
    )

    private suspend fun saveUserIntoPreferences(user: User) {
        preferencesRepo.saveUserData(user)
    }

    private suspend fun removeUserFromPreferences() {
        preferencesRepo.removeUserData()
    }
}
