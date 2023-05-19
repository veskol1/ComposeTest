package com.example.composehealthytest.di

import com.example.composehealthytest.api.Api
import com.example.composehealthytest.constants.Constants.API_BASE_URL
import com.example.composehealthytest.repository.HealthyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    @Singleton
    fun provideRepository(api: Api): HealthyRepository = HealthyRepository(api = api)

}