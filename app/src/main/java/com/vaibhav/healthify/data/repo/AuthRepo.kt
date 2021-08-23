package com.vaibhav.healthify.data.repo

import com.auth0.android.result.UserProfile
import com.vaibhav.healthify.data.models.local.User
import com.vaibhav.healthify.data.models.mapper.UserMapper
import com.vaibhav.healthify.data.models.remote.UserDTO
import com.vaibhav.healthify.data.remote.auth.FirebaseAuthDataSource
import com.vaibhav.healthify.util.Resource
import com.vaibhav.healthify.util.USER_DOES_NOT_EXIST
import com.vaibhav.healthify.util.getSleepQuantity
import com.vaibhav.healthify.util.getWaterQuantity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    private val preferencesRepo: PreferencesRepo,
    private val userMapper: UserMapper
) {

    companion object {
        private const val USER_NOT_LOGGED_IN = "User is not logged in"
    }

    suspend fun getCurrentUser() = preferencesRepo.getUserData()

    fun getUserDataFlow() = preferencesRepo.getUserDataFlow()

    suspend fun isUserLoggedIn() = getCurrentUser() != null

    suspend fun isUserOnBoardingComplete() = preferencesRepo.getOnBoardingCompleted() != null

    suspend fun isUserDataEntryCompleted() = preferencesRepo.getUserCompletedDataEntry() != null

    suspend fun saveUserOnBoardingCompleted() = preferencesRepo.saveOnBoardingCompleted()

    suspend fun saveUserDataEntryCompleted() = preferencesRepo.saveUserDataCompleted()

    suspend fun isUserRegistered(userProfile: UserProfile): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            val userResource = authDataSource.getUserData(userProfile.email!!)
            return@withContext if (userResource is Resource.Error) {
                if (userResource.message == USER_DOES_NOT_EXIST)
                    Resource.Success(false)
                else
                    Resource.Error(userResource.message, errorType = userResource.errorType)
            } else Resource.Success(true)
        }

    suspend fun continueAfterLogin(userProfile: UserProfile) = withContext(Dispatchers.IO) {
        val userResource = authDataSource.getUserData(userProfile.email.toString())
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
        removeUserDataCompleted()
    }

    suspend fun saveUserName(username: String) = withContext(Dispatchers.IO) {
        val user = getCurrentUser()
        return@withContext user?.let {
            it.username = username
            val resource = authDataSource.saveUserName(username, it.email)
            if (resource is Resource.Success)
                saveUserIntoPreferences(it)
            resource
        } ?: Resource.Error(USER_NOT_LOGGED_IN)
    }

    suspend fun saveUserAge(age: Int) = withContext(Dispatchers.IO) {
        val user = getCurrentUser()
        return@withContext user?.let {
            it.age = age
            it.sleepLimit = age.getSleepQuantity()
            val resource = authDataSource.saveUserAgeAndSleepLimit(it.age, it.sleepLimit, it.email)
            if (resource is Resource.Success)
                saveUserIntoPreferences(it)
            resource
        } ?: Resource.Error(USER_NOT_LOGGED_IN)
    }

    suspend fun saveUserWeight(weight: Int) = withContext(Dispatchers.IO) {
        val user = getCurrentUser()
        return@withContext user?.let {
            it.weight = weight
            it.waterLimit = weight.getWaterQuantity()
            val resource =
                authDataSource.saveUserWeightAndWaterQuantity(weight, it.waterLimit, it.email)
            if (resource is Resource.Success)
                saveUserIntoPreferences(it)
            resource
        } ?: Resource.Error(USER_NOT_LOGGED_IN)
    }

    suspend fun updateUserWaterLimit(limit: Int) = withContext(Dispatchers.IO) {
        return@withContext getCurrentUser()?.let {
            it.waterLimit = limit
            val resource = authDataSource.updateUserWaterLimit(it.waterLimit, it.email)
            if (resource is Resource.Success)
                saveUserIntoPreferences(it)
            resource
        } ?: Resource.Error(USER_NOT_LOGGED_IN)
    }

    suspend fun updateUserSleepLimit(limit: Int) = withContext(Dispatchers.IO) {
        return@withContext getCurrentUser()?.let {
            it.sleepLimit = limit
            Timber.d("Editing sleep Limit in REPO")
            val resource = authDataSource.updateUserSleepLimit(it.sleepLimit, it.email)
            if (resource is Resource.Success)
                saveUserIntoPreferences(it)
            resource
        } ?: Resource.Error(USER_NOT_LOGGED_IN)
    }

    suspend fun increaseUserExp(increment: Int) = withContext(Dispatchers.IO) {
        return@withContext getCurrentUser()?.let {
            it.exp += increment
            val resource = authDataSource.increaseUserExp(increment, it.email)
            if (resource is Resource.Success)
                saveUserIntoPreferences(it)
            resource
        } ?: Resource.Error(USER_NOT_LOGGED_IN)
    }

    private suspend fun addUserToFirestore(userDTO: UserDTO): Resource<UserDTO> {
        val resource = authDataSource.saveUserData(userDTO)
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

    private suspend fun removeUserDataCompleted() {
        preferencesRepo.removeUserData()
    }

    private suspend fun removeUserOnBoarding() {
        preferencesRepo.removeOnBoarding()
    }
}
