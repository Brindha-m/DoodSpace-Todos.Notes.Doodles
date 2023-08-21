package com.implementing.feedfive.inappscreens.task.screens.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.Action
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.implementing.feedfive.model.Task
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.glance_widgets.CompleteTaskAction
import com.implementing.feedfive.inappscreens.glance_widgets.TaskWidgetItemClickAction
import com.implementing.feedfive.inappscreens.glance_widgets.completed
import com.implementing.feedfive.inappscreens.glance_widgets.taskId
import com.implementing.feedfive.util.Priority
import com.implementing.feedfive.util.formatDateDependingOnDay
import com.implementing.feedfive.util.isDueDateOverdue
import com.implementing.feedfive.util.toPriority

@Composable
fun TaskWidgetItem(
    task: Task
) {
    Box(
        GlanceModifier.padding(bottom = 3.dp)
    ) {
        Column(
            GlanceModifier
                .background(ImageProvider(R.drawable.small_item_rounded_corner_shape))
                .padding(10.dp)
                .clickable(
                    actionRunCallback<TaskWidgetItemClickAction>(
                        parameters = actionParametersOf(
                            taskId to task.id
                        )
                    )
                )
        ) {
            Row(GlanceModifier
                .fillMaxWidth()
                .clickable(
                    actionRunCallback<TaskWidgetItemClickAction>(
                    parameters = actionParametersOf(
                        taskId to task.id
                    )
                ))
                , verticalAlignment = Alignment.CenterVertically) {
                TaskWidgetCheckBox(
                    isComplete = task.isCompleted,
                    task.priority.toPriority().color,
                    onComplete = actionRunCallback<CompleteTaskAction>(
                        parameters = actionParametersOf(
                            taskId to task.id,
                            completed to !task.isCompleted
                        )
                    )
                )
                Spacer(GlanceModifier.width(6.dp))
                Text(
                    task.title,
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    maxLines = 2,
                    modifier = GlanceModifier.clickable(actionRunCallback<CompleteTaskAction>(
                        parameters = actionParametersOf(
                            taskId to task.id,
                            completed to !task.isCompleted
                        )
                    ))
                )
            }
            if (task.dueDate != 0L) {
                Spacer(GlanceModifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = GlanceModifier.size(10.dp),
                        provider = if (task.dueDate.isDueDateOverdue()) ImageProvider(R.drawable.ic_alarm_red) else ImageProvider(
                            R.drawable.ic_alarm
                        ),
                        contentDescription = "",
                    )
                    Spacer(GlanceModifier.width(3.dp))
                    Text(
                        text = task.dueDate.formatDateDependingOnDay(),
                        style = TextStyle(
                            color = if (task.dueDate.isDueDateOverdue()) ColorProvider(Color.Red) else ColorProvider(
                                Color.White
                            ),
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun TaskWidgetCheckBox(
    isComplete: Boolean,
    borderColor: Color,
    onComplete: Action
) {
    Box(
        modifier = GlanceModifier
            .size(25.dp)
            .cornerRadius(99.dp)
            .background(ImageProvider(
                when (borderColor){
                      Priority.LOW.color -> R.drawable.task_check_box_background_green
                      Priority.MEDIUM.color -> R.drawable.task_check_box_background_orange
                    else -> R.drawable.task_check_box_background_red
                }))
            .clickable(
                onClick = onComplete
            ).padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isComplete) {
            Image(
                modifier = GlanceModifier.size(14.dp),
                provider = ImageProvider(R.drawable.ic_check),
                contentDescription = null
            )
        }
    }
}