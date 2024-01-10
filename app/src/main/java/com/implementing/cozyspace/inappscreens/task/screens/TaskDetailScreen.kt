package com.implementing.cozyspace.inappscreens.task.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.task.SubTaskItem
import com.implementing.cozyspace.inappscreens.task.TaskCheckBox
import com.implementing.cozyspace.inappscreens.task.TaskEvent
import com.implementing.cozyspace.inappscreens.task.screens.widgets.NumberPicker
import com.implementing.cozyspace.inappscreens.task.viewmodel.TaskViewModel
import com.implementing.cozyspace.model.SubTask
import com.implementing.cozyspace.model.Task
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.util.Priority
import com.implementing.cozyspace.util.TaskFrequency
import com.implementing.cozyspace.util.formatDateDependingOnDay
import com.implementing.cozyspace.util.toInt
import com.implementing.cozyspace.util.toPriority
import com.implementing.cozyspace.util.toTaskFrequency
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    var recurring by rememberSaveable { mutableStateOf(false) }
    var frequency by rememberSaveable { mutableIntStateOf(0) }
    var frequencyAmount by rememberSaveable { mutableIntStateOf(0) }
    val subTasks = remember { mutableStateListOf<SubTask>() }
    val priorities = listOf(Priority.LOW, Priority.MEDIUM, Priority.HIGH)
    val context = LocalContext.current
    val formattedDate by remember {
        derivedStateOf {
            dueDate.formatDateDependingOnDay()
        }
    }

    val timeState = rememberTimePickerState()
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val opendateDialog = remember { mutableStateOf(true) }
    val confirmEnabled = remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }


    LaunchedEffect(uiState.task) {
        title = uiState.task.title
        description = uiState.task.description
        priority = uiState.task.priority.toPriority()
        dueDate = uiState.task.dueDate
        dueDateExists = uiState.task.dueDate != 0L
        completed = uiState.task.isCompleted
        subTasks.addAll(uiState.task.subTasks)
        recurring = uiState.task.recurring
        frequency = uiState.task.frequency
        frequencyAmount = uiState.task.frequencyAmount
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
                subTasks = subTasks,
                recurring = recurring,
                frequency = frequency,
                frequencyAmount = frequencyAmount
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
                title = {
                    Text(
                        text = "Edit Task",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    IconButton(onClick = { openDialog = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.delete_icon),
                            contentDescription = stringResource(R.string.delete_task),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        updateTaskIfChanged(
                            uiState.task,
                            uiState.task.copy(
                                title = title,
                                description = description,
                                dueDate = if (dueDateExists) dueDate else 0L,
                                priority = priority.toInt(),
                                subTasks = subTasks,
                                recurring = recurring,
                                frequency = frequency
                            ),
                            {
                                navController.popBackStack(Screen.TaskSearchScreen.route, false)
                                navController.navigateUp()
                            }
                        ) {
                            viewModel.onEvent(
                                TaskEvent.UpdateTask(
                                    it,
                                    dueDate != uiState.task.dueDate
                                )
                            )
                        }

                        navController.navigateUp()
                    })
                    {
                        Image(
                            painter = painterResource(id = R.drawable.backarrow_ic),
                            contentDescription = "back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        }
    ) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, bottom = 20.dp, top = 67.dp, end = 16.dp)
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
                    modifier = Modifier.size(15.dp),
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(
                        id = R.string.add_sub_task
                    )
                )
            }


            Spacer(Modifier.height(12.dp))
            Divider()
            Text(
                text = stringResource(R.string.priority),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(15.dp))

            PriorityTabRow(
                priorities = priorities,
                priority,
                onChange = { priority = it }
            )
            Spacer(Modifier.height(12.dp))
            Divider()

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = dueDateExists,
                    onCheckedChange = {
                        dueDateExists = it
                        if (it)
                            dueDate = Calendar.getInstance().timeInMillis
                    },
                    colors = CheckboxDefaults.colors(Color.DarkGray)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.due_date),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            AnimatedVisibility(dueDateExists) {
                Column {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                showTimePicker = true
                            }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {

                        val date =
                            if (dueDate == 0L) Calendar.getInstance() else Calendar
                                .getInstance()
                                .apply { timeInMillis = dueDate }

                        if (showTimePicker) {

                            TimePickerDialog(
                                onCancel = { showTimePicker = false },
                                onConfirm = {
                                    val cal = Calendar.getInstance()
                                    cal.set(Calendar.HOUR_OF_DAY, timeState.hour)
                                    cal.set(Calendar.MINUTE, timeState.minute)
                                    cal.isLenient = false
                                    dueDate = cal.timeInMillis

                                    date[Calendar.HOUR_OF_DAY]
                                    date[Calendar.MINUTE]
                                    false


                                    snackScope.launch {
                                        snackState.showSnackbar("Entered time: ${formatter.format(cal.time)}")
                                    }
                                    showTimePicker = false
                                },
                            ) {
                                TimePicker(
                                    state = timeState,
                                    colors = TimePickerDefaults.colors(
                                        timeSelectorSelectedContainerColor = Color(0xE18260BE),
                                        selectorColor = Color(0xFFB79AE9),
                                    )
                                )
                            }
                        }


                        if (opendateDialog.value) {

                            DatePickerDialog(
                                onDismissRequest = {
                                    opendateDialog.value = false
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            opendateDialog.value = false
                                            snackScope.launch {
                                                snackState.showSnackbar(
                                                    "Selected date timestamp: ${datePickerState.selectedDateMillis}"
                                                )
                                            }
                                            showTimePicker = true

                                            date[Calendar.YEAR]
                                            date[Calendar.MONTH]
                                            date[Calendar.DAY_OF_MONTH]

                                        },
                                        enabled = confirmEnabled.value
                                    ) {
                                        Text("OK")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            opendateDialog.value = false
                                        }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    colors = DatePickerDefaults.colors(
                                        selectedDayContainerColor = Color(0xD78260BE)
                                    )
                                )
                            }
                        }

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
                            text = formattedDate,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = recurring,
                            colors = CheckboxDefaults.colors(Color.DarkGray),
                            onCheckedChange = {
                                recurring = it
                                if (!it) frequency = 0
                            })
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.recurring),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    AnimatedVisibility(recurring) {
                        var frequencyMenuExpanded by remember { mutableStateOf(false) }
                        Column {
                            Box {
                                DropdownMenu(
                                    expanded = frequencyMenuExpanded,
                                    onDismissRequest = { frequencyMenuExpanded = false }) {
                                    TaskFrequency.values().forEach { f ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = stringResource(f.title),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            },
                                            onClick = {
                                                frequencyMenuExpanded = false
                                                frequency = f.ordinal
                                            }
                                        )
                                    }
                                }
                                Row(
                                    Modifier
                                        .clickable { frequencyMenuExpanded = true }
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(
                                            frequency.toTaskFrequency().title
                                        )
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = stringResource(R.string.recurring),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            NumberPicker(
                                stringResource(R.string.repeats_every),
                                frequencyAmount
                            ) {
                                if (it > 0) frequencyAmount = it
                            }
                        }
                    }
                }

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
            title = {
                Text(
                    stringResource(R.string.delete_task_confirmation_title),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    stringResource(
                        R.string.delete_task_confirmation_message,
                        uiState.task.title
                    ),
                    style = MaterialTheme.typography.bodyMedium
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
                    Text(
                        stringResource(R.string.delete_task),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            dismissButton = {
                Button(
                    shape = RoundedCornerShape(25.dp),
                    onClick = {
                        openDialog = false
                    }) {
                    Text(
                        stringResource(R.string.cancel),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
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
            task.subTasks != newTask.subTasks ||
            task.recurring != newTask.recurring ||
            task.frequency != newTask.frequency ||
            task.frequencyAmount != newTask.frequencyAmount
}