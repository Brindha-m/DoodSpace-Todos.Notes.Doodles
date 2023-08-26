package com.implementing.feedfive.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.implementing.feedfive.R

val Avenir = FontFamily(
    Font(R.font.avenir_heavy),
    Font(R.font.avenir_medium)
)

val Jost = FontFamily(
    Font(R.font.jost_book),
    Font(R.font.jost_medium)
)

val Rubik = FontFamily(
    Font(R.font.rubik_regular),
    Font(R.font.rubik_bold)
)


// Set of Material typography styles to start with
fun getTypography(font: FontFamily) = Typography(

    bodyMedium = TextStyle(fontFamily = font),

    titleMedium = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Normal,
        fontSize = 96.sp
    ),

    // For diary

    displayMedium = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    // Space Main headings

    bodyLarge = TextStyle(
        fontFamily = font,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,


//        fontWeight = FontWeight.Normal,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
    ),

    // Top app bars
    titleLarge = TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp
    ),

    displayLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),



    displaySmall = TextStyle(
        fontSize = 14.sp
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