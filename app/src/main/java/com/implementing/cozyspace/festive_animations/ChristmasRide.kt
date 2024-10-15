package com.implementing.cozyspace.festive_animations

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.implementing.cozyspace.R
import com.implementing.cozyspace.mainscreens.viewmodel.MainViewModel
import com.implementing.cozyspace.ui.theme.FeedFiveTheme
import com.implementing.cozyspace.util.ThemeSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ChristmasRide() {
    val animState = MutableStateFlow(false)

    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            animState.value = true
            delay(3000)
            animState.value = false
            delay(4000)
        }
    }

    RunningCarScreenSkeleton(
        animState
    )
}

@Preview
@Composable
fun RunningCarScreenSkeletonPreview() {
    FeedFiveTheme {
        RunningCarScreenSkeleton(
            MutableStateFlow(true)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RunningCarScreenSkeletonPreviewDark() {
    FeedFiveTheme {
        RunningCarScreenSkeleton(
            MutableStateFlow(true)
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun RunningCarScreenSkeleton(
    _animState: StateFlow<Boolean>,
    viewModel: MainViewModel = hiltViewModel()
) {
    val animState = _animState.collectAsState()

    val themeMode = viewModel.themeMode.collectAsState(initial = ThemeSettings.DARK.value)

    val animRotationZ by animateFloatAsState(
        targetValue = if (animState.value) 360f else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = LinearOutSlowInEasing
        ), label = ""
    )


    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        /*
        Image(
            painter = painterResource(id = R.drawable.ic_jetpack_compose_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .align(Alignment.Center)
                .size(128.dp)
                .graphicsLayer {
                    rotationZ = animRotationZ
                }
        )
        */

        Column(
            Modifier.align(Alignment.CenterStart),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .wrapContentHeight(),
                contentAlignment = Alignment.CenterEnd
            ) {
                val transition = rememberInfiniteTransition(label = "")
                val updateTransition =
                    updateTransition(animState.value, label = "fore&back-ground")

                val animatePositionBackground by updateTransition.animateFloat(
                    label = "foreground",
                    transitionSpec = {
                        tween(
                            2000,
                            easing = CubicBezierEasing(0.0f, 0.0f, 0.5f, 1.0f)
                        )
                    }
                ) { state ->
                    when (state) {
                        true -> -400f
                        else -> 0f
                    }
                }

                val animatePositionForeground by updateTransition.animateFloat(
                    label = "background",
                    transitionSpec = {
                        tween(
                            2000,
                            easing = CubicBezierEasing(0.0f, 0.0f, 0.5f, 1.0f)
                        )
                    }
                ) { state ->
                    when (state) {
                        true -> -700f
                        else -> 0f
                    }
                }

                val animateSantaPositionX by updateTransition.animateFloat(
                    label = "background",
                    transitionSpec = {
                        tween(
                            2000,
                            easing = CubicBezierEasing(0.0f, 0.0f, 0.5f, 1.0f)
                        )
                    }
                ) { state ->
                    when (state) {
                        true -> 0f
                        else -> -1000f
                    }
                }

                val animatePositionSanta by transition.animateFloat(
                    initialValue = 1f,
                    targetValue = -1f,
                    animationSpec = infiniteRepeatable(tween(3000, easing = LinearOutSlowInEasing)),
                    label = ""
                )

                // Animation for the image moving from left to right
                val animateSantaPositionXLeftToRight by updateTransition.animateFloat(
                    label = "background",
                    transitionSpec = {
                        tween(
                            2000,
                            easing = CubicBezierEasing(0.0f, 0.0f, 0.5f, 1.0f)
                        )
                    }
                ) { state ->
                    when (state) {
                        true -> 0f // Move towards the center
                        else -> -800f // Move from right to the center
                    }
                }

                // Animation for the image moving from right to left
                val animateSantaPositionXRightToLeft by updateTransition.animateFloat(
                    label = "background",
                    transitionSpec = {
                        tween(
                            2000,
                            easing = CubicBezierEasing(0.0f, 0.0f, 0.5f, 1.0f)
                        )
                    }
                ) { state ->
                    when (state) {
                        true -> 0f // Move towards the center
                        else -> -700f // Move from right to the center
                    }
                }

                Image(
//                    painterResource(id = R.drawable.scrolling_foreground),
                    painterResource(id = R.drawable.spaceweekfg),
                    "foreground",
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 15.dp, top = 18.dp)
                            .height(48.dp)
                            .requiredWidth(1600.dp)
                            .graphicsLayer {
                                translationX = animatePositionSanta
                            },
//                        .background(Color.Transparent),
                        contentScale = ContentScale.Inside,
//                    colorFilter = ColorFilter.tint(
////                        Color(0x003D3D3D)
//                                // color road
//                                Color(0xFF3D3D3D)
//                    )
                )

                /* background **/

                Image(
//                    painterResource(id = R.drawable.scrolling_background),
                    painterResource(id = R.drawable.spaceweekbg),
                    "background",
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 15.dp, top = 18.dp)
                        .height(48.dp)
                        .requiredWidth(1280.dp)
                        .graphicsLayer {
                            translationX = animatePositionBackground
                        },
                    contentScale = ContentScale.Inside,
//                    colorFilter = ColorFilter.tint(Color(0x00E6E6E6))
                    /* color road */
//                    colorFilter = ColorFilter.tint(Color(0xFFE6E6E6))

                )






                Row {
//                    Image(
//                        painterResource(id = R.drawable.running),
//                        contentDescription = "Santa",
//                        Modifier
//                            .size(54.dp)
//                            .graphicsLayer {
//                                translationX = animateSantaPositionX
//                                translationY = animatePositionSanta
//                            }
//                    )

                    Image(
                        painterResource(id = R.drawable.spaceweekastronaut),
                        contentDescription = "Santa/Astronaut",
                        Modifier
                            .size(40.dp)
                            .graphicsLayer {
                                translationX = animateSantaPositionX
                                translationY = animatePositionSanta
                            }
//
                    )

                }

            }
        }
    }

}