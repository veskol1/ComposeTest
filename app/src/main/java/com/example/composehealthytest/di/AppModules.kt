package com.example.composehealthytest.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.composehealthytest.api.Api
import com.example.composehealthytest.constants.Constants.API_BASE_URL
import com.example.composehealthytest.repository.HealthyRepository
import com.example.composehealthytest.room.HealthyDao
import com.example.composehealthytest.room.HealthyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModules {

    @Provides
    @Singleton
    fun provideApi(): Api {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Provides
    fun provideSharedPrefs(application: Application): SharedPreferences {
        return application.getSharedPreferences("date", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRepository(api: Api, healthyDao: HealthyDao, shared: SharedPreferences): HealthyRepository =
        HealthyRepository(api = api, healthyDao = healthyDao, sharedPref = shared)

    @Provides
    fun provideDao(healthyDatabase: HealthyDatabase) : HealthyDao = healthyDatabase.healthyDao()

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): HealthyDatabase {
        return Room.databaseBuilder(appContext,
            HealthyDatabase::class.java, "healthy database name")
            .build()
    }

}