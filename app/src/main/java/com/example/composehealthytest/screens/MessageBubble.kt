package com.example.composehealthytest.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composehealthytest.constants.Constants.BUBBLE_WIDTH
import com.example.composehealthytest.ui.theme.BlueSuperLightColor
import com.example.composehealthytest.ui.theme.DarkGrayColor

val IncomingMessageLeft = RoundedCornerShape(
    topStart = 8.dp,
    topEnd = 8.dp,
    bottomEnd = 8.dp,
    bottomStart = 0.dp)

val IncomingMessageRight = RoundedCornerShape(
    topStart = 8.dp,
    topEnd = 8.dp,
    bottomEnd = 0.dp,
    bottomStart = 8.dp)




@Composable
fun MessageBubble(
    side: Side,
    text: String,
    modifier: Modifier = Modifier,
) {

    Surface(
        shape = IncomingMessageLeft.takeIf { side == Side.LEFT } ?: IncomingMessageRight ,
        color = BlueSuperLightColor,
        modifier = modifier.padding(7.dp).width(BUBBLE_WIDTH)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold, fontSize = 12.sp,
            color = DarkGrayColor,
            modifier = Modifier.padding(8.dp)
        )
    }
}

enum class Side {
    LEFT,
    RIGHT
}