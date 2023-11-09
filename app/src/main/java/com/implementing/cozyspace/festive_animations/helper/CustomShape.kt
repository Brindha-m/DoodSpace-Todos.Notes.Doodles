package com.implementing.cozyspace.festive_animations.helper

import androidx.compose.foundation.shape.GenericShape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class CustomShape {
    object Shapes {
        val Triangle = GenericShape { size, _ ->

            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
        }
        val Parallelogram = GenericShape { size, _ ->

            val radian = (90 - 60) * Math.PI / 180
            val xOnOpposite = (size.height * tan(radian)).toFloat()
            moveTo(0f, size.height)
            lineTo(x = xOnOpposite, y = 0f)
            lineTo(x = size.width, y = 0f)
            lineTo(x = size.width - xOnOpposite, y = size.height)
            lineTo(x = xOnOpposite, y = size.height)
        }

        val Heart = GenericShape { size, _ ->
            val width = size.width
            val height = size.height

            moveTo(width / 2f, height * 0.2f)
            cubicTo(
                width * 0.8f, height * 0.0f,
                width * 0.8f, height * 0.6f,
                width * 0.5f, height * 0.8f
            )
            cubicTo(
                width * 0.2f, height * 0.6f,
                width * 0.2f, height * 0.0f,
                width / 2f, height * 0.2f
            )
            close()
        }

        val Square = GenericShape { size, _ ->
            val sideLength = minOf(size.width, size.height)

            val startX = (size.width - sideLength) / 2f
            val startY = (size.height - sideLength) / 2f

            moveTo(startX, startY)
            lineTo(startX + sideLength, startY)
            lineTo(startX + sideLength, startY + sideLength)
            lineTo(startX, startY + sideLength)
            close()
        }

        val Pentagon = GenericShape { size, _ ->
            val width = size.width
            val height = size.height

            val startX = width / 2f
            val startY = height * 0.1f

            val radius = minOf(width, height) * 0.4f

            moveTo(startX, startY)
            lineTo(startX + radius * cos(0f), startY + radius * sin(0f))
            lineTo(
                (startX + radius * cos(2 * PI / 5)).toFloat(),
                (startY + radius * sin(2 * PI / 5)).toFloat()
            )
            lineTo(
                (startX + radius * cos(4 * PI / 5)).toFloat(),
                (startY + radius * sin(4 * PI / 5)).toFloat()
            )
            lineTo(
                (startX + radius * cos(6 * PI / 5)).toFloat(),
                (startY + radius * sin(6 * PI / 5)).toFloat()
            )
            close()
        }


    }
}

