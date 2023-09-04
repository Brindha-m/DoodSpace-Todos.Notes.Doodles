package com.implementing.cozyspace.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(

    /* tab container and texts color, add buttons, edit dialog */
    primary = PrimaryColor,
    primaryContainer = Color.LightGray,

    secondary = SecondaryColor,
//    surface = DarkGray,
    background = Color.Black,
    onSurface = Color.White,
    onBackground = Color.White,
    /* Bottom navs */
    onPrimary = Color.LightGray,
    // ball colors
    onTertiary = Color(0xFF67509F),
    onSecondary = Color(0xFF1B1A18),

    // save icon
    scrim = Color.DarkGray,

    inverseOnSurface = Color.White
)

private val LightColorScheme = lightColorScheme(

    /* tab container and texts color, add buttons */
    primary = PrimaryLightColor,
    primaryContainer = Color.Black,

    secondary = SecondaryColor,
    background = Color.White,
    onTertiary = Color.DarkGray,
    onSecondary = Color.DarkGray,
    onPrimary = Color.LightGray,

    // save icon
    scrim = Color.White,
// Calendar
    inverseOnSurface = Color(0xFFBCABE6)


)

@Composable
fun FeedFiveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontFamily: FontFamily = Avenir,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            if (darkTheme) {
                window.statusBarColor = Color.Black.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            } else {
                window.statusBarColor = Color.Black.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }

        }
    }

    val typography = getTypography(fontFamily)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = Shapes,
        content = content
    )
}