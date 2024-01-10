package com.implementing.cozyspace.util


import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun getRandomTextColorSuitableColor(): Color {
    val backgroundColor = getRandomColor()
    val textColor = if (isDarkColor(backgroundColor)) Color.White else Color.Black
    return Color(backgroundColor.red, backgroundColor.green, backgroundColor.blue, textColor.alpha)
}

fun getRandomColor(): Color {
    val random = Random.Default
    return Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))
}

fun isDarkColor(color: Color): Boolean {
    val darkness = 1 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue) / 255
    return darkness >= 0.5
}
