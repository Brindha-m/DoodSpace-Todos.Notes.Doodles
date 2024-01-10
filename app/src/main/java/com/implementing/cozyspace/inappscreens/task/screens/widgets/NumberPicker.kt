package com.implementing.cozyspace.inappscreens.task.screens.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun NumberPicker(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { onValueChange(value - 1) }) {
                Text(text = "-", style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = value.toString(), style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = { onValueChange(value + 1) }) {
                Text(text = "+", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}