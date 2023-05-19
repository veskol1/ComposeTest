package com.example.composehealthytest.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.composehealthytest.R
import com.example.composehealthytest.ui.theme.BlueLightColor
import com.example.composehealthytest.ui.theme.ComposeHealthyTestTheme
import com.example.composehealthytest.ui.theme.GreenLightColor
import com.example.composehealthytest.ui.theme.LightGrayColor
import com.example.composehealthytest.ui.theme.SuperLightGrayColor


@Composable
fun TimelineScreen(data: String, onBackPressed: () -> Unit) {
    ScaffoldWithTopBar(onBackPressed)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTopBar(onBackPressed: () -> Unit) {
    Scaffold(
        topBar = { TopBarHealthy(onBackPressed) }, content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                DateTitle()
                TimelineData()
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHealthy(onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Timeline")
        },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(painterResource(id = R.drawable.back_ic), "back Button")
            }
        },
    )
}

@Composable
fun DateTitle() {
    Row (modifier = Modifier.padding(top = 20.dp, start = 24.dp)){
        Text(text = "MARCH-APRIL")
    }
}

@Composable
fun TimelineData() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp)) {
        DayDataRow()
        DayDataRow()
        DayDataRow()
        DayDataRow()
        DayDataRow()
        DayDataRow()
        DayDataRow()
    }
}


@Composable
fun DayDataRow() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .padding(end = 24.dp, bottom = 32.dp)) {
        drawLeftIndicator()
        Spacer(modifier = Modifier.width(20.dp))
        DateTile()
        Spacer(modifier = Modifier.width(8.dp))
        Divider(modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
            .background(color = Color.LightGray))
        Spacer(modifier = Modifier.width(8.dp))
        CircularProgress(size = 46.dp, strokeWidth = 6.dp)
        Spacer(modifier = Modifier.width(8.dp))
        StepsTile()
        Spacer(modifier = Modifier.width(20.dp))
        DistanceTile()
    }
}

@Composable
fun DistanceTile() {
    Column {
        Row (verticalAlignment = Alignment.CenterVertically){
            drawCircle()
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "400 kcal")
        }
        Row (verticalAlignment = Alignment.CenterVertically){
            drawCircle()
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "4 km")
        }
    }
}

@Composable
fun drawCircle() {
    Canvas(modifier = Modifier.size(10.dp), onDraw = {
        drawCircle(color = LightGrayColor)
    })
}

@Composable
fun StepsTile() {
    Column {
        Text(text = "Steps:")
        Row {
            Text(text = "2000", color = GreenLightColor)
            Text(text = "/4000")
        }
    }
}

@Composable
fun DateTile() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "31")
        Text(text = "sun")
    }
}

@Composable
fun drawLeftIndicator() {
    Canvas(
        modifier = Modifier
            .fillMaxHeight()
            .width(2.dp)
            .progressSemantics()
    ) {
        drawRect(color = BlueLightColor)
    }
}


@Composable
fun CircularProgress(
    size: Dp,
    strokeWidth: Dp
) {
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
    }

    // draw on canvas
    Canvas(
        modifier = Modifier
            .progressSemantics()
            .size(size)
            .padding(strokeWidth / 2)
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
            sweepAngle = 90f,
            useCenter = false,
            color = BlueLightColor,
            style = stroke
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimelineScreenPreview() {
    ComposeHealthyTestTheme {
        TimelineScreen("asfdsa", {})
    }
}