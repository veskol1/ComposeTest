package com.example.composehealthytest.room

import androidx.room.TypeConverter
import com.example.composehealthytest.model.DailyData
import com.example.composehealthytest.model.DailyItem
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromDailyItem(dailyItem: DailyItem?): String? {
        return Gson().toJson(dailyItem)
    }

    @TypeConverter
    fun toDailyItem(json: String?): DailyItem? {
        return Gson().fromJson(json, DailyItem::class.java)
    }

    @TypeConverter
    fun fromDailyData(dailyData: DailyData?): String? {
        return Gson().toJson(dailyData)
    }

    @TypeConverter
    fun toDailyData(json: String?): DailyData? {
        return Gson().fromJson(json, DailyData::class.java)
    }
}