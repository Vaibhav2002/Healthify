package com.vaibhav.healthify.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.auth0.android.Auth0
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.vaibhav.healthify.data.local.room.HealthifyDB
import com.vaibhav.healthify.data.local.room.LeaderBoardDao
import com.vaibhav.healthify.data.local.room.SleepDao
import com.vaibhav.healthify.data.local.room.WaterDao
import com.vaibhav.healthify.data.models.local.Sleep
import com.vaibhav.healthify.data.models.local.Water
import com.vaibhav.healthify.data.models.mapper.LeaderBoardItemMapper
import com.vaibhav.healthify.data.models.mapper.SleepMapper
import com.vaibhav.healthify.data.models.mapper.UserMapper
import com.vaibhav.healthify.data.models.mapper.WaterMapper
import com.vaibhav.healthify.util.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun providesRoomDb(@ApplicationContext context: Context): HealthifyDB =
        Room.databaseBuilder(context, HealthifyDB::class.java, "Healthify_DB")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesWaterDao(roomDatabase: HealthifyDB): WaterDao = roomDatabase.getWaterDao()

    @Provides
    fun providesSleepDao(roomDatabase: HealthifyDB): SleepDao = roomDatabase.getSleepDao()

    @Provides
    fun providesLeaderBoardDao(roomDatabase: HealthifyDB): LeaderBoardDao =
        roomDatabase.getLeaderBoardDao()

    // mappers

    @Provides
    fun providesUserMapper(): UserMapper = UserMapper()

    @Provides
    fun providesWaterMapper(): WaterMapper = WaterMapper()

    @Provides
    fun providesSleepMapper(): SleepMapper = SleepMapper()

    @Provides
    fun providesLeaderBoardMapper(): LeaderBoardItemMapper = LeaderBoardItemMapper()

    // Chart DataOrganizer
    @Provides
    fun providesWaterChartDataOrganizer(): ChartDataOrganizer<Water> = ChartDataOrganizer()

    @Provides
    fun providesSleepChartDataOrganizer(): ChartDataOrganizer<Sleep> = ChartDataOrganizer()

    @Provides
    fun providesInternetChecker(@ApplicationContext context: Context): InternetChecker =
        InternetChecker(context)
}
