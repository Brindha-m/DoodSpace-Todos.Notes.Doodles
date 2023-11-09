package com.implementing.cozyspace.festive_animations.fireworks

import androidx.compose.ui.graphics.*
import androidx.lifecycle.ViewModel
import com.implementing.cozyspace.festive_animations.confetticenter.SHAPES

/// - Parameters:
///  - pieceCount: amount of confetti
///  - colors: list of colors that is applied to the default shapes
///  - pieceSize: size that confetti and emojis are scaled to
///  - radius: explosion radius
///  - repetitions: number of repetitions of the explosion
///  - repetitionInterval: duration between the repetitions
class FireworkCenterVM(
    var pieceCount: Int = 15,
    var pieceType: List<FireworkTypes> = listOf(
//        FireworkTypes.Text("💥"),
        FireworkTypes.Shape(SHAPES.PENTAGON),
//        FireworkTypes.Text("Happy Diwali")

    ),
    var colors: List<Color> = listOf(
        Color(0xFFB39847),
        Color(0xFFFFAB00),
        Color(0xFFDDA704),
        Color(0xFFDDA704),
        Color(0xFFDDA704),
        Color(0xFFCF9D51),
        Color(0xFFB6983D),
        Color(0xFFDB9221),
//        Color(0xFF00C853),
//        Color(0xff9c1d08),
//        Color(0xffce7117),
//        Color(0xfff24d24),
//        Color(0xFF2962FF),
//        Color(0xffc54a85),
//        Color(0xFF00BFA5),
//        Color(0xFFAA00FF),
    ),
    var pieceSize: Float = 5.0f,
    var radius: Float = 100f,
    var repetitions: Int = 0,
    var repetitionInterval: Double = 1000.0,
    var explosionAnimDuration: Double = 1200.0,
    var launchAnimDuration: Double = 3000.0,
) : ViewModel() {
    fun getAnimDuration(): Double {
        return explosionAnimDuration + launchAnimDuration
    }
}

sealed interface FireworkTypes {
    data class Image(val value: Int) : FireworkTypes
    data class Text(val value: String) : FireworkTypes
    data class Shape(val value: SHAPES) : FireworkTypes
}