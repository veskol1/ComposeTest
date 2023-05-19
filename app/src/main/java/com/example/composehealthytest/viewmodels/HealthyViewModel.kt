package com.example.composehealthytest.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composehealthytest.repository.HealthyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HealthyViewModel @Inject constructor(private val repository: HealthyRepository): ViewModel() {

    private val _uiState = MutableStateFlow(UiMainScreenState(
        selectedDayIndex = 0,
        weeklyDataList = arrayListOf()
    ))

    val uiState = _uiState.asStateFlow()

    init {
        val numDayOfWeek = getNumDayOfWeek()

        viewModelScope.launch {
            val weeklyMainScreenList = repository.getData()
            weeklyMainScreenList?.let {

                _uiState.update {
                    it.copy(
                        selectedDayIndex = numDayOfWeek,
                        weeklyDataList = weeklyMainScreenList,
                    )
                }
            }
        }


//        val daysToAdd = 7 - numDayOfWeek
//
//        val nextMonth = date?.plusDays(daysToAdd.toLong())?.month?.name
//
//        Log.d("haha","monthAfter="+nextMonth)

    }



    fun getData(): String {
        return ""
    }


    private fun getNumDayOfWeek(): Int {
        val date = LocalDate.now()

        return when (date?.dayOfWeek?.name) {
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

    enum class Day(name: String) {
        SUNDAY("SUNDAY"),
        MONDAY("MONDAY"),
        TUESDAY("TUESDAY"),
        WEDNESDAY("WEDNESDAY"),
        THURSDAY("THURSDAY"),
        FRIDAY("FRIDAY"),
        SATURDAY("SATURDAY")
    }

    data class UiMainScreenState(
        val selectedDayIndex: Int,
        val weeklyDataList: ArrayList<HealthyRepository.WeeklyDataMainScreen>
    )


//    data class UiTimelineScreenState(
//        val selectedDayIndex: Int,
//        val weeklyDataList: ArrayList<HealthyRepository.WeeklyDataMainScreen>
//    )



}