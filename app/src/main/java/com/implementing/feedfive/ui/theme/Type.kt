package com.implementing.feedfive.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.implementing.feedfive.R

val Avenir = FontFamily(
    Font(R.font.avenirheavy),
    Font(R.font.avenirmedium)
)

val Jost = FontFamily(
    Font(R.font.jost_book),
    Font(R.font.jost_medium, FontWeight.Bold)
)

val Mont = FontFamily(
    Font(R.font.montserrat_alternates_medium),
    Font(R.font.montserrat_alternates_semibold)
)

// Set of Material typography styles to start with
fun getTypography(font: FontFamily) = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    displayLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    displayMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    displaySmall = TextStyle(
        fontSize = 14.sp
    ),

    titleLarge = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),

    titleMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 96.sp
    ),

    labelSmall  = TextStyle(
        fontSize = 15.sp
    ),


    /* Other default text styles to override
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)