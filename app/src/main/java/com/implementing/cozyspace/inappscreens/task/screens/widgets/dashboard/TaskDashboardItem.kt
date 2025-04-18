package com.implementing.cozyspace.inappscreens.task.screens.widgets.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.model.Task
import com.implementing.cozyspace.util.formatDateDependingOnDay
import com.implementing.cozyspace.util.isDueDateOverdue
import com.implementing.cozyspace.util.toPriority

@Composable
fun TaskDashboardItem(
    modifier: Modifier = Modifier,
    task: Task,
    onComplete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),

    ) {
        Column(
            Modifier
                .clickable {
                    onClick()
                }
                .padding(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TaskDashboardCheckBox(
                    isComplete = task.isCompleted,
                    task.priority.toPriority().color,
                    onComplete = { onComplete() }
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            if (task.dueDate != 0L) {
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(10.dp),
                        painter = painterResource(R.drawable.ic_alarm),
                        contentDescription = stringResource(R.string.due_date),
                        tint = if (task.dueDate.isDueDateOverdue()) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = task.dueDate.formatDateDependingOnDay(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (task.dueDate.isDueDateOverdue()) Color.Red else MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
fun TaskDashboardCheckBox(
    isComplete: Boolean,
    borderColor: Color,
    onComplete: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .border(2.dp, borderColor, CircleShape)
            .clickable {
                onComplete()
            }, contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = isComplete) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun TaskDashboardItemPreview() {
    TaskDashboardItem(
        task = Task(
            title = "Task 1",
            description = "Task 1 description",
            dueDate = 1666999999999L,
            priority = 1,
            isCompleted = false
        ),
        onComplete = {},
        onClick = {}
    )
}