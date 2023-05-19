package com.example.composehealthytest.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composehealthytest.model.Weekly
import com.example.composehealthytest.repository.HealthyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Integer.max
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HealthyViewModel @Inject constructor(private val repository: HealthyRepository): ViewModel() {

    private var date :LocalDate? = null

    private val shortDaysList = listOf(
        ShortDay.SUN.name,
        ShortDay.MON.name,
        ShortDay.TUE.name,
        ShortDay.WED.name,
        ShortDay.THU.name,
        ShortDay.FRI.name,
        ShortDay.SAT.name
    )

    private val _uiState = MutableStateFlow(UiMainScreenState(
        selectedDayIndex = 0,
        weeklyDataList = emptyList(),
        WeeklyPercentilesList = arrayListOf(),
        shortDayNamesList = shortDaysList,
        maxBarHeight = 100
    ))

    val uiState = _uiState.asStateFlow()

    init {
        date = LocalDate.now()
        val day = date?.dayOfWeek?.name
        Log.d("haha","data="+date?.dayOfWeek?.name)
        val numDayOfWeek = getNumDayOfWeek(day)

        viewModelScope.launch {
            val dataList = repository.getData()
            dataList?.let {
                val maxBarHeight = findMax(dataList)
                val listPercentilesList = getPercentilesList(maxBarHeight, dataList)


                _uiState.update {
                    it.copy(
                        selectedDayIndex = numDayOfWeek,
                        weeklyDataList = dataList,
                        WeeklyPercentilesList = listPercentilesList
                    )
                }
            }
        }


        val daysToAdd = 7 - numDayOfWeek

        val nextMonth = date?.plusDays(daysToAdd.toLong())?.month?.name

        Log.d("haha","monthAfter="+nextMonth)

    }

    private fun getPercentilesList(maxBarHeight: Int, dataList: List<Weekly>): ArrayList<WeeklyPercentile> {
        var lista = ArrayList<WeeklyPercentile>()

        dataList.forEach {
            val percGoal = it.dailyItem.dailyGoal.toInt().div(maxBarHeight)*100
            val percActivity = it.dailyItem.dailyActivity.toInt().div(maxBarHeight)*100
            lista.add(WeeklyPercentile(percGoal, percActivity))
        }
        return lista
    }

    private fun findMax(dataList: List<Weekly>): Int {
        val dailyGoalsList = dataList.map { it.dailyItem.dailyGoal}.map { it.toInt() }
        val maxDaily = dailyGoalsList.max()

        val dailyActivityList = dataList.map { it.dailyItem.dailyActivity}.map { it.toInt() }
        val maxActivity = dailyActivityList.max()

        val max = max(maxDaily, maxActivity)

        return max
    }

    fun getData(): String {
        return ""
    }

    private fun getNumDayOfWeek(day: String?): Int {
        return when (day) {
            Day.SUNDAY.name -> 1
            Day.MONDAY.name -> 2
            Day.TUESDAY.name -> 3
            Day.WEDNESDAY.name -> 4
            Day.THURSDAY.name -> 5
            Day.FRIDAY.name -> 6
            Day.SATURDAY.name -> 7
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

    enum class ShortDay(name: String) {
        SUN("Sun"),
        MON("Mon"),
        TUE("Tue"),
        WED("Wed"),
        THU("Thu"),
        FRI("Fri"),
        SAT("Sat")
    }
    data class UiMainScreenState(
        val selectedDayIndex : Int,
        val weeklyDataList: List<Weekly>,
        val WeeklyPercentilesList: ArrayList<WeeklyPercentile>,
        val shortDayNamesList: List<String>,
        val maxBarHeight: Int,

        )

    data class WeeklyPercentile(
        val dailyGoal: Int,
        val dailyActivity: Int,
    )

//    data class UiTimelineScreenState(
//
//    )



}