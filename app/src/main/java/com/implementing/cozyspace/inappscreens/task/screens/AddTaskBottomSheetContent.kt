package com.implementing.cozyspace.inappscreens.task.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.task.SubTaskItem
import com.implementing.cozyspace.inappscreens.task.screens.widgets.NumberPicker
import com.implementing.cozyspace.model.SubTask
import com.implementing.cozyspace.model.Task
import com.implementing.cozyspace.util.Priority
import com.implementing.cozyspace.util.TaskFrequency
import com.implementing.cozyspace.util.formatDateDependingOnDay
import com.implementing.cozyspace.util.formatTime
import com.implementing.cozyspace.util.toInt
import com.implementing.cozyspace.util.toPriority
import com.implementing.cozyspace.util.toTaskFrequency
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("StringFormatInvalid")
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
    var recurring by rememberSaveable { mutableStateOf(false) }
    var frequency by rememberSaveable { mutableIntStateOf(0) }
    var frequencyAmount by rememberSaveable { mutableIntStateOf(0) }
    val priorities = listOf(Priority.LOW, Priority.MEDIUM, Priority.HIGH)
    val context = LocalContext.current


    val timeState = rememberTimePickerState()
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    var showTimePicker by remember { mutableStateOf(true) }

    var openDialog by remember { mutableStateOf(true) }


    Column(
        modifier = Modifier
            .defaultMinSize(minHeight = 1.dp)
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SheetHandle(Modifier.align(Alignment.CenterHorizontally))
        Text(
            text = stringResource(R.string.add_task),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp
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
                style = MaterialTheme.typography.bodyMedium,
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
        Divider()

        Spacer(Modifier.height(13.dp))

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

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = dueDateExists,
                onCheckedChange = { dueDateExists = it },
                colors = CheckboxDefaults.colors(Color.DarkGray)
            )

            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.due_date),
                style = MaterialTheme.typography.bodyMedium,
            )
        }


        AnimatedVisibility(dueDateExists) {
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            showTimePicker = true
                            openDialog = true
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {

                    if (showTimePicker) {
                        TimePickerDialog(
                            onCancel = { showTimePicker = false },
                            onConfirm = {

                                // Construct the Calendar object with the selected time
                                val cal = Calendar.getInstance()
                                // Update the dueDate with the selected time
                                dueDate.set(Calendar.HOUR_OF_DAY, timeState.hour)
                                dueDate.set(Calendar.MINUTE, timeState.minute)

                                // Show Snackbar with selected time
                                val formattedTime = SimpleDateFormat(
                                    "yyyy-MM-dd hh:mm a",
                                    Locale.getDefault()
                                ).format(cal.time)
                                snackScope.launch {
                                    snackState.showSnackbar("Selected time: $formattedTime")
                                }

                                // Close the time picker
                                showTimePicker = false
                            }
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


                    if (openDialog) {
                        val datePickerState = rememberDatePickerState()
                        val confirmEnabled = remember {
                            derivedStateOf { datePickerState.selectedDateMillis != null }
                        }

                        DatePickerDialog(
                            onDismissRequest = {
                                openDialog = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        val selectedDateMillis = datePickerState.selectedDateMillis ?: Calendar.getInstance().timeInMillis
                                        // Update the dueDate with the selected date
                                        dueDate = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }

                                        openDialog = false
                                        // Show the time picker after selecting the date
                                        showTimePicker = true
                                        // Show Snackbar with selected date
                                        snackScope.launch {
                                            snackState.showSnackbar(
                                                context.getString(
                                                    R.string.selected_date,
                                                    dueDate.timeInMillis.formatDateDependingOnDay()
                                                )
                                            )
                                        }
                                    },
                                    enabled = confirmEnabled.value
                                ) {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        openDialog = false
                                    }
                                ) {
                                    Text("Cancel")
                                }
                            }
                        ) {
                            val minDate = Calendar.getInstance().apply {
                                // Clear the time fields to set the time to midnight
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }.timeInMillis

                            DatePicker(
                                state = datePickerState,
                                colors = DatePickerDefaults.colors(
                                    selectedDayContainerColor = Color(0xD78260BE)
                                ),
                                dateValidator = { date ->
                                    // Check if the date is after or equal to the minimum date
                                    date >= minDate
                                },

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
                        text = dueDate.timeInMillis.formatDateDependingOnDay(),
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
                    var expanded by remember { mutableStateOf(false) }
                    Column {
                        Box {
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }) {
                                TaskFrequency.values().forEach { f ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(f.title),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        },
                                        onClick = {
                                            expanded = false
                                            frequency = f.ordinal
                                        }
                                    )
                                }
                            }
                            Row(
                                Modifier
                                    .clickable { expanded = true }
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

        Spacer(Modifier.height(11.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = stringResource(R.string.description)) },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(13.dp))

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            onClick = {
                onAddTask(
                    Task(
                        title = title,
                        description = description,
                        priority = priority.toInt(),
                        recurring = recurring,
                        frequency = frequency,
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
        modifier = Modifier.clip(RoundedCornerShape(14.dp)),
        contentColor = Color.Black
    ) {
        priorities.forEachIndexed { index, it ->
            Tab(
                text = {
                    Text(
                        stringResource(it.title),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                selected = selectedPriority.toInt() == index,
                onClick = {
                    onChange(index.toPriority())
                },
                modifier = Modifier.background(it.color),
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


@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}