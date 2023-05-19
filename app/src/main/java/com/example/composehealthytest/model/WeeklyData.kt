package com.example.composehealthytest.model

import com.google.gson.annotations.SerializedName

data class WeeklyData(
    @SerializedName("weekly_data")
    val weeklyDataList: List<Weekly>
)

data class Weekly(
    @SerializedName("daily_item")
    val dailyItem: DailyItem,

    @SerializedName("daily_data")
    val dailyData: DailyData
)

data class DailyItem(
    @SerializedName("daily_goal")
    val dailyGoal: String,

    @SerializedName("daily_activity")
    val dailyActivity: String,
)

data class DailyData(
    @SerializedName("daily_distance_meters")
    val distance: String,

    @SerializedName("daily_kcal")
    val kcal: String,
)