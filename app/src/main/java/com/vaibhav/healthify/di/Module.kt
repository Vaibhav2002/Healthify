package com.vaibhav.healthify.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.auth0.android.Auth0
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.vaibhav.healthify.data.models.mapper.UserMapper
import com.vaibhav.healthify.util.CLIENT_ID
import com.vaibhav.healthify.util.DATASTORE
import com.vaibhav.healthify.util.DOMAIN_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Module {

    private val Context.dataStore by preferencesDataStore(DATASTORE)

    @Provides
    fun providesAuth0(): Auth0 = Auth0(CLIENT_ID, DOMAIN_NAME)

    @Provides
    fun providesFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesStorage(): FirebaseStorage = Firebase.storage

    @Provides
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    // mappers

    @Provides
    fun providesUserMapper(): UserMapper = UserMapper()
}
