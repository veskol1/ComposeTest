package com.example.composehealthytest.api

import com.example.composehealthytest.constants.Constants.API_URL
import com.example.composehealthytest.model.WeeklyData
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET(API_URL)
    suspend fun getHealthyData() : Response<WeeklyData>
}