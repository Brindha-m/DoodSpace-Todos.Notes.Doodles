package com.implementing.cozyspace.festive_animations.confetticenter

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.*
import androidx.lifecycle.ViewModel
import com.implementing.cozyspace.festive_animations.helper.CustomShape.Shapes.Heart
import com.implementing.cozyspace.festive_animations.helper.CustomShape.Shapes.Parallelogram
import com.implementing.cozyspace.festive_animations.helper.CustomShape.Shapes.Pentagon
import com.implementing.cozyspace.festive_animations.helper.CustomShape.Shapes.Square
import com.implementing.cozyspace.festive_animations.helper.CustomShape.Shapes.Triangle

/// - Parameters:
///  - counter: on any change of this variable the animation is run
///  - num: amount of confetti
///  - colors: list of colors that is applied to the default shapes
///  - confettiSize: size that confetti and emojis are scaled to
///  - dropHeight: vertical distance that confetti pass
///  - fadesOut: reduce opacity towards the end of the animation
///  - opacity: maximum opacity that is reached during the animation
///  - openingAngle: boundary that defines the opening angle in degrees
///  - closingAngle: boundary that defines the closing angle in degrees
///  - radius: explosion radius
///  - repetitions: number of repetitions of the explosion
///  - repetitionInterval: duration between the repetitions
class ConfettiAnimationVM(
    var confettiNumber: Int = 20,
    var confettiTypes: List<ConfettiTypes> = listOf(
        ConfettiTypes.Shape(SHAPES.TRIANGLE),
//        ConfettiTypes.Shape(SHAPES.CIRCLE),
        ConfettiTypes.Shape(SHAPES.RECTANGLE),
        ConfettiTypes.Shape(SHAPES.RECTANGLE),
        ConfettiTypes.Shape(SHAPES.RECTANGLE),
        ConfettiTypes.Shape(SHAPES.PARALLELOGRAM),
        ConfettiTypes.Shape(SHAPES.SQUARE),
        ConfettiTypes.Shape(SHAPES.HEART),
        ConfettiTypes.Shape(SHAPES.PENTAGON),
        ),
    var colors: List<Color> = listOf(
//        Color.Red,
        Color(0xFFB39847),
        Color(0xFFF5F523),
        Color(0xFFDDA704),
        Color(0xFFDDA704),
        Color(0xFFDDA704),
        Color(0xFFD6C3A6),
        Color(0xFFB6983D),
        Color(0xFFDB9221),
//       Color.random()
    ),
    var confettiSize: Float = 12.0f,
    var dropHeight: Float = 700.0f,
    var fadesOut: Boolean = true,
    var fireworkEffect: Boolean = false,
    var opacity: Double = 1.0,
    var openingAngle: Double = 60.0,
    var closingAngle: Double = 120.0,
    var radius: Float = 300f,
    var repetitions: Int = 0,
    var repetitionInterval: Double = 1000.0,
    var explosionAnimDuration: Double = 200.0,
    var dropAnimationDuration: Double = 4500.0,
): ViewModel() {
    fun getAnimDuration(): Double {
        return explosionAnimDuration + dropAnimationDuration
    }
}

sealed interface ConfettiTypes {
    data class Image(val value: Int) : ConfettiTypes
    data class Text(val value: String) : ConfettiTypes
    data class Shape(val value: SHAPES) : ConfettiTypes
}

enum class SHAPES(val shape: Shape){
    RECTANGLE(RectangleShape),
    CIRCLE(CircleShape),
    TRIANGLE(Triangle),
    PARALLELOGRAM(Parallelogram),

    // New shapes...
    HEART(Heart),
    SQUARE(Square),
    PENTAGON(Pentagon)
}

