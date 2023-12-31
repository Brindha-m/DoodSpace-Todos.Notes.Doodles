package com.implementing.cozyspace.inappscreens.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.model.SubTask

@Composable
fun SubTaskItem(
    subTask: SubTask,
    onChange: (SubTask) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = stringResource(R.string.delete_sub_task),
            modifier = Modifier.clickable { onDelete() }
        )
        Checkbox(
            checked = subTask.isCompleted,
            onCheckedChange = { onChange(subTask.copy(isCompleted = it)) },
            colors = CheckboxDefaults.colors(Color.DarkGray)
        )
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value = subTask.title,
            enabled = true,
            onValueChange = {
                onChange(subTask.copy(title = it))
            },
            textStyle = if (subTask.isCompleted)
                TextStyle(
                    textDecoration = TextDecoration.LineThrough,
                    color = MaterialTheme.colorScheme.primary
                )
            else
                MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        )

    }
}
