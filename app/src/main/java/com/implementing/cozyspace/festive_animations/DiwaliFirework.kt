package com.implementing.cozyspace.festive_animations

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Firework(
    colorLine: Color = Color.White,
    colorOrb: Color = Color.Yellow,
    lineWidth: Dp = 0.dp,
    hasSparkle: Boolean = true,
    orbRadius: Float = 4f,
    width: Dp = 250.dp,
    height: Dp = 350.dp
) {
    var elapsedTime by remember { mutableStateOf(0L) }
    var alphaValue by remember { mutableStateOf(1f) }
    var isBurst by remember { mutableStateOf(false) }
    var isSparkle by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }
    var isShow by remember { mutableStateOf(true) }
    var fixY = 0f

    if (isShow) {
        LaunchedEffect(Unit) {
            while (!isFinished) {
                delay(60) // Every 0.05 seconds.
                elapsedTime += 50 // Accumulate time in increments of 50 milliseconds.
            }
            while (isBurst) {
                repeat(3) { // Fading out fireworks.
                    delay(100)
                    if (alphaValue - 0.2f > 0f) {
                        alphaValue -= 0.2f
                    } else {
                        elapsedTime = 0
                        isShow = false
                    }
                }
            }
        }

        Canvas(
            modifier = Modifier
                .width(width)
                .height(height)
                .background(Color.Transparent)
        ) {
            val centerX = size.width / 2f
            val baseY = size.height
            val duration = 2000L // The duration of the fireworks is n seconds.
            val burstPosition = baseY / 4  // Explode at a distance of 1/4 from the top.

            // Calculate the position of the fireworks at different time points.
            val currentY =
                if (isBurst) burstPosition else baseY - (elapsedTime.toFloat() / duration) * baseY
            val circleRadiusLength = size.width * 0.4f // The radius of the fireworks.

            // Draw a vertical line.
            drawLine(
                color = colorLine.copy(alpha = alphaValue),
                start = Offset(centerX, baseY),
                end = if (elapsedTime >= duration) Offset(centerX, fixY) else Offset(
                    centerX,
                    currentY
                ),
                strokeWidth = lineWidth.toPx()
            )

            // Reach the burst location.
            if (currentY <= burstPosition) {
                isBurst = true
                fixY = currentY

                // After the burst, orbs appear.
                if (hasSparkle) {
                    isSparkle = true
                }
                // Draw a radial ray effect.
                repeat(36) { index ->
                    val angle = index * 10f
                    val lineEndX =
                        centerX + circleRadiusLength * cos(Math.toRadians(angle.toDouble()))
                            .toFloat()
                    val lineEndY =
                        fixY + circleRadiusLength * sin(Math.toRadians(angle.toDouble()))
                            .toFloat()

                    drawLine(
                        color = colorLine.copy(alpha = alphaValue),
                        start = Offset(centerX, fixY),
                        end = Offset(lineEndX, lineEndY),
                        strokeWidth = lineWidth.toPx()
                    )

                    // Draw orbs.
                    if (isSparkle) {
                        drawCircle(
                            color = colorOrb.copy(alpha = alphaValue),
                            radius = orbRadius,
                            center = Offset(lineEndX, lineEndY)
                        )
                    }
                    // Finish after the burst.
                    isFinished = true
                }
            }
        }
    }
}

fun randomColor(randomNumber: Int): Color {
    val color = when(randomNumber) {
        0 -> Color(0xFFC0C0C0)  // Silver
        1 -> Color(0xFFFFD700)  // Gold
        2 -> Color(0xFFDAA520)  // Goldenrod
        3 -> Color(0xFFFFA500)  // Orange
        4 -> Color(0xFFF5F5DC)  // Beige
        5 -> Color.White         // White
        6 -> Color(0xFFFFE135)  // Banana Yellow
        7 -> Color(0xFFFFE4B5)  // Moccasin (light golden shade)
        8 -> Color(0xFFFFDAB9)  // Peach Puff (light yellowish shade)
        9 -> Color(0xFFFFD700) // Yellow (repeated bright yellow)
        else -> Color.Transparent
    }
    return color
}
