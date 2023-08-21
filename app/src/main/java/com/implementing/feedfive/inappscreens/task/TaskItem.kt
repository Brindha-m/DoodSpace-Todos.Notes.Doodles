package com.implementing.feedfive.inappscreens.task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyItemScope
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
import com.implementing.feedfive.R
import com.implementing.feedfive.model.Task
import com.implementing.feedfive.util.formatDateDependingOnDay
import com.implementing.feedfive.util.isDueDateOverdue
import com.implementing.feedfive.util.toPriority

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onComplete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .animateItemPlacement(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            Modifier
                .clickable {
                    onClick()
                }
                .padding(12.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TaskCheckBox(
                    isComplete = task.isCompleted,
                    task.priority.toPriority().color,
                    onComplete = { onComplete() }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            if (task.dueDate != 0L) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(13.dp),
                        painter = painterResource(R.drawable.ic_alarm),
                        contentDescription = stringResource(R.string.due_date),
                        tint = if (task.dueDate.isDueDateOverdue()) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = task.dueDate.formatDateDependingOnDay(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (task.dueDate.isDueDateOverdue()) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCheckBox(
    isComplete: Boolean,
    borderColor: Color,
    onComplete: () -> Unit
) {
    Box(modifier = Modifier
        .size(30.dp)
        .clip(CircleShape)
        .border(2.dp, borderColor, CircleShape)
        .clickable {
            onComplete()
        }, contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = isComplete) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun LazyItemScope.TaskItemPreview() {
    TaskItem(
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