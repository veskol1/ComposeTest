package com.example.composehealthytest.repository

import com.example.composehealthytest.api.Api
import com.example.composehealthytest.model.Weekly
import com.example.composehealthytest.util.DateUtil
import com.example.composehealthytest.util.ShortDay
import com.example.composehealthytest.util.Status
import java.lang.Exception
import javax.inject.Inject

class HealthyRepository @Inject constructor(private val api: Api) {

    private var weeklyResponseDataList: List<Weekly>? = null
    suspend fun fetchData(): Status {
        return try {
            val apiResponse = api.getHealthyData()
            if (apiResponse.isSuccessful && apiResponse.body() != null) {
                weeklyResponseDataList = apiResponse.body()?.weeklyDataList
                Status.DONE
            } else {
                Status.ERROR
            }
        } catch (e: Exception) {
            Status.ERROR
        }
    }

    fun getTimelineData(): ArrayList<TimelineRowData> {
        val weeklyTimelineDataList = ArrayList<TimelineRowData>()

        weeklyResponseDataList!!.forEachIndexed { indexOfDay, it ->
            val stepsActivity = it.dailyItem.dailyActivity
            val stepsGoal = it.dailyItem.dailyGoal
            val progressMadePercent = stepsActivity.toFloat().div(stepsGoal.toFloat()) * CIRCLE_DEGREE

            weeklyTimelineDataList.add(
                TimelineRowData(
                    madeGoal = it.dailyItem.dailyActivity > it.dailyItem.dailyGoal,
                    progressMadePercent = progressMadePercent,
                    stepsActivity = it.dailyItem.dailyActivity,
                    stepsGoal = it.dailyItem.dailyGoal,
                    distance = it.dailyData.distance.toFloat().div(METER_IN_KM),
                    kcal = it.dailyData.kcal,
                    dayName = ShortDay.values()[indexOfDay],
                    dateNum = DateUtil.getDateNumber(indexOfDay)
                )
            )
        }

        return weeklyTimelineDataList
    }

    fun getMainScreenData() = getNormalizedGraphData(weeklyResponseDataList!!)
    private fun getNormalizedGraphData(weeklyDataList: List<Weekly>): ArrayList<GraphBarData> {
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
    ): ArrayList<GraphBarData> {
        val weeklyMainDataList = ArrayList<GraphBarData>()

        weeklyDataList.forEachIndexed { index, it ->
            val percGoalHeight = it.dailyItem.dailyGoal.toFloat().div(maxBarHeight) * BAR_HEIGHT_DP
            val percActivityHeight = it.dailyItem.dailyActivity.toFloat().div(maxBarHeight) * BAR_HEIGHT_DP
            weeklyMainDataList.add(
                GraphBarData(
                    dailyGoalPercent = percGoalHeight.toInt(),
                    dailyActivityPercent = percActivityHeight.toInt(),
                    dayName = ShortDay.values()[index]
                )
            )
        }
        return weeklyMainDataList
    }

    data class GraphBarData(
        val dailyGoalPercent: Int,
        val dailyActivityPercent: Int,
        val dayName: ShortDay,
    )

    data class TimelineRowData(
        val madeGoal: Boolean,
        val progressMadePercent: Float,
        val stepsActivity: String,
        val stepsGoal: String,
        val distance: Float,
        val kcal: String,
        val dayName: ShortDay,
        val dateNum: Int,
    )

    companion object {
        const val BAR_HEIGHT_DP = 150
        const val CIRCLE_DEGREE = 360
        const val METER_IN_KM = 1000
    }
}