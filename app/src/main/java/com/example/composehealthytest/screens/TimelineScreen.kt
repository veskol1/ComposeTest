package com.example.composehealthytest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import com.example.composehealthytest.R
import com.example.composehealthytest.repository.HealthyRepository
import com.example.composehealthytest.ui.theme.BlueLightColor
import com.example.composehealthytest.ui.theme.DividerColor
import com.example.composehealthytest.ui.theme.GreenLightColor
import com.example.composehealthytest.ui.theme.LightGrayColor
import com.example.composehealthytest.ui.theme.SuperLightGrayColor
import com.example.composehealthytest.viewmodels.HealthyViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(healthyViewModel: HealthyViewModel, onBackPressed: () -> Unit) {
    val state by healthyViewModel.uiTimelineState.collectAsState()

    Scaffold(
        topBar = { TopBarHealthy(onBackPressed) }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                DateTitle(state.monthsTitle)
                TimelineData(state)
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHealthy(onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.timeline_title), fontWeight = Bold)
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(painterResource(id = R.drawable.back_ic), "back Button")
            }
        },
    )
}

@Composable
fun DateTitle(monthsTitle: String) {
    Row(modifier = Modifier.padding(top = 20.dp, start = 24.dp)) {
        Text(text = monthsTitle, color = LightGrayColor)
    }
}

@Composable
fun TimelineData(state: HealthyViewModel.UiTimelineScreenState) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
        state.weeklyDataList.forEachIndexed { index, rowData ->
            DayDataRow(rowData = rowData, showIndicator = index == state.selectedDayIndex)
        }
    }
}

@Composable
fun DayDataRow(rowData: HealthyRepository.TimelineRowData, showIndicator: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().height(80.dp).padding(end = 24.dp, bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DrawLeftIndicator(showIndicator = showIndicator)
        DateTile(dayName = rowData.dayName.name, rowData.dateNum)
        DividerDraw()
        CircularProgress(goalMade = rowData.madeGoal, progressMade = rowData.progressMadePercent)
        StepsTile(madeGoal = rowData.madeGoal, dataActivity = rowData.stepsActivity, dataGoal = rowData.stepsGoal)
        DistanceTile(madeGoal = rowData.madeGoal, dataKcal = rowData.kcal, dataDistance = rowData.distance)
    }
}

@Composable
fun DrawLeftIndicator(showIndicator: Boolean = false) {
    Canvas(
        modifier = Modifier
            .fillMaxHeight()
            .width(4.dp)
            .progressSemantics()
    ) {
        drawRect(color = BlueLightColor.takeIf { showIndicator } ?: Color.White)
    }
}
@Composable
fun RowScope.DateTile(dayName: String, dateNum: Int) {
    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = dateNum.toString(), fontWeight = Bold)
        Text(text = dayName)
    }
}

@Composable
fun RowScope.DividerDraw() {
    Column(modifier = Modifier.weight(0.2f)) {
        Divider(modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
            .background(color = DividerColor))
    }
}

@Composable
fun RowScope.CircularProgress(
    goalMade: Boolean,
    progressMade: Float
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
    }

    Column(modifier = Modifier.weight(1f)) {
        Canvas(
            modifier = Modifier
                .progressSemantics()
                .size(46.dp)
                .padding(6.dp / 2)
        ) {
            drawArc(
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                color = SuperLightGrayColor,
                style = stroke
            )

            drawArc(
                startAngle = -90f,
                sweepAngle = progressMade,
                useCenter = false,
                color = GreenLightColor.takeIf { goalMade } ?: BlueLightColor,
                style = stroke
            )
        }
    }
}

@Composable
fun RowScope.StepsTile(madeGoal: Boolean, dataActivity: String, dataGoal: String) {
    Column (modifier = Modifier.weight(2f)){
        Text(text = stringResource(R.string.steps))
        Row {
            Text(text = dataActivity, color = GreenLightColor.takeIf { madeGoal } ?: BlueLightColor)
            Text(text = "/$dataGoal")
        }
    }
}

@Composable
fun RowScope.DistanceTile(madeGoal: Boolean, dataKcal: String, dataDistance: Float) {
    Column (modifier = Modifier.weight(2f)){
        DrawCircleDistance(madeGoal = madeGoal, data = "$dataKcal KCAL")
        DrawCircleDistance(madeGoal = madeGoal, data = "$dataDistance KM")
    }
}

@Composable
fun DrawCircleDistance(madeGoal: Boolean, data: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(14.dp), onDraw = {
            drawCircle(color = LightGrayColor.takeIf { madeGoal.not() } ?: GreenLightColor)
        })
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = data)
    }
}


//@Preview(showBackground = true)
//@Composable
//fun TimelineScreenPreview() {
//    TimelineScreen(null, {})
//}