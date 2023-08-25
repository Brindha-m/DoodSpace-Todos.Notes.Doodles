package com.implementing.feedfive.inappscreens.task.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.task.SubTaskItem
import com.implementing.feedfive.inappscreens.task.TaskCheckBox
import com.implementing.feedfive.inappscreens.task.TaskEvent
import com.implementing.feedfive.inappscreens.task.viewmodel.TaskViewModel
import com.implementing.feedfive.model.SubTask
import com.implementing.feedfive.model.Task
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.util.Priority
import com.implementing.feedfive.util.formatDateDependingOnDay
import com.implementing.feedfive.util.toInt
import com.implementing.feedfive.util.toPriority
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskDetailScreen(
    navController: NavHostController,
    taskId: Int,
    viewModel: TaskViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        viewModel.onEvent(TaskEvent.GetTask(taskId))
    }
    val uiState = viewModel.taskDetailsUiState
    val snackbarHostState = remember { SnackbarHostState() }
    var openDialog by rememberSaveable { mutableStateOf(false) }

    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var priority by rememberSaveable { mutableStateOf(Priority.LOW) }
    var dueDate by rememberSaveable { mutableStateOf(0L) }
    var dueDateExists by rememberSaveable { mutableStateOf(false) }
    var completed by rememberSaveable { mutableStateOf(false) }
    val subTasks = remember { mutableStateListOf<SubTask>() }
    val priorities = listOf(Priority.LOW, Priority.MEDIUM, Priority.HIGH)
    val context = LocalContext.current

    LaunchedEffect(uiState.task) {
        title = uiState.task.title
        description = uiState.task.description
        priority = uiState.task.priority.toPriority()
        dueDate = uiState.task.dueDate
        dueDateExists = uiState.task.dueDate != 0L
        completed = uiState.task.isCompleted
        subTasks.addAll(uiState.task.subTasks)
    }

    LaunchedEffect(uiState) {
        if (uiState.navigateUp) {
            openDialog = false
            navController.popBackStack(Screen.TaskSearchScreen.route, false)
            navController.navigateUp()
        }
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(
                uiState.error
            )
            viewModel.onEvent(TaskEvent.ErrorDisplayed)
        }
    }

    BackHandler {
        updateTaskIfChanged(
            uiState.task,
            uiState.task.copy(
                title = title,
                description = description,
                dueDate = if (dueDateExists) dueDate else 0L,
                priority = priority.toInt(),
                subTasks = subTasks
            ),
            {
                navController.popBackStack(Screen.TaskSearchScreen.route, false)
                navController.navigateUp()
            }
        ) {
            viewModel.onEvent(TaskEvent.UpdateTask(it, dueDate != uiState.task.dueDate))
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { openDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.delete_task)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//                elevation = 0.dp,
            )
        }
    ) {paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 15.dp, bottom = 20.dp, top = 55.dp, end = 15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TaskCheckBox(
                    isComplete = completed,
                    borderColor = priority.color
                ) {
                    completed = !completed
                    viewModel.onEvent(
                        TaskEvent.CompleteTask(
                            uiState.task,
                            completed
                        )
                    )
                }
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = stringResource(R.string.title)) },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
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
                Checkbox(checked = dueDateExists, onCheckedChange = {
                    dueDateExists = it
                    if (it)
                        dueDate = Calendar.getInstance().timeInMillis
                })
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
                                if (dueDate == 0L) Calendar.getInstance() else Calendar
                                    .getInstance()
                                    .apply { timeInMillis = dueDate }
                            val tempDate = Calendar.getInstance()
                            val timePicker = TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    tempDate[Calendar.HOUR_OF_DAY] = hour
                                    tempDate[Calendar.MINUTE] = minute
                                    dueDate = tempDate.timeInMillis
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
                        text = dueDate.formatDateDependingOnDay(),
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
        }
    }
    if (openDialog)
        AlertDialog(
            shape = RoundedCornerShape(25.dp),
            onDismissRequest = { openDialog = false },
            title = { Text(stringResource(R.string.delete_task_confirmation_title)) },
            text = {
                Text(
                    stringResource(
                        R.string.delete_task_confirmation_message,
                        uiState.task.title
                    )
                )
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(Color.Red),
                    shape = RoundedCornerShape(25.dp),
                    onClick = {
                        viewModel.onEvent(TaskEvent.DeleteTask(uiState.task))
                    },
                ) {
                    Text(stringResource(R.string.delete_task), color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    shape = RoundedCornerShape(25.dp),
                    onClick = {
                        openDialog = false
                    }) {
                    Text(stringResource(R.string.cancel), color = Color.White)
                }
            }
        )
}

private fun updateTaskIfChanged(
    task: Task,
    newTask: Task,
    onNotChanged: () -> Unit = {},
    onUpdate: (Task) -> Unit,
) {
    if (taskChanged(task, newTask)) onUpdate(newTask) else onNotChanged()
}

private fun taskChanged(
    task: Task,
    newTask: Task
): Boolean {
    return task.title != newTask.title ||
            task.description != newTask.description ||
            task.dueDate != newTask.dueDate ||
            task.priority != newTask.priority ||
            task.subTasks != newTask.subTasks
}