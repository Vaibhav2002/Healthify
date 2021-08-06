package com.vaibhav.healthify.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

    suspend fun getUserData(): User = withContext(Dispatchers.IO) {
        return@withContext dataStore.data.map {
            val serializedUser = it[USER_KEY]
            return@map Gson().fromJson(serializedUser, User::class.java)
        }.first()
    }
}
