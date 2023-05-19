package com.example.composehealthytest.repository

import com.example.composehealthytest.api.Api
import com.example.composehealthytest.model.Weekly
import com.example.composehealthytest.viewmodels.HealthyViewModel
import java.lang.Exception
import javax.inject.Inject

class HealthyRepository @Inject constructor(private val api: Api) {
    private var weeklyDataList: List<Weekly>? = null
    suspend fun fetchData(): HealthyViewModel.Status {
        return try {
            val apiResponse = api.getHealthyData()
            if (apiResponse.isSuccessful && apiResponse.body() != null) {
                weeklyDataList = apiResponse.body()?.weeklyDataList
                HealthyViewModel.Status.DONE
            } else {
                HealthyViewModel.Status.ERROR
            }
        } catch (e: Exception) {
            HealthyViewModel.Status.ERROR
        }
    }

    fun getMainScreenData() = getNormalizedGraphData(weeklyDataList!!)

    fun getTimelineDataList() {

    }

    private fun getNormalizedGraphData(weeklyDataList: List<Weekly>): ArrayList<WeeklyDataMainScreen> {
        val maxBarHeight = findMaxData(weeklyDataList)
        return getNormalizedGraphDataList(maxBarHeight, weeklyDataList)
    }

    private fun findMaxData(dataList: List<Weekly>): Int {
        val dailyGoalsDataList = dataList.map { it.dailyItem.dailyGoal }.map { it.toInt() }
        val maxDaily = dailyGoalsDataList.max()

        val dailyActivityDataList = dataList.map { it.dailyItem.dailyActivity }.map { it.toInt() }
        val maxActivity = dailyActivityDataList.max()

        return Integer.max(maxDaily, maxActivity)
    }

    private fun getNormalizedGraphDataList(
        maxBarHeight: Int,
        weeklyDataList: List<Weekly>
    ): ArrayList<WeeklyDataMainScreen> {
        val weeklyMainDataList = ArrayList<WeeklyDataMainScreen>()

        weeklyDataList.forEachIndexed { index, it ->
            val percGoalHeight = it.dailyItem.dailyGoal.toFloat().div(maxBarHeight) * BAR_HEIGHT_DP
            val percActivityHeight = it.dailyItem.dailyActivity.toFloat().div(maxBarHeight) * BAR_HEIGHT_DP
            weeklyMainDataList.add(
                WeeklyDataMainScreen(
                    dailyGoalPercent = percGoalHeight.toInt(),
                    dailyActivityPercent = percActivityHeight.toInt(),
                    dayName = ShortDay.values()[index]
                )
            )
        }
        return weeklyMainDataList
    }

    data class WeeklyDataTimelineScreen(
        val monthsTitle: String,
        val madeGoal: Boolean,
        val successPercentIndicator: Int,
        val stepsActivity: String,
        val stepsGoal: String,
        val distance: String,
        val kcal: String,
        val dayName: ShortDay,
        val dateNum: Int,
    )

    data class WeeklyDataMainScreen(
        val dailyGoalPercent: Int,
        val dailyActivityPercent: Int,
        val dayName: ShortDay,
    )

    enum class ShortDay {
        Sun,
        Mon,
        Tue,
        Wed,
        Thu,
        Fri,
        Sat
    }

    companion object {
        const val BAR_HEIGHT_DP = 150
    }
}