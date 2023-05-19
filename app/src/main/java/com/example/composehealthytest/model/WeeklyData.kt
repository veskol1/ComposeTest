package com.example.composehealthytest.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class WeeklyData(
    @SerializedName("weekly_data")
    val weeklyDataList: List<Weekly>
)
@Entity(tableName = "healthy_table")
data class Weekly(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

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