package com.example.composehealthytest.util

import java.time.LocalDate

object DateUtil {
    private const val LAST_DAY_OF_WEEK = 7
    private var date: LocalDate = LocalDate.now()

     fun getNumDayOfWeek(): Int {
        return when (date.dayOfWeek?.name) {
            Day.SUNDAY.name -> 0
            Day.MONDAY.name -> 1
            Day.TUESDAY.name -> 2
            Day.WEDNESDAY.name -> 3
            Day.THURSDAY.name -> 4
            Day.FRIDAY.name -> 5
            Day.SATURDAY.name -> 6
            else -> {
                -1
            }
        }
    }

    fun getMonthsTitle(): String {
        val currentMonthName = date.month.name
        val daysToAdd = LAST_DAY_OF_WEEK - getNumDayOfWeek()

        val nextMonthName = date.plusDays(daysToAdd.toLong())?.month?.name
        return if (currentMonthName != nextMonthName)
            "$currentMonthName-$nextMonthName"
        else
            currentMonthName
    }

    fun getDateNumber(indexOfDay: Int): Int {
        val dayOfWeekNumber = getNumDayOfWeek()
        val dayOfMonthNumber = date.dayOfMonth
        return dayOfMonthNumber - dayOfWeekNumber + indexOfDay
    }

}