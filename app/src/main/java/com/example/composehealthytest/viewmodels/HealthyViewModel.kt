package com.example.composehealthytest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composehealthytest.repository.HealthyRepository
import com.example.composehealthytest.util.DateUtil
import com.example.composehealthytest.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthyViewModel @Inject constructor(private val repository: HealthyRepository): ViewModel() {

    private val _uiMainState = MutableStateFlow(
        UiMainScreenState(
            status = Status.LOADING,
            selectedDayIndex = -1,
            weeklyGraphDataList = arrayListOf()
        )
    )

    val uiMainState = _uiMainState.asStateFlow()

    private val _uiTimelineState = MutableStateFlow(UiTimelineScreenState(
        selectedDayIndex = -1,
        monthsTitle = "",
        weeklyDataList = arrayListOf()
    ))

    val uiTimelineState = _uiTimelineState.asStateFlow()

    init {
        viewModelScope.launch {
            when(repository.fetchData()) {
                Status.DONE -> {
                    updateMainScreenState()
                    updateTimelineScreenState()
                }
                Status.ERROR -> {
                    _uiMainState.update {
                        it.copy(status = Status.ERROR)
                    }
                }
                else -> {}
            }
        }
    }

    private fun updateMainScreenState() {
        val weeklyGraphDataList = repository.getMainScreenData()
        _uiMainState.update {
            it.copy(
                status = Status.DONE,
                selectedDayIndex = DateUtil.getNumDayOfWeek(),
                weeklyGraphDataList = weeklyGraphDataList,
            )
        }
    }

    private fun updateTimelineScreenState() {
        val weeklyTimelineData = repository.getTimelineData()
        _uiTimelineState.update {
            it.copy(
                selectedDayIndex = DateUtil.getNumDayOfWeek(),
                monthsTitle = DateUtil.getMonthsTitle(),
                weeklyDataList = weeklyTimelineData,
            )
        }
    }

    data class UiMainScreenState(
        val status: Status,
        val selectedDayIndex: Int,
        val weeklyGraphDataList: ArrayList<HealthyRepository.GraphBarData>
    )

    data class UiTimelineScreenState(
        val selectedDayIndex: Int,
        val monthsTitle: String,
        val weeklyDataList: ArrayList<HealthyRepository.TimelineRowData>,
    )
}