package com.example.composehealthytest.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.composehealthytest.model.Weekly

@Database(entities = [Weekly::class], version = 1)
@TypeConverters(Converters::class)
abstract class HealthyDatabase: RoomDatabase() {
    abstract fun healthyDao(): HealthyDao

}