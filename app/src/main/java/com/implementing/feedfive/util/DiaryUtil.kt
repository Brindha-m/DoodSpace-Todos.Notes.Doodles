package com.implementing.feedfive.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.implementing.feedfive.R
import com.implementing.feedfive.ui.theme.Blue
import com.implementing.feedfive.ui.theme.Green
import com.implementing.feedfive.ui.theme.Orange
import com.implementing.feedfive.ui.theme.Purple

enum class Mood(@DrawableRes val icon: Int, val color: Color, @StringRes val title: Int, val value: Int) {
    AWESOME(R.drawable.img, Green, R.string.awesome, 6),
    GOOD(R.drawable.img_1, Blue, R.string.good, 5),
    OKAY(R.drawable.img_2, Purple, R.string.okay, 4),
    SLEEPY(R.drawable.img_5, Orange, R.string.sleepy, 3),
    BAD(R.drawable.img_3, Color(0xFF8B4513), R.string.bad, 2),
    TERRIBLE(R.drawable.img_4, Color.Red, R.string.terrible, 1)

}