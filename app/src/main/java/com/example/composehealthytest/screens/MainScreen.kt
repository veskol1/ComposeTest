package com.example.composehealthytest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composehealthytest.R
import com.example.composehealthytest.ui.theme.BackgroundWhite
import com.example.composehealthytest.ui.theme.BlueLightColor
import com.example.composehealthytest.ui.theme.DividerColor
import com.example.composehealthytest.ui.theme.GreenLightColor
import com.example.composehealthytest.ui.theme.DarkGrayColor
import com.example.composehealthytest.ui.theme.TitleColor
import com.example.composehealthytest.util.Status
import com.example.composehealthytest.viewmodels.HealthyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(healthyViewModel: HealthyViewModel, onTimelineClicked: () -> Unit) {
    val mainScreenState by healthyViewModel.uiMainState.collectAsState()

    Scaffold(
        topBar = { MainScreenTopBar() }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                when (mainScreenState.status) {
                    Status.DONE -> {
                        TopScreenData()
                        Divider(modifier = Modifier.padding(vertical = 16.dp), color = DividerColor)
                        WeeklyTile(mainScreenState = mainScreenState, onTimelineClicked = onTimelineClicked)
                        Divider(color = DividerColor)
                    }
                    Status.LOADING -> {
                        LoadingScreen()
                    }
                    Status.ERROR -> {
                        ErrorScreen()
                    }
                }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.daily_activity_title),
                color = TitleColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    )
}

@Composable
fun TopScreenData() {
    Column(modifier = Modifier.padding(top = 16.dp, start = 24.dp)) {
        Text(
            text = stringResource(R.string.daily_goal_title),
            color = Color.Black,
            fontSize = 16.sp
        )
        Text(
            text = stringResource(R.string.daily_goal_subtitle),
            color = DarkGrayColor,
            fontSize = 12.sp
        )
    }
}

@Composable
fun WeeklyTile(mainScreenState: HealthyViewModel.UiMainScreenState, onTimelineClicked: () -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp)) {
        TitleWeeklyAndButton(onTimelineScreen = onTimelineClicked)
        Spacer(modifier = Modifier.height(4.dp))
        WeeklyIcons()
        WeeklyGraph(mainScreenState)
    }
}

@Composable
fun TitleWeeklyAndButton(onTimelineScreen: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = stringResource(R.string.weekly_progress_title), fontSize = 16.sp)
            Text(
                text = stringResource(R.string.weekly_progress_subtitle),
                fontSize = 12.sp,
                color = DarkGrayColor
            )
        }
        OutlinedButton(onClick = { onTimelineScreen() }) {
            Text(text = stringResource(R.string.button_timeline))
        }
    }
}

@Composable
fun WeeklyIcons() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(12.dp), onDraw = {
            drawCircle(color = BlueLightColor)
        })
        Text(
            text = stringResource(R.string.activity),
            modifier = Modifier.padding(start = 3.dp),
            color = DarkGrayColor,
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Canvas(modifier = Modifier.size(12.dp), onDraw = {
            drawCircle(color = GreenLightColor)
        })
        Text(
            text = stringResource(R.string.daily_goal),
            modifier = Modifier.padding(start = 3.dp),
            color = DarkGrayColor,
            fontSize = 10.sp
        )
    }
}

@Composable
fun WeeklyGraph(mainScreenState: HealthyViewModel.UiMainScreenState) {
    Row(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        mainScreenState.weeklyGraphDataList.forEachIndexed { index, it ->
            Bar(
                dayName = it.dayName.name,
                activityGoal = it.dailyActivityPercent,
                dailyGoal = it.dailyGoalPercent,
                selectedDay = index == mainScreenState.selectedDayIndex
            )
        }
    }

}

@Composable
fun Bar(dayName: String, activityGoal: Int, dailyGoal: Int, selectedDay: Boolean = false) {
    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Column(
                modifier = Modifier
                    .height(activityGoal.dp)
                    .width(12.dp)
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = BlueLightColor)
            ) {}
            Column(
                modifier = Modifier
                    .height(dailyGoal.dp)
                    .width(12.dp)
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = GreenLightColor)
            ) {}
        }
        Row(modifier = Modifier.width(28.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = dayName, fontSize = 12.sp)
        }
        Row(
            modifier = Modifier
                .height(4.dp)
                .width(28.dp)
                .background(color = BlueLightColor.takeIf { selectedDay }
                    ?: BackgroundWhite)
        ) {}
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MainScreenPreview() {
//    MainScreen(healthyViewModel = null, onTimelineClicked = {})
//}
