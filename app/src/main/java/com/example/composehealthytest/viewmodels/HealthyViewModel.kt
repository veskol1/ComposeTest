package com.example.composehealthytest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composehealthytest.model.Weekly
import com.example.composehealthytest.repository.HealthyRepository
import com.example.composehealthytest.util.DateUtil
import com.example.composehealthytest.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthyViewModel @Inject constructor(private val repository: HealthyRepository): ViewModel() {

    private val _uiMainState = MutableStateFlow(
        UiMainScreenState(
            status = Status.LOADING,
            selectedDayIndex = -1,
            weeklyGraphDataList = arrayListOf(),
            weeklyDataList = arrayListOf()
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
            repository.getFetchedData().collectLatest { fetchedData: List<Weekly> ->
                if (fetchedData.isNotEmpty()) {
                    updateMainScreenState(fetchedData)
                    updateTimelineScreenState(fetchedData)
                }
            }
        }
    }

    private fun updateMainScreenState(fetchedData: List<Weekly>) {
        val weeklyGraphDataList = repository.getMainScreenData(fetchedData)
        _uiMainState.update {
            it.copy(
                status = Status.DONE,
                selectedDayIndex = DateUtil.getNumDayOfWeek(),
                weeklyGraphDataList = weeklyGraphDataList,
                weeklyDataList = repository.getTimelineData(fetchedData)
            )
        }
    }

    private fun updateTimelineScreenState(fetchedData: List<Weekly>) {
        val weeklyTimelineData = repository.getTimelineData(fetchedData)
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
        val weeklyGraphDataList: ArrayList<HealthyRepository.GraphBarData>,
        val weeklyDataList: ArrayList<HealthyRepository.TimelineRowData>
    )

    data class UiTimelineScreenState(
        val selectedDayIndex: Int,
        val monthsTitle: String,
        val weeklyDataList: ArrayList<HealthyRepository.TimelineRowData>,
    )
}