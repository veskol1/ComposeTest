package com.example.composehealthytest.repository

import com.example.composehealthytest.api.Api
import com.example.composehealthytest.model.Weekly
import javax.inject.Inject

class HealthyRepository @Inject constructor(private val api: Api) {

    suspend fun getData(): ArrayList<WeeklyDataMainScreen>? {
        val apiResponse = api.getHealthyData()
        return if (apiResponse.isSuccessful) {
            apiResponse.body()?.weeklyDataList
            getNormalizedData(apiResponse.body()!!.weeklyDataList)
        } else {
            null
        }
    }

    private fun getNormalizedData(weeklyDataList: List<Weekly>): ArrayList<WeeklyDataMainScreen> {
        val maxBarHeight = findMaxData(weeklyDataList)
        return getNormalizedDataList(maxBarHeight, weeklyDataList)
    }

    private fun findMaxData(dataList: List<Weekly>): Int {
        val dailyGoalsDataList = dataList.map { it.dailyItem.dailyGoal }.map { it.toInt() }
        val maxDaily = dailyGoalsDataList.max()

        val dailyActivityDataList = dataList.map { it.dailyItem.dailyActivity }.map { it.toInt() }
        val maxActivity = dailyActivityDataList.max()

        return Integer.max(maxDaily, maxActivity)
    }

    private fun getNormalizedDataList(
        maxBarHeight: Int,
        weeklyDataList: List<Weekly>
    ): ArrayList<WeeklyDataMainScreen> {
        val weeklyMainDataList = ArrayList<WeeklyDataMainScreen>()

        weeklyDataList.forEachIndexed { index, it ->
            val percGoalHeight = it.dailyItem.dailyGoal.toFloat().div(maxBarHeight) * BAR_HEIGHT_DP
            val percActivityHeight = it.dailyItem.dailyActivity.toFloat().div(maxBarHeight) * BAR_HEIGHT_DP
            weeklyMainDataList.add(
                WeeklyDataMainScreen(
                    percentileBarData = PercentileBarData(
                        percGoalHeight.toInt(),
                        percActivityHeight.toInt()
                    ),
                    dayName = ShortDay.values()[index]
                )
            )
        }
        return weeklyMainDataList
    }

    data class WeeklyDataMainScreen(
        val percentileBarData: PercentileBarData,
        val dayName: ShortDay,
    )

    data class PercentileBarData(
        val dailyGoal: Int,
        val dailyActivity: Int,
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