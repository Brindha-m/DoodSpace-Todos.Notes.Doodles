package com.implementing.cozyspace.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.implementing.cozyspace.R
import com.implementing.cozyspace.ui.theme.Blue
import com.implementing.cozyspace.ui.theme.Green
import com.implementing.cozyspace.ui.theme.Orange
import com.implementing.cozyspace.ui.theme.Purple

enum class Mood(@DrawableRes val icon: Int, val color: Color, @StringRes val title: Int, val value: Int) {
    AWESOME(R.drawable.img, Green, R.string.awesome, 6),
    GOOD(R.drawable.img_1, Color(0xFFE8CE94), R.string.good, 5),
    OKAY(R.drawable.img_11, Purple, R.string.okay, 4),
    SLEEPY(R.drawable.img_5, Orange, R.string.sleepy, 3),
    BAD(R.drawable.img_10, Color.Red, R.string.bad, 2),
    TERRIBLE(R.drawable.img_4, Color(0xFF8B4513), R.string.terrible, 1)

}