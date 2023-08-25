package com.implementing.feedfive.doodle_space.utils


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.implementing.feedfive.R
import com.implementing.feedfive.doodle_space.model.ModelObject

@Composable
fun BottomTabBar(
    buttonIndex: (Int) -> Unit,
    currentColor: Color = Color.Red,
    sliderPosition: Float,
    sliderPositionOnSlide: (Float) -> Unit,
    showSeekBar: Boolean
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                AnimatedVisibility(visible = showSeekBar) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(if (showSeekBar) 48.dp else 0.dp)
                    ) {
                        MySliderDemo(sliderPosition, sliderPositionOnSlide)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    Arrangement.SpaceAround,
                    Alignment.CenterVertically
                ) {
                    ModelObject.IconButtonModelList.forEachIndexed { index, it ->
                        if (index == 2) {
                            IconButton(onClick = { buttonIndex(index) }) {
                                Icon(
                                    painter = painterResource(id = it.icon),
                                    contentDescription = it.name,
                                    modifier = Modifier.border(
                                        2.dp,
                                        color = Color.Gray,
                                        shape = CircleShape
                                    ),
                                    tint = currentColor,
                                )
                            }
                        } else {
                            IconButton(onClick = { buttonIndex(index) }) {
                                Icon(
                                    painter = painterResource(id = it.icon),
                                    contentDescription = it.name,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                    IconButton(onClick = { buttonIndex(4) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_size),
                            contentDescription = "Size"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MySliderDemo(sliderPosition: Float, sliderPositionOnSlide: (Float) -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = (sliderPosition * 100).toInt().toString())
            Slider(value = sliderPosition, onValueChange = { sliderPositionOnSlide(it) })
        }
    }
}