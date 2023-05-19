package com.example.composehealthytest.viewmodels

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

    private val _uiState = MutableStateFlow(
        UiMainScreenState(
            status = Status.LOADING,
            selectedDayIndex = 0,
            weeklyGraphDataList = arrayListOf()
        )
    )

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when(repository.fetchData()) {
                Status.DONE -> {
                    updateMainScreenState()
                }
                Status.ERROR -> {
                    _uiState.update {
                        it.copy(status = Status.ERROR)
                    }
                }
                else -> {}
            }
        }


//        val daysToAdd = 7 - numDayOfWeek
//
//        val nextMonth = date?.plusDays(daysToAdd.toLong())?.month?.name
//
//        Log.d("haha","monthAfter="+nextMonth)

    }

    private fun updateMainScreenState() {
        val numDayOfWeek = getNumDayOfWeek()
        val weeklyGraphDataList = repository.getMainScreenData()
        _uiState.update {
            it.copy(
                status = Status.DONE,
                selectedDayIndex = numDayOfWeek,
                weeklyGraphDataList = weeklyGraphDataList,
            )
        }
    }


    fun getTimeLineData(): String {
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

    enum class Day {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }

    enum class Status {
        DONE,
        LOADING,
        ERROR,
    }

    data class UiMainScreenState(
        val status: Status,
        val selectedDayIndex: Int,
        val weeklyGraphDataList: ArrayList<HealthyRepository.WeeklyDataMainScreen>
    )

    data class UiTimelineScreenState(
        val selectedDayIndex: Int,
        val weeklyDataList: ArrayList<HealthyRepository.WeeklyDataTimelineScreen>,
    )
}