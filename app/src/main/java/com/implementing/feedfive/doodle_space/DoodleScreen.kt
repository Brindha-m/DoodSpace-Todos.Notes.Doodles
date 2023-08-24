package com.implementing.feedfive.doodle_space

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.implementing.feedfive.doodle_space.contoller.DoodleController
import com.implementing.feedfive.doodle_space.utils.BottomTabBar
import com.implementing.feedfive.doodle_space.utils.ColorPickerAlertBox
import com.implementing.feedfive.doodle_space.utils.DoodleBox


@Composable
fun DoodleScreen(doodleController: DoodleController) {

    var currentColor by remember { mutableStateOf(doodleController.currentColor) }
    var openDialog by remember { mutableStateOf(false) }
    var showSeekBar by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(doodleController.pathStroke) }
    var toolsVisibility by remember { mutableStateOf(true) }
    val doodleController = viewModel<DoodleController>()

    Scaffold(
        Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(visible = toolsVisibility) {
                BottomTabBar(
                    buttonIndex = {
                        when (it) {
                            0 -> {
                                showSeekBar = false
                                doodleController.undo()
                            }

                            1 -> {
                                showSeekBar = false
                                doodleController.redo()
                            }

                            2 -> {
                                openDialog = !openDialog
                                showSeekBar = false
                            }

                            3 -> {
                                doodleController.clear()
                            }

                            4 -> {
                                showSeekBar = !showSeekBar
                            }

                            else -> {}
                        }
                    },
                    currentColor = currentColor,
                    sliderPosition = sliderPosition,
                    sliderPositionOnSlide = {
                        sliderPosition = it
                        doodleController.pathStroke(it)
                    },
                    showSeekBar = showSeekBar
                )
            }
        },
    ) { paddingValues ->
        DoodleBox(
            modifier = Modifier.padding(paddingValues),
            showSeekBar = { showSeekBar = false },
            insertNewPath = {
                doodleController.insertNewPath(it)
            },
            updatePath = {
                doodleController.updatePath(it)
            },
            pointPathSnapshotStateList = doodleController.point,
            pointPathOnClick = { doodleController.point = it },
            onDragEnd = { toolsVisibility = !toolsVisibility }
        )
        if (openDialog) {
            ColorPickerAlertBox(
                sentCurrentColor = {
                    currentColor = it
                    doodleController.currentColor(color = it)
                },
                onClickDialog = { openDialog = !openDialog },
                currentColor = currentColor
            )
        }
    }
}