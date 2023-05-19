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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composehealthytest.R
import com.example.composehealthytest.ui.theme.BackgroundWhite
import com.example.composehealthytest.ui.theme.BlueLightColor
import com.example.composehealthytest.ui.theme.DividerColor
import com.example.composehealthytest.ui.theme.GreenLightColor
import com.example.composehealthytest.ui.theme.DarkGrayColor
import com.example.composehealthytest.ui.theme.TitleColor
import com.example.composehealthytest.viewmodels.HealthyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(healthyViewModel: HealthyViewModel?, onTimelineClicked: () -> Unit) {
    Scaffold(
        topBar = { MainScreenTopBar() }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                TopScreenData()
                Divider(modifier = Modifier.padding(vertical = 16.dp), color = DividerColor)
                WeeklyTile(onTimelineClicked = onTimelineClicked)
                Divider(color = DividerColor)
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar() {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.daily_activity_title), color = TitleColor, fontSize = 20.sp)
        }
    )
}

@Composable
fun TopScreenData() {
    Column(modifier = Modifier.padding(top = 16.dp, start = 24.dp)) {
        Text(text = stringResource(R.string.daily_goal_title), color = Color.Black, fontSize = 16.sp)
        Text(
            text = stringResource(R.string.daily_goal_subtitle),
            color = DarkGrayColor,
            fontSize = 12.sp
        )
    }
}

@Composable
fun WeeklyTile(onTimelineClicked: () -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp, start = 24.dp, end = 24.dp)) {
        TitleWeeklyAndButton(onTimelineScreen = onTimelineClicked)
        Spacer(modifier = Modifier.height(4.dp))
        WeeklyIcons()
        WeeklyGraph()
    }
}

@Composable
fun TitleWeeklyAndButton(onTimelineScreen: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(text = stringResource(R.string.weekly_progress_title), fontSize = 16.sp)
            Text(text = stringResource(R.string.weekly_progress_subtitle), fontSize = 12.sp, color = DarkGrayColor)
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
fun WeeklyGraph() {
    Row(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Bar(dayOfWeek = "Mon", dayName = "Sun", activityGoal = "80", dailyGoal = "100")
        Bar(dayOfWeek = "Mon", dayName = "Mon", activityGoal = "80", dailyGoal = "100")
        Bar(dayOfWeek = "Mon", dayName = "Tue", activityGoal = "60", dailyGoal = "100")
        Bar(dayOfWeek = "Mon", dayName = "Wed", activityGoal = "80", dailyGoal = "100")
        Bar(dayOfWeek = "Mon", dayName = "Thu", activityGoal = "40", dailyGoal = "100")
        Bar(dayOfWeek = "Mon", dayName = "Fri", activityGoal = "80", dailyGoal = "90")
        Bar(dayOfWeek = "Mon", dayName = "Sat", activityGoal = "80", dailyGoal = "90")
    }

}

@Composable
fun Bar(dayOfWeek: String, dayName: String, activityGoal: String, dailyGoal: String) {
    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Column(
                modifier = Modifier
                    .height(activityGoal.toInt().dp)
                    .width(14.dp)
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = BlueLightColor)
            ) {}
            Column(
                modifier = Modifier
                    .height(dailyGoal.toInt().dp)
                    .width(14.dp)
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
                .background(color = BlueLightColor.takeIf { dayOfWeek == dayName }
                    ?: BackgroundWhite)
        ) {}

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(healthyViewModel = null, onTimelineClicked = {})
}
