package com.implementing.cozyspace.inappscreens.diary.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.diary.DiaryEvent
import com.implementing.cozyspace.inappscreens.diary.viewmodel.DiaryViewModel
import com.implementing.cozyspace.model.Diary
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.util.Mood
import com.implementing.cozyspace.util.fullDate
import java.util.Calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.implementing.cozyspace.inappscreens.task.screens.PresentAndFutureSelectableDates


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryEntryDetailsScreen(
    navController: NavHostController,
    entryId: Int,
    viewModel: DiaryViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        if (entryId != -1) {
            viewModel.onEvent(DiaryEvent.GetEntry(entryId))
        }
    }
    val state = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }

    var openDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf(state.entry?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(state.entry?.content ?: "") }
    var mood by rememberSaveable { mutableStateOf(state.entry?.mood ?: Mood.AWESOME) }
    var date by rememberSaveable {
        mutableStateOf(
            state.entry?.createdDate ?: System.currentTimeMillis()
        )
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = PresentAndFutureSelectableDates
    )
    val timePickerState = rememberTimePickerState(
        initialHour = Calendar.getInstance().apply { timeInMillis = date }
            .get(Calendar.HOUR_OF_DAY),
        initialMinute = Calendar.getInstance().apply { timeInMillis = date }
            .get(Calendar.MINUTE),
        is24Hour = false
    )
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.entry) {
        if (state.entry != null) {
            title = state.entry.title
            content = state.entry.content
            date = state.entry.createdDate
            mood = state.entry.mood
        }
    }
    LaunchedEffect(state) {
        if (state.navigateUp) {
            openDialog = false
            navController.popBackStack(route = Screen.DiaryScreen.route, inclusive = false)
        }
        if (state.error != null) {
            snackbarHostState.showSnackbar(state.error)
            viewModel.onEvent(DiaryEvent.ErrorDisplayed)
        }
    }
    BackHandler {
        if (state.entry != null) {
            val entry = state.entry.copy(
                title = title,
                content = content,
                mood = mood,
                createdDate = date,
                updatedDate = System.currentTimeMillis()
            )
            if (entryChanged(state.entry, entry))
                viewModel.onEvent(DiaryEvent.UpdateEntry(entry))
            else navController.popBackStack(route = Screen.DiaryScreen.route, inclusive = false)
        } else
            navController.popBackStack(route = Screen.DiaryScreen.route, inclusive = false)
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Entry", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    if (state.entry != null) IconButton(onClick = { openDialog = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.delete_icon),
                            contentDescription = stringResource(R.string.delete_entry),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.backarrow_ic),
                            contentDescription = "back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (state.entry == null) {
                        val entry = Diary(
                            title = title,
                            content = content,
                            mood = mood,
                            createdDate = date,
                            updatedDate = System.currentTimeMillis()
                        )
                        viewModel.onEvent(DiaryEvent.AddEntry(entry))
                    } else {
                        val entry = state.entry.copy(
                            title = title,
                            content = content,
                            mood = mood,
                            createdDate = date,
                            updatedDate = System.currentTimeMillis()
                        )
                        viewModel.onEvent(DiaryEvent.UpdateEntry(entry))
                    }
                    navController.popBackStack(
                        route = Screen.DiaryScreen.route,
                        inclusive = false
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.save_ic),
                    contentDescription = stringResource(R.string.save_entry),
                    modifier = Modifier.size(25.dp),
                    tint = MaterialTheme.colorScheme.scrim
                )
            }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 70.dp, start = 14.dp, end = 14.dp, bottom = 14.dp)
        ) {
            Text(
                text = stringResource(R.string.mood),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(Modifier.height(12.dp))

            EntryMoodSection(
                currentMood = mood,
            ) { mood = it }

            Spacer(Modifier.height(9.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = stringResource(R.string.title)) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(9.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(text = stringResource(R.string.content)) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(15.dp))

            TextButton(
                elevation = ButtonDefaults.elevatedButtonElevation(2.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF221F3E)),
                modifier = Modifier.align(Alignment.End),
                onClick = { showDatePicker = true }
            ) {
                Text(
                    text = date.fullDate(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { selectedDate ->
                                showDatePicker = false
                                showTimePicker = true
                            }
                        }
                    ) { Text("Next") }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) { Text("Cancel") }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContentColor = Color.LightGray,
                        selectedYearContainerColor = Color(0xFF7C60A1),
                        selectedDayContainerColor = Color(0xFF7C60A1),
                    )
                )
            }
        }

        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = datePickerState.selectedDateMillis ?: date
                                set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                set(Calendar.MINUTE, timePickerState.minute)
                            }
                            date = calendar.timeInMillis
                            showTimePicker = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showTimePicker = false }
                    ) { Text("Cancel") }
                }
            ) {
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        timeSelectorSelectedContainerColor = Color(0xFF7B4BCE), // Solid deep purple
                        selectorColor = Color(0xFFB388FF),                    // Mid-tone purple
                        timeSelectorSelectedContentColor = Color.White,       // White for contrast
                        timeSelectorUnselectedContentColor = Color.White , // Neutral gray for unselected
                        timeSelectorUnselectedContainerColor = Color.DarkGray
                    )
                )
            }
        }

        if (openDialog)
            AlertDialog(
                shape = RoundedCornerShape(25.dp),
                onDismissRequest = { openDialog = false },
                title = {
                    Text(
                        stringResource(R.string.delete_diary_entry_confirmation_title),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Text(
                        stringResource(R.string.delete_diary_entry_confirmation_message),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            viewModel.onEvent(DiaryEvent.DeleteEntry(state.entry!!))
                        },
                    ) {
                        Text(
                            stringResource(R.string.delete_entry),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                },
                dismissButton = {
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = { openDialog = false }
                    ) {
                        Text(
                            stringResource(R.string.cancel),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            )
    }
}

@Composable
fun EntryMoodSection(
    currentMood: Mood,
    onMoodChange: (Mood) -> Unit
) {
    val moods = listOf(Mood.AWESOME, Mood.GOOD, Mood.OKAY, Mood.SLEEPY, Mood.BAD, Mood.TERRIBLE)
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        moods.forEach { mood ->
            MoodItem(
                mood = mood,
                chosen = mood == currentMood,
                onMoodChange = { onMoodChange(mood) }
            )
        }
    }
}

@Composable
private fun MoodItem(mood: Mood, chosen: Boolean, onMoodChange: () -> Unit) {
    Box(Modifier.clip(RoundedCornerShape(8.dp))) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable { onMoodChange() }
                .padding(6.dp)
        ) {
            val borderColor = mood.color
            val borderWidth = 2.dp

            Image(
                painter = painterResource(id = mood.icon),
                contentDescription = stringResource(mood.title),
                modifier = Modifier
                    .size(32.dp)
                    .border(
                        border = BorderStroke(
                            width = borderWidth,
                            color = if (chosen) borderColor else Color.Transparent
                        ),
                        shape = CircleShape
                    )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(mood.title),
                overflow = TextOverflow.Ellipsis,
                color = if (chosen) mood.color else Color.Gray,
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}

private fun entryChanged(entry: Diary?, newEntry: Diary): Boolean {
    return entry?.title != newEntry.title ||
            entry.content != newEntry.content ||
            entry.mood != newEntry.mood ||
            entry.createdDate != newEntry.createdDate
}

