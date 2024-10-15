package com.implementing.cozyspace.inappscreens.diary.chart

import androidx.compose.foundation.Canvas
import com.implementing.cozyspace.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implementing.cozyspace.model.Diary
import com.implementing.cozyspace.util.Mood



@Composable
fun MoodCircularBar(
    modifier: Modifier = Modifier,
    entries: List<Diary>,
    strokeWidth: Float = 75f,
    showPercentage: Boolean = true,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column(
            Modifier.clickable {
                onClick()
            }
                .background(
                    Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF221F3E), // Light grey at the start
                        0.5f to Color(0xFF775A6D), // Darker grey in the middle
                        1f to Color(0xFF221F3E) // Dark grey at the end
                    )
                ))

        ) {
            val mostFrequentMood by remember(entries) {
                derivedStateOf {
                    // if multiple ones with the same frequency, return the most positive one
                    val entriesGrouped = entries
                        .groupBy { it.mood }
                    val max = entriesGrouped.maxOf { it.value.size }
                    entriesGrouped
                        .filter { it.value.size == max }
                        .maxByOrNull {
                            it.key.value
                        }?.key ?: Mood.OKAY
                }
            }
            val moods by remember(entries) {
                derivedStateOf {
                    entries.toPercentages()
                }
            }
            Text(
                text = stringResource(R.string.mood_summary),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                textAlign = TextAlign.Center,
                color = Color.White
            )
            if (entries.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    var currentAngle = remember { 90f }

                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(29.dp)
                    ) {
                        for ((mood, percentage) in moods) {
                            drawArc(
                                color = mood.color,
                                startAngle = currentAngle,
                                sweepAngle = percentage * 360f,
                                useCenter = false,
                                size = Size(size.width, size.width),
                                style = Stroke(
                                    width = strokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Miter
                                )
                            )
                            currentAngle += percentage * 360f
                        }
                    }
                    if (showPercentage) Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for ((mood, percentage) in moods) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    stringResource(
                                        R.string.percent,
                                        (percentage * 100).toInt()
                                    ),
                                    color = Color.White
                                )
                                Spacer(Modifier.width(8.dp))
                                Image(
                                    painter = painterResource(mood.icon),
                                    contentDescription = stringResource(mood.title),
                                    modifier = Modifier.size(strokeWidth.dp / 3)
                                )

                                Spacer(Modifier.width(2.dp))

                                Icon(
                                    painter = painterResource(mood.icon),
                                    contentDescription = stringResource(mood.title),
                                    tint = mood.color,
                                    modifier = Modifier.size(strokeWidth.dp / 3)
                                )
                            }
                        }
                    }
                }
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.your_mood_was))
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = mostFrequentMood.color
                            )
                        ) {
                            append(stringResource(mostFrequentMood.title))
                        }
                        append(stringResource(R.string.most_of_the_time),)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            } else {
                Text(
                    text = stringResource(R.string.no_data_yet),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}

fun List<Diary>.toPercentages(): Map<Mood, Float> {
    return this
        .sortedBy { it.mood.value }
        .groupingBy { it.mood }
        .eachCount()
        .mapValues { it.value / this.size.toFloat() }
}

@Composable
@Preview
fun MoodCircularBarPreview() {
    MoodCircularBar(
        entries = listOf(
            Diary(
                id = 1,
                mood = Mood.AWESOME
            ),
            Diary(
                id = 1,
                mood = Mood.AWESOME
            ),
            Diary(
                id = 2,
                mood = Mood.GOOD,
            ),
            Diary(
                id = 3,
                mood = Mood.OKAY,
            ),
            Diary(
                id = 3,
                mood = Mood.OKAY,
            ),
            Diary(
                id = 4,
                mood = Mood.BAD,
            ),
            Diary(
                id = 5,
                mood = Mood.TERRIBLE,
            ),
            Diary(
                id = 6,
                mood = Mood.SLEEPY,
            ),
            Diary(
                id = 5,
                mood = Mood.TERRIBLE,
            )
        )
    )
}