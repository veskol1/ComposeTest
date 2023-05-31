package com.example.composehealthytest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composehealthytest.R
import com.example.composehealthytest.constants.Constants.BAR_INDEX_4
import com.example.composehealthytest.constants.Constants.BAR_MARGIN_WIDTH
import com.example.composehealthytest.constants.Constants.BAR_WIDTH
import com.example.composehealthytest.constants.Constants.BUBBLE_WIDTH
import com.example.composehealthytest.constants.Constants.GRAPH_BAR_MAX_HEIGHT
import com.example.composehealthytest.constants.Constants.GRAPH_MAX_HEIGHT
import com.example.composehealthytest.constants.Constants.NUM_OF_BARS
import com.example.composehealthytest.ui.theme.BackgroundWhite
import com.example.composehealthytest.ui.theme.BlueLightColor
import com.example.composehealthytest.ui.theme.DarkGrayColor
import com.example.composehealthytest.ui.theme.DividerColor
import com.example.composehealthytest.ui.theme.GreenLightColor
import com.example.composehealthytest.ui.theme.TitleColor
import com.example.composehealthytest.util.Status
import com.example.composehealthytest.util.dpToPx
import com.example.composehealthytest.util.pxToDp
import com.example.composehealthytest.viewmodels.HealthyViewModel
import kotlinx.coroutines.delay


@Composable
fun MainScreen(healthyViewModel: HealthyViewModel, onTimelineClicked: () -> Unit) {
    val mainScreenState by healthyViewModel.uiMainState.collectAsState()

    Scaffold(
        topBar = { MainScreenTopBar() }, content = {
            Column(modifier = Modifier.padding(it).fillMaxSize()) {
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
    var xMarginOfBars by remember { mutableStateOf(0.dp) }
    var yMarginFromTopBar by remember { mutableStateOf(0.dp) }
    var showBubble by remember { mutableStateOf(false) }
    var indexItem by remember { mutableStateOf(0) }
    Box {
        var graphWidth by remember { mutableStateOf(0) }
        Row(
            modifier = Modifier
                .height(GRAPH_MAX_HEIGHT)
                .fillMaxWidth()
                .onGloballyPositioned {
                    graphWidth = it.size.width
                },
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            mainScreenState.weeklyGraphDataList.forEachIndexed { index, it ->
                Bar(
                    dayName = it.dayName.name,
                    activityGoal = it.dailyActivityPercent,
                    dailyGoal = it.dailyGoalPercent,
                    selectedDay = index == mainScreenState.selectedDayIndex,
                    onBarClicked = { blueBarHeight: Dp ->
                        showBubble = showBubble != true

                            xMarginOfBars = (index * BAR_MARGIN_WIDTH).dp

                            yMarginFromTopBar = if (blueBarHeight < 10.dp) {
                                GRAPH_BAR_MAX_HEIGHT - 50.dp
                            } else {
                                GRAPH_BAR_MAX_HEIGHT - blueBarHeight - 20.dp
                            }
                            indexItem = index
                    }
                )
            }
        }

        LaunchedEffect(key1 = showBubble) {
                delay(2000)
                showBubble = false
            }

        if (showBubble) {
            val stepsActivity = mainScreenState.weeklyDataList[indexItem].stepsActivity
            val stepsGoal = mainScreenState.weeklyDataList[indexItem].stepsGoal

            val calcXOffset = calcBubbleXOffset(graphWidth, indexItem, xMarginOfBars)

            Box(modifier = Modifier.offset(
                x = calcXOffset.takeIf { indexItem < BAR_INDEX_4 }
                    ?: (calcXOffset - BUBBLE_WIDTH),
                y = yMarginFromTopBar
            )) {
                MessageBubble(Side.LEFT.takeIf { indexItem < BAR_INDEX_4 } ?: Side.RIGHT,
                    text = "${stepsActivity}/${stepsGoal} steps")
            }
        }
    }
}

@Composable
fun calcBubbleXOffset(graphWidth: Int, indexClickedBar: Int, xMarginOfBars: Dp): Dp {
    val wholeLeftSpace = (graphWidth - (NUM_OF_BARS * BAR_WIDTH).dp.dpToPx().toInt())
    val spaceForEachBar = wholeLeftSpace.div(NUM_OF_BARS)
    return (spaceForEachBar * indexClickedBar).pxToDp() + xMarginOfBars
}
@Composable
fun Bar(
    dayName: String,
    activityGoal: Int,
    dailyGoal: Int,
    selectedDay: Boolean = false,
    onBarClicked: ( height: Dp) -> Unit
) {
    Column(modifier = Modifier.width(BAR_WIDTH.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Column(
                modifier = Modifier
                    .height(activityGoal.dp)
                    .weight(1f)
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = BlueLightColor)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            onBarClicked(activityGoal.dp)
                        }
                    }
            ) {}
            Column(
                modifier = Modifier
                    .height(dailyGoal.dp)
                    .weight(1f)
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(color = GreenLightColor)
                    .pointerInput(Unit) {
                        detectTapGestures {
                            onBarClicked(activityGoal.dp)
                        }
                    }
            ) {}
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Text(text = dayName, fontSize = 12.sp)
        }
        Row(
            modifier = Modifier
                .height(4.dp)
                .fillMaxWidth()
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
