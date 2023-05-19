package com.example.composehealthytest.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import java.time.LocalDate


class HealthyViewModel: ViewModel() {

    private var date :LocalDate? = null

    init {
        date = LocalDate.now()
        val day = date?.dayOfWeek?.name
        Log.d("haha","data="+date?.dayOfWeek?.name)
        val numDayOfWeek = getNumDayOfWeek(day)

        val daysToAdd = 7 - numDayOfWeek

        val nextMonth = date?.plusDays(daysToAdd.toLong())?.month?.name

        Log.d("haha","monthAfter="+nextMonth)

    }
    fun getData(): String {
        return ""
    }

    private fun getNumDayOfWeek(day: String?): Int {
        return when (day) {
            DAY.SUNDAY.name -> 1
            DAY.MONDAY.name -> 2
            DAY.TUESDAY.name -> 3
            DAY.WEDNESDAY.name -> 4
            DAY.THURSDAY.name -> 5
            DAY.FRIDAY.name -> 6
            DAY.SATURDAY.name -> 7
            else -> {
                -1
            }
        }
    }

    enum class DAY(name: String) {
        SUNDAY("SUNDAY"),
        MONDAY("MONDAY"),
        TUESDAY("TUESDAY"),
        WEDNESDAY("WEDNESDAY"),
        THURSDAY("THURSDAY"),
        FRIDAY("FRIDAY"),
        SATURDAY("SATURDAY")
    }
}