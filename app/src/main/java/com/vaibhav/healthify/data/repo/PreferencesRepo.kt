package com.vaibhav.healthify.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.vaibhav.healthify.data.models.local.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PreferencesRepo @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val USER_KEY = stringPreferencesKey("User")
        private val USER_DATA_KEY = booleanPreferencesKey("UserData")
        private val ON_BOARDING_KEY = booleanPreferencesKey("OnBoarding")
    }

    suspend fun saveUserData(user: User) = withContext(Dispatchers.IO) {
        val userSerialized = Gson().toJson(user)
        dataStore.edit {
            it[USER_KEY] = userSerialized
        }
    }

    suspend fun removeUserData() = withContext(Dispatchers.IO) {
        dataStore.edit {
            it.remove(USER_KEY)
        }
    }

    suspend fun getUserData(): User? = withContext(Dispatchers.IO) {
        return@withContext dataStore.data.map {
            val serializedUser = it[USER_KEY]
            return@map serializedUser?.let { sUser ->
                Gson().fromJson(sUser, User::class.java)
            }
        }.first()
    }

    fun getUserDataFlow() = dataStore.data.map {
        val serializedUser = it[USER_KEY]
        return@map serializedUser?.let { sUser ->
            Gson().fromJson(sUser, User::class.java)
        }
    }

    suspend fun saveUserDataCompleted() = withContext(Dispatchers.IO) {
        dataStore.edit {
            it[USER_DATA_KEY] = true
        }
    }

    suspend fun removeUserDataCompleted() = withContext(Dispatchers.IO) {
        dataStore.edit {
            it.remove(USER_DATA_KEY)
        }
    }

    suspend fun getUserCompletedDataEntry() = dataStore.data.map {
        return@map it[USER_DATA_KEY]
    }.first()

    suspend fun saveOnBoardingCompleted() = withContext(Dispatchers.IO) {
        dataStore.edit {
            it[ON_BOARDING_KEY] = true
        }
    }

    suspend fun removeOnBoarding() = withContext(Dispatchers.IO) {
        dataStore.edit {
            it.remove(ON_BOARDING_KEY)
        }
    }

    suspend fun getOnBoardingCompleted() = dataStore.data.map {
        it[ON_BOARDING_KEY]
    }.first()
}
