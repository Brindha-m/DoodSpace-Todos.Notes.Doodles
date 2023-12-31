package com.implementing.cozyspace.doodle_space.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.implementing.cozyspace.R


data class PathWrapper(
    val points: SnapshotStateList<Offset>,
    val color: Color,
    val stroke: Float
)

data class IconButtonModel(
    val name: String,
    val icon: Int
)


object ModelObject {
    val IconButtonModelList: List<IconButtonModel> = listOf(
        IconButtonModel("Undo", R.drawable.ic_undo),
        IconButtonModel("Redo", R.drawable.ic_redo),
        IconButtonModel("Path Color", R.drawable.ic_color),
        IconButtonModel("Clear", R.drawable.ic_refresh),
    )
}

