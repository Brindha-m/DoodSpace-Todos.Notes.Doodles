package com.implementing.cozyspace.doodle_space.utils


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.implementing.cozyspace.doodle_space.helper.pointPath
import com.implementing.cozyspace.doodle_space.model.PathWrapper


@Composable
fun DoodleBox(
    modifier: Modifier,
    showSeekBar: () -> Unit,
    insertNewPath: (Offset) -> Unit,
    updatePath: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    pointPathSnapshotStateList: SnapshotStateList<PathWrapper>,
    pointPathOnClick: (SnapshotStateList<PathWrapper>) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(onDragStart = { offset ->
                    showSeekBar()
                    insertNewPath(offset)
                    onDragEnd()
                },
                    onDragEnd = { onDragEnd() })
                { change, _ ->
                    updatePath(change.position)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(onTap = { offset ->
                    showSeekBar()
                    insertNewPath(offset)
                    updatePath(offset)
                    pointPathOnClick(pointPathSnapshotStateList)
                })
            }
        ) {
            pointPathSnapshotStateList.forEach { pw ->
                drawPath(
                    path = pointPath(pw.points),
                    color = pw.color,
                    style = Stroke(
                        pw.stroke * 100,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}

