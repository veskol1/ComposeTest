package com.example.composehealthytest.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composehealthytest.model.Weekly

@Dao
interface HealthyDao {
    @Query("SELECT * from healthy_table")
    fun getAll(): List<Weekly>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<Weekly>)

    @Query("DELETE from healthy_table")
    fun deleteAll()
}