package com.implementing.feedfive.inappscreens.task.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.task.SubTaskItem
import com.implementing.feedfive.model.SubTask
import com.implementing.feedfive.model.Task
import com.implementing.feedfive.util.Priority
import com.implementing.feedfive.util.formatDateDependingOnDay
import com.implementing.feedfive.util.toInt
import com.implementing.feedfive.util.toPriority
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheetContent(
    onAddTask: (Task) -> Unit,
    focusRequester: FocusRequester
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var priority by rememberSaveable { mutableStateOf(Priority.LOW) }
    var dueDate by rememberSaveable { mutableStateOf(Calendar.getInstance()) }
    var dueDateExists by rememberSaveable { mutableStateOf(false) }
    val subTasks = remember { mutableStateListOf<SubTask>() }
    val priorities = listOf(Priority.LOW, Priority.MEDIUM, Priority.HIGH)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .defaultMinSize(minHeight = 1.dp)
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SheetHandle(Modifier.align(Alignment.CenterHorizontally))
        Text(
            text = stringResource(R.string.add_task),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(text = stringResource(R.string.title)) },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )
        Spacer(Modifier.height(12.dp))
        Column {
            subTasks.forEachIndexed { index, item ->
                SubTaskItem(
                    subTask = item,
                    onChange = { subTasks[index] = it },
                    onDelete = { subTasks.removeAt(index) }
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .clickable {
                    subTasks.add(
                        SubTask(
                            title = "",
                            isCompleted = false,
                        )
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.add_sub_task),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Icon(
                modifier = Modifier.size(10.dp),
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = stringResource(
                    id = R.string.add_sub_task
                )
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.priority),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(12.dp))
        PriorityTabRow(
            priorities = priorities,
            priority,
            onChange = { priority = it }
        )
        Spacer(Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = dueDateExists, onCheckedChange = { dueDateExists = it })
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.due_date),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (dueDateExists)
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        val date =
                            if (dueDate.timeInMillis == 0L) Calendar.getInstance() else dueDate
                        val tempDate = Calendar.getInstance()
                        val timePicker = TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                tempDate[Calendar.HOUR_OF_DAY] = hour
                                tempDate[Calendar.MINUTE] = minute
                                dueDate = tempDate
                            }, date[Calendar.HOUR_OF_DAY], date[Calendar.MINUTE], false
                        )
                        val datePicker = DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                tempDate[Calendar.YEAR] = year
                                tempDate[Calendar.MONTH] = month
                                tempDate[Calendar.DAY_OF_MONTH] = day
                                timePicker.show()
                            },
                            date[Calendar.YEAR],
                            date[Calendar.MONTH],
                            date[Calendar.DAY_OF_MONTH]
                        )
                        datePicker.show()
                    }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_alarm),
                        stringResource(R.string.due_date)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.due_date),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = dueDate.timeInMillis.formatDateDependingOnDay(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = stringResource(R.string.description)) },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                onAddTask(
                    Task(
                        title = title,
                        description = description,
                        priority = priority.toInt(),
                        dueDate = if (dueDateExists) dueDate.timeInMillis else 0L,
                        createdDate = System.currentTimeMillis(),
                        updatedDate = System.currentTimeMillis(),
                        subTasks = subTasks.toList()
                    )
                )
                title = ""
                description = ""
                priority = Priority.LOW
                dueDate = Calendar.getInstance()
                dueDateExists = false
                subTasks.clear()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(
                text = stringResource(R.string.add_task),
                style = MaterialTheme.typography.bodyMedium.copy(Color.White)
            )
        }
        Spacer(modifier = Modifier.height(54.dp))
    }
}

@Composable
fun PriorityTabRow(
    priorities: List<Priority>,
    selectedPriority: Priority,
    onChange: (Priority) -> Unit
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        AnimatedTabIndicator(Modifier.tabIndicatorOffset(tabPositions[selectedPriority.toInt()]))
    }
    TabRow(
        selectedTabIndex = selectedPriority.toInt(),
        indicator = indicator,
        modifier = Modifier.clip(RoundedCornerShape(14.dp))
    ) {
        priorities.forEachIndexed { index, it ->
            Tab(
                text = { Text(stringResource(it.title)) },
                selected = selectedPriority.toInt() == index,
                onClick = {
                    onChange(index.toPriority())
                },
                modifier = Modifier.background(it.color)
            )
        }
    }
}

@Composable
fun AnimatedTabIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(5.dp)
            .fillMaxSize()
            .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(8.dp))
    )
}

@Composable
fun SheetHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .size(width = 60.dp, height = 4.dp)
            .background(Color.Gray)
            .padding(5.dp)
    )
}

@Preview
@Composable
fun AddTaskSheetPreview() {
    AddTaskBottomSheetContent(onAddTask = {}, FocusRequester())
}