package com.implementing.cozyspace.inappscreens.calendar.screen


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.gson.Gson
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.calendar.CalendarEventsVM
import com.implementing.cozyspace.inappscreens.calendar.viewmodel.CalendarViewModel
import com.implementing.cozyspace.inappscreens.task.screens.PresentAndFutureSelectableDates
import com.implementing.cozyspace.model.Calendar
import com.implementing.cozyspace.model.CalendarEvent
import com.implementing.cozyspace.util.CALENDAR_FREQ_DAILY
import com.implementing.cozyspace.util.CALENDAR_FREQ_MONTHLY
import com.implementing.cozyspace.util.CALENDAR_FREQ_NEVER
import com.implementing.cozyspace.util.CALENDAR_FREQ_WEEKLY
import com.implementing.cozyspace.util.CALENDAR_FREQ_YEARLY
import com.implementing.cozyspace.util.HOUR_IN_MILLIS
import com.implementing.cozyspace.util.formatDate
import com.implementing.cozyspace.util.formatTime
import com.implementing.cozyspace.util.toUIFrequency
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CalendarEventDetailsScreen(
    navController: NavHostController,
    eventJson: String = "",
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val writeCalendarPermissionState = rememberPermissionState(permission = Manifest.permission.WRITE_CALENDAR)

    var openDeleteDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val event by remember {
        mutableStateOf(
            if (eventJson.isNotEmpty()) {
                val decodedJson = URLDecoder.decode(eventJson, StandardCharsets.UTF_8.toString())
                Gson().fromJson(decodedJson, CalendarEvent::class.java)
            } else null
        )
    }
    var title by rememberSaveable { mutableStateOf(event?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(event?.description ?: "") }
    var startDate by rememberSaveable {
        mutableStateOf(event?.start ?: System.currentTimeMillis())
    }
    var endDate by rememberSaveable {
        mutableStateOf(event?.end ?: (System.currentTimeMillis() + HOUR_IN_MILLIS))
    }
    var frequency by rememberSaveable { mutableStateOf(event?.frequency ?: CALENDAR_FREQ_NEVER) }
    var calendar by remember {
        mutableStateOf(
            Calendar(id = 1, name = "", color = Color.Black.toArgb(), account = "")
        )
    }
    LaunchedEffect(state.calendarsList) {
        if (event != null && state.calendarsList.isNotEmpty()) {
            calendar = state.calendarsList.first { it.id == event!!.calendarId }
        } else if (state.calendarsList.isNotEmpty()) {
            calendar = state.calendarsList.first()
        }
    }

    var allDay by rememberSaveable { mutableStateOf(event?.allDay ?: false) }
    var location by rememberSaveable { mutableStateOf(event?.location ?: "") }
    val snackbarHostState = remember { SnackbarHostState() }

    if (writeCalendarPermissionState.status.isGranted) {
        LaunchedEffect(true) { viewModel.onEvent(CalendarEventsVM.ReadPermissionChanged(true)) }
        LaunchedEffect(state) {
            if (state.navigateUp) {
                openDeleteDialog = false
                navController.navigateUp()
            }
            if (state.error != null) {
                snackbarHostState.showSnackbar(state.error)
                viewModel.onEvent(CalendarEventsVM.ErrorDisplayed)
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                val topBarContent = if (eventJson.isNotBlank()) {
                    Pair("Edit an Event", true)
                } else {
                    Pair("Add an Event", false)
                }
                TopAppBar(
                    title = { Text(text = topBarContent.first, style = MaterialTheme.typography.titleLarge) },
                    actions = {
                        if (eventJson.isNotBlank()) {
                            IconButton(onClick = { openDeleteDialog = topBarContent.second }) {
                                Image(
                                    painter = painterResource(id = R.drawable.delete_icon),
                                    contentDescription = stringResource(R.string.delete_event),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Image(
                                painter = painterResource(id = R.drawable.backarrow_ic),
                                contentDescription = "back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    val newEvent = CalendarEvent(
                        id = event?.id ?: 0,
                        title = title,
                        description = description,
                        start = startDate,
                        end = endDate,
                        allDay = allDay,
                        location = location,
                        calendarId = calendar.id,
                        recurring = frequency != CALENDAR_FREQ_NEVER,
                        frequency = frequency
                    )
                    if (event != null) {
                        viewModel.onEvent(CalendarEventsVM.EditEvent(newEvent))
                    } else {
                        viewModel.onEvent(CalendarEventsVM.AddEvent(newEvent))
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.save_ic),
                        contentDescription = stringResource(R.string.add_event),
                        tint = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        ) { paddingValues ->
            DeleteEventDialog(
                openDialog = openDeleteDialog,
                onDelete = { viewModel.onEvent(CalendarEventsVM.DeleteEvent(event!!)) },
                onDismiss = { openDeleteDialog = false }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 55.dp, start = 20.dp, end = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = stringResource(R.string.title)) },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                CalendarChoiceSection(
                    selectedCalendar = calendar,
                    calendars = state.calendarsList,
                    onCalendarSelected = { calendar = it }
                )
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))

                EventTimeSection(
                    start = java.util.Calendar.getInstance().apply { timeInMillis = startDate },
                    end = java.util.Calendar.getInstance().apply { timeInMillis = endDate },
                    onStartDateSelected = { startDate = it.timeInMillis },
                    onEndDateSelected = { endDate = it.timeInMillis },
                    allDay = allDay,
                    onAllDayChange = { allDay = it },
                    frequency = frequency,
                    onFrequencySelected = { frequency = it },
                    onMultipleDatesSelected = { dates ->
                        // Optionally handle multiple dates here, e.g., set start and end from the range
                        if (dates.isNotEmpty()) {
                            startDate = dates.first().timeInMillis
                            endDate = dates.last().timeInMillis
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(text = stringResource(R.string.location), style = MaterialTheme.typography.bodyMedium) },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_location), null) },
                    textStyle = TextStyle(fontSize = 12.sp)
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = stringResource(R.string.description), style = MaterialTheme.typography.bodyMedium) },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_description), null) },
                    textStyle = TextStyle(fontSize = 12.sp)
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    } else {
        LaunchedEffect(true) { viewModel.onEvent(CalendarEventsVM.ReadPermissionChanged(false)) }
        NoWriteCalendarPermissionMessage(
            shouldShowRationale = writeCalendarPermissionState.status.shouldShowRationale,
            context = context
        ) {
            writeCalendarPermissionState.launchPermissionRequest()
        }
    }
}

@Composable
fun NoWriteCalendarPermissionMessage(
    shouldShowRationale: Boolean,
    context: Context,
    onRequest: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Text(
            text = stringResource(R.string.no_write_calendar_permission_message),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        if (shouldShowRationale) {
            TextButton(onClick = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", context.packageName, null)
                context.startActivity(intent)
            }) {
                Text(text = stringResource(R.string.go_to_settings))
            }
        } else {
            TextButton(
                onClick = { onRequest() },
                colors = ButtonDefaults.textButtonColors(containerColor = Color(0xFF221F3E), contentColor = Color.White)
            ) {
                Text(text = stringResource(R.string.grant_permission), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun CalendarChoiceSection(
    selectedCalendar: Calendar,
    calendars: List<Calendar>,
    onCalendarSelected: (Calendar) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(selectedCalendar.color))
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(text = selectedCalendar.name, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text(text = selectedCalendar.account, style = MaterialTheme.typography.bodyMedium)
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            calendars.forEach { calendar ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(calendar.color))
                            )
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(text = calendar.name, style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.height(4.dp))
                                Text(text = calendar.account, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    },
                    onClick = {
                        expanded = false
                        onCalendarSelected(calendar)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTimeSection(
    start: java.util.Calendar,
    end: java.util.Calendar,
    onStartDateSelected: (java.util.Calendar) -> Unit,
    onEndDateSelected: (java.util.Calendar) -> Unit,
    allDay: Boolean,
    onAllDayChange: (Boolean) -> Unit,
    frequency: String,
    onFrequencySelected: (String) -> Unit,
    onMultipleDatesSelected: (List<java.util.Calendar>) -> Unit
) {
    val context = LocalContext.current

    // State for showing Date and Time Pickers
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showMultiDatePicker by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    // Current time as default
    val currentTime = java.util.Calendar.getInstance()
    val initialStartMillis = start.timeInMillis.takeIf { it > 0 } ?: currentTime.timeInMillis
    val initialEndMillis = end.timeInMillis.takeIf { it > 0 } ?: (currentTime.timeInMillis + HOUR_IN_MILLIS)

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(R.drawable.ic_time), null)
                Spacer(Modifier.width(8.dp))
                Text(text = stringResource(R.string.all_day), style = MaterialTheme.typography.bodyMedium)
            }
            Switch(
                checked = allDay,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF876BCE),
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                ),
                onCheckedChange = { onAllDayChange(it) }
            )
        }

        // Start Date and Time
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = start.timeInMillis.formatDate(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable { showStartDatePicker = true }
                    .padding(horizontal = 28.dp, vertical = 16.dp)
            )
            Text(
                text = start.timeInMillis.formatTime(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable { showStartTimePicker = true }
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            )
        }

        // End Date and Time
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = end.timeInMillis.formatDate(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable { showEndDatePicker = true }
                    .padding(horizontal = 28.dp, vertical = 16.dp)
            )
            Text(
                text = end.timeInMillis.formatTime(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable { showEndTimePicker = true }
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            )
        }

        // Frequency and Multi-Date Option
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable { openDialog = true }
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_refresh), null)
                Spacer(Modifier.width(8.dp))
                Text(frequency.toUIFrequency(), style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                text = "Multiple Dates",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clickable { showMultiDatePicker = true }
                    .padding(horizontal = 8.dp)
            )
        }

        var openDialog by remember { mutableStateOf(false) }
        FrequencyDialog(
            selectedFrequency = frequency,
            onFrequencySelected = {
                onFrequencySelected(it)
                openDialog = false
            },
            open = openDialog,
            onClose = { openDialog = false }
        )

        // Material 3 Date Picker for Start Date
        if (showStartDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialStartMillis,
                selectableDates = PresentAndFutureSelectableDates
            )
            DatePickerDialog(
                onDismissRequest = { showStartDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newEvent = java.util.Calendar.getInstance().apply {
                                timeInMillis = millis
                                set(java.util.Calendar.HOUR_OF_DAY, start.get(java.util.Calendar.HOUR_OF_DAY))
                                set(java.util.Calendar.MINUTE, start.get(java.util.Calendar.MINUTE))
                            }
                            onStartDateSelected(newEvent)
                            if (newEvent.timeInMillis > end.timeInMillis) {
                                onEndDateSelected(newEvent.apply { timeInMillis += HOUR_IN_MILLIS })
                            }
                        }
                        showStartDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showStartDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContentColor = Color.LightGray,
                        selectedYearContainerColor = Color(0xFF7C60A1),
                        selectedDayContainerColor = Color(0xFF7C60A1)
                    )
                )
            }
        }

        // Material 3 Date Picker for End Date
        if (showEndDatePicker) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialEndMillis)
            DatePickerDialog(
                onDismissRequest = { showEndDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val newEvent = java.util.Calendar.getInstance().apply {
                                timeInMillis = millis
                                set(java.util.Calendar.HOUR_OF_DAY, end.get(java.util.Calendar.HOUR_OF_DAY))
                                set(java.util.Calendar.MINUTE, end.get(java.util.Calendar.MINUTE))
                            }
                            onEndDateSelected(newEvent)
                            if (newEvent.timeInMillis < start.timeInMillis) {
                                onStartDateSelected(newEvent.apply { timeInMillis -= HOUR_IN_MILLIS })
                            }
                        }
                        showEndDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showEndDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(
                        selectedDayContentColor = Color.LightGray,
                        selectedYearContainerColor = Color(0xFF7C60A1),
                        selectedDayContainerColor = Color(0xFF7C60A1)
                    )
                )
            }
        }

        // Material 3 Time Picker for Start Time
        if (showStartTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = currentTime.get(java.util.Calendar.HOUR_OF_DAY),
                initialMinute = currentTime.get(java.util.Calendar.MINUTE),
                is24Hour = false
            )
            AlertDialog(
                onDismissRequest = { showStartTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val newEvent = java.util.Calendar.getInstance().apply {
                            set(java.util.Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(java.util.Calendar.MINUTE, timePickerState.minute)
                            set(java.util.Calendar.YEAR, start.get(java.util.Calendar.YEAR))
                            set(java.util.Calendar.MONTH, start.get(java.util.Calendar.MONTH))
                            set(java.util.Calendar.DAY_OF_MONTH, start.get(java.util.Calendar.DAY_OF_MONTH))
                        }
                        onStartDateSelected(newEvent)
                        if (newEvent.timeInMillis > end.timeInMillis) {
                            onEndDateSelected(newEvent.apply { timeInMillis += HOUR_IN_MILLIS })
                        }
                        showStartTimePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showStartTimePicker = false }) { Text("Cancel") }
                },
                text = {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            timeSelectorSelectedContainerColor = Color(0xFF7B4BCE),
                            selectorColor = Color(0xFFB388FF),
                            timeSelectorSelectedContentColor = Color.White,
                            timeSelectorUnselectedContentColor = Color.White,
                            timeSelectorUnselectedContainerColor = Color.DarkGray
                        )
                    )
                }
            )
        }

        // Material 3 Time Picker for End Time
        if (showEndTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = currentTime.get(java.util.Calendar.HOUR_OF_DAY),
                initialMinute = currentTime.get(java.util.Calendar.MINUTE),
                is24Hour = false
            )
            AlertDialog(
                onDismissRequest = { showEndTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val newEvent = java.util.Calendar.getInstance().apply {
                            set(java.util.Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(java.util.Calendar.MINUTE, timePickerState.minute)
                            set(java.util.Calendar.YEAR, end.get(java.util.Calendar.YEAR))
                            set(java.util.Calendar.MONTH, end.get(java.util.Calendar.MONTH))
                            set(java.util.Calendar.DAY_OF_MONTH, end.get(java.util.Calendar.DAY_OF_MONTH))
                        }
                        onEndDateSelected(newEvent)
                        if (newEvent.timeInMillis < start.timeInMillis) {
                            onStartDateSelected(newEvent.apply { timeInMillis -= HOUR_IN_MILLIS })
                        }
                        showEndTimePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showEndTimePicker = false }) { Text("Cancel") }
                },
                text = {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(
                            timeSelectorSelectedContainerColor = Color(0xFF7B4BCE),
                            timeSelectorUnselectedContainerColor = Color.DarkGray
                        )
                    )
                }
            )
        }

        // Material 3 Multi-Date Picker
        if (showMultiDatePicker) {
            val dateRangePickerState = rememberDateRangePickerState(
                initialSelectedStartDateMillis = initialStartMillis,
                initialSelectedEndDateMillis = initialEndMillis
            )
            DatePickerDialog(
                onDismissRequest = { showMultiDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val startMillis = dateRangePickerState.selectedStartDateMillis
                        val endMillis = dateRangePickerState.selectedEndDateMillis
                        if (startMillis != null && endMillis != null) {
                            val dates = mutableListOf<java.util.Calendar>()
                            var currentMillis = startMillis
                            while (currentMillis <= endMillis) {
                                val cal = java.util.Calendar.getInstance().apply {
                                    timeInMillis = currentMillis
                                    set(java.util.Calendar.HOUR_OF_DAY, start.get(java.util.Calendar.HOUR_OF_DAY))
                                    set(java.util.Calendar.MINUTE, start.get(java.util.Calendar.MINUTE))
                                }
                                dates.add(cal)
                                currentMillis += 24 * HOUR_IN_MILLIS
                            }
                            onMultipleDatesSelected(dates)
                        }
                        showMultiDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showMultiDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DateRangePicker(
                    state = dateRangePickerState,
                    modifier = Modifier.padding(16.dp),
                    title = { Text("Select Date Range") },
                    colors = DatePickerDefaults.colors(
                        selectedDayContentColor = Color.LightGray,
                        selectedYearContainerColor = Color(0xFF7C60A1),
                        selectedDayContainerColor = Color(0xFF7C60A1)
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyDialog(
    selectedFrequency: String,
    onFrequencySelected: (String) -> Unit,
    open: Boolean,
    onClose: () -> Unit
) {
    val frequencies = listOf(
        CALENDAR_FREQ_NEVER,
        CALENDAR_FREQ_DAILY,
        CALENDAR_FREQ_WEEKLY,
        CALENDAR_FREQ_MONTHLY,
        CALENDAR_FREQ_YEARLY
    )
    if (open) {
        BasicAlertDialog(
            onDismissRequest = { onClose() },
            content = {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    frequencies.forEach { frequency ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = frequency.toUIFrequency(), style = MaterialTheme.typography.bodyMedium)
                            RadioButton(
                                selected = frequency == selectedFrequency,
                                onClick = { onFrequencySelected(frequency) }
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun DeleteEventDialog(
    openDialog: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            shape = RoundedCornerShape(25.dp),
            onDismissRequest = { onDismiss() },
            title = { Text(stringResource(R.string.delete_event_confirmation_title)) },
            text = { Text(stringResource(R.string.delete_event_confirmation_message)) },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(25.dp),
                    onClick = { onDelete() }
                ) {
                    Text(stringResource(R.string.delete_event), color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    shape = RoundedCornerShape(25.dp),
                    onClick = { onDismiss() }
                ) {
                    Text(stringResource(R.string.cancel), color = Color.White)
                }
            }
        )
    }
}


//
//@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun CalendarEventDetailsScreen(
//    navController: NavHostController,
//    eventJson: String = "",
//    viewModel: CalendarViewModel = hiltViewModel()
//) {
//    val state = viewModel.uiState
//    val writeCalendarPermissionState =
//        rememberPermissionState(permission = Manifest.permission.WRITE_CALENDAR)
//
//    var openDeleteDialog by rememberSaveable { mutableStateOf(false) }
//    val context = LocalContext.current
//    val event by remember {
//        mutableStateOf(
//            if (eventJson.isNotEmpty()) {
//                val decodedJson = URLDecoder.decode(eventJson, StandardCharsets.UTF_8.toString())
//                Gson().fromJson(decodedJson, CalendarEvent::class.java)
//            } else null
//        )
//    }
//    var title by rememberSaveable { mutableStateOf(event?.title ?: "") }
//    var description by rememberSaveable { mutableStateOf(event?.description ?: "") }
//    var startDate by rememberSaveable {
//        mutableStateOf(
//            event?.start ?: (System.currentTimeMillis() + HOUR_IN_MILLIS)
//        )
//    }
//    var endDate by rememberSaveable {
//        mutableStateOf(
//            event?.end ?: (System.currentTimeMillis() + 2 * HOUR_IN_MILLIS)
//        )
//    }
//    var frequency by rememberSaveable { mutableStateOf(event?.frequency ?: CALENDAR_FREQ_NEVER) }
//    var calendar by remember {
//        mutableStateOf(
//            Calendar(
//                id = 1, name = "", color = Color.Black.toArgb(), account = ""
//            )
//        )
//    }
//    LaunchedEffect(state.calendarsList) {
//        if (event != null) {
//            if (state.calendarsList.isNotEmpty()) {
//                calendar = state.calendarsList.first { it.id == event!!.calendarId }
//            }
//        } else {
//            if (state.calendarsList.isNotEmpty()) {
//                calendar = state.calendarsList.first()
//            }
//        }
//    }
//
//    var allDay by rememberSaveable { mutableStateOf(event?.allDay ?: false) }
//    var location by rememberSaveable { mutableStateOf(event?.location ?: "") }
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    if (writeCalendarPermissionState.status.isGranted) {
//        LaunchedEffect(true) { viewModel.onEvent(CalendarEventsVM.ReadPermissionChanged(true)) }
//        LaunchedEffect(state) {
//            if (state.navigateUp) {
//                openDeleteDialog = false
//                navController.navigateUp()
//            }
//            if (state.error != null) {
//                snackbarHostState.showSnackbar(
//                    state.error
//                )
//                viewModel.onEvent(CalendarEventsVM.ErrorDisplayed)
//            }
//        }
//
//        Scaffold(
//            snackbarHost = { SnackbarHost(snackbarHostState) },
//            topBar = {
//                val topBarContent = if (eventJson.isNotBlank()) {
//                    // Edit Event Mode
//                    Pair("Edit an Event", true)
//                } else {
//                    // Add Event Mode
//                    Pair("Add an Event", false)
//                }
//
//
//                TopAppBar(
//                    title = {
//                        Text(
//                            text = topBarContent.first,
//                            style = MaterialTheme.typography.titleLarge
//                        )
//                    },
//                    actions = {
//                        if (eventJson.isNotBlank()) {
//                            IconButton(onClick = { openDeleteDialog = topBarContent.second }) {
//                                Image(
//                                    painter = painterResource(id = R.drawable.delete_icon),
//                                    contentDescription = stringResource(R.string.delete_event),
//                                    modifier = Modifier.size(24.dp)
//                                )
//                            }
//                        }
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = { navController.navigateUp() }) {
//                            Image(
//                                painter = painterResource(id = R.drawable.backarrow_ic),
//                                contentDescription = "back"
//                            )
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//                )
//            },
//            floatingActionButton = {
//                FloatingActionButton(onClick = {
//                    val newEvent = CalendarEvent(
//                        id = event?.id ?: 0,
//                        title = title,
//                        description = description,
//                        start = startDate,
//                        end = endDate,
//                        allDay = allDay,
//                        location = location,
//                        calendarId = calendar.id,
//                        recurring = frequency != CALENDAR_FREQ_NEVER,
//                        frequency = frequency
//                    )
//                    if (event != null) {
//                        viewModel.onEvent(CalendarEventsVM.EditEvent(newEvent))
//                    } else {
//                        viewModel.onEvent(CalendarEventsVM.AddEvent(newEvent))
//                    }
//                }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.save_ic),
//                        contentDescription = stringResource(R.string.add_event),
//                        tint = MaterialTheme.colorScheme.scrim,
//                        modifier = Modifier.size(25.dp)
//                    )
//                }
//            }) { paddingValues ->
//
//            DeleteEventDialog(openDeleteDialog,
//                onDelete = { viewModel.onEvent(CalendarEventsVM.DeleteEvent(event!!)) },
//                onDismiss = { openDeleteDialog = false })
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 55.dp, start = 20.dp, end = 20.dp)
//                    .verticalScroll(rememberScrollState()),
//            ) {
//                OutlinedTextField(
//                    value = title,
//                    onValueChange = { title = it },
//                    label = { Text(text = stringResource(R.string.title)) },
//                    shape = RoundedCornerShape(15.dp),
//                    modifier = Modifier.fillMaxWidth(),
//                )
//                Spacer(Modifier.height(8.dp))
//
//                CalendarChoiceSection(selectedCalendar = calendar,
//                    calendars = state.calendarsList,
//                    onCalendarSelected = { calendar = it })
//                Spacer(Modifier.height(8.dp))
//                HorizontalDivider()
//                Spacer(Modifier.height(8.dp))
//
//                EventTimeSection(start = java.util.Calendar.getInstance()
//                    .apply { timeInMillis = startDate },
//                    end = java.util.Calendar.getInstance().apply { timeInMillis = endDate },
//                    onStartDateSelected = { startDate = it.timeInMillis },
//                    onEndDateSelected = { endDate = it.timeInMillis },
//                    allDay = allDay,
//                    onAllDayChange = { allDay = it },
//                    frequency = frequency,
//                    onFrequencySelected = { frequency = it })
//                Spacer(Modifier.height(8.dp))
//                HorizontalDivider()
//
//                Spacer(Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = location,
//                    onValueChange = { location = it },
//                    label = {
//                        Text(
//                            text = stringResource(R.string.location),
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    },
//                    shape = RoundedCornerShape(15.dp),
//                    modifier = Modifier.fillMaxWidth(),
//                    leadingIcon = {
//                        Icon(painter = painterResource(id = R.drawable.ic_location), null)
//                    },
//                    textStyle = TextStyle(fontSize = 12.sp)
//                )
//
//                Spacer(Modifier.height(12.dp))
//
//                OutlinedTextField(
//                    value = description,
//                    onValueChange = { description = it },
//                    label = {
//                        Text(
//                            text = stringResource(R.string.description),
//                            style = MaterialTheme.typography.bodyMedium
//                        )
//                    },
//                    shape = RoundedCornerShape(15.dp),
//                    modifier = Modifier.fillMaxWidth(),
//                    leadingIcon = {
//                        Icon(painter = painterResource(id = R.drawable.ic_description), null)
//                    },
//                    textStyle = TextStyle(fontSize = 12.sp)
//
//                )
//                Spacer(Modifier.height(8.dp))
//            }
//        }
//    } else {
//        LaunchedEffect(true) { viewModel.onEvent(CalendarEventsVM.ReadPermissionChanged(false)) }
//        NoWriteCalendarPermissionMessage(
//            shouldShowRationale = writeCalendarPermissionState.status.shouldShowRationale,
//            context = context
//        ) {
//            writeCalendarPermissionState.launchPermissionRequest()
//        }
//    }
//}
//
//@Composable
//fun NoWriteCalendarPermissionMessage(
//    shouldShowRationale: Boolean, context: Context, onRequest: () -> Unit
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(15.dp)
//    ) {
//        Text(
//            text = stringResource(R.string.no_write_calendar_permission_message),
//            style = MaterialTheme.typography.bodyMedium,
//            textAlign = TextAlign.Center
//        )
//        Spacer(Modifier.height(12.dp))
//        if (shouldShowRationale) {
//            TextButton(onClick = {
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.data = Uri.fromParts("package", context.packageName, null)
//                context.startActivity(intent)
//            }) {
//                Text(text = stringResource(R.string.go_to_settings))
//            }
//
//        } else {
//            TextButton(
//                onClick = { onRequest() },
//                colors = ButtonDefaults.textButtonColors(
//                    containerColor = Color(0xFF221F3E),
//                    contentColor = Color.White
//                )
//            )
//            {
//                Text(
//                    text = stringResource(R.string.grant_permission),
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun CalendarChoiceSection(
//    selectedCalendar: Calendar, calendars: List<Calendar>, onCalendarSelected: (Calendar) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    Box(
//        Modifier
//            .fillMaxWidth()
//            .clickable { expanded = true }
//            .padding(vertical = 12.dp)) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                Modifier
//                    .size(18.dp)
//                    .clip(CircleShape)
//                    .background(Color(selectedCalendar.color))
//            )
//            Spacer(Modifier.width(8.dp))
//            Column {
//                Text(
//                    text = selectedCalendar.name, style = MaterialTheme.typography.bodyMedium
//                )
//                Spacer(Modifier.height(4.dp))
//                Text(
//                    text = selectedCalendar.account, style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//        ) {
//            calendars.forEach { calendar ->
//                DropdownMenuItem(onClick = {
//                    expanded = false
//                    onCalendarSelected(calendar)
//                }, modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp), text = {
//                    Box(
//                        Modifier
//                            .size(20.dp)
//                            .clip(CircleShape)
//                            .background(Color(calendar.color))
//                    )
//                    Spacer(Modifier.width(8.dp))
//                    Column {
//                        Text(
//                            text = calendar.name, style = MaterialTheme.typography.bodyMedium
//                        )
//                        Spacer(Modifier.height(4.dp))
//                        Text(
//                            text = calendar.account, style = MaterialTheme.typography.bodyMedium
//                        )
//                    }
//
//                })
//            }
//
//            Spacer(Modifier.height(4.dp))
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EventTimeSection(
//    start: java.util.Calendar,
//    end: java.util.Calendar,
//    onStartDateSelected: (java.util.Calendar) -> Unit,
//    onEndDateSelected: (java.util.Calendar) -> Unit,
//    allDay: Boolean,
//    onAllDayChange: (Boolean) -> Unit,
//    frequency: String,
//    onFrequencySelected: (String) -> Unit
//) {
//    val context = LocalContext.current
//
//    // State for showing Date and Time Pickers
//    var showStartDatePicker by remember { mutableStateOf(false) }
//    var showEndDatePicker by remember { mutableStateOf(false) }
//    var showStartTimePicker by remember { mutableStateOf(false) }
//    var showEndTimePicker by remember { mutableStateOf(false) }
//
//    Column {
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(end = 12.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(painter = painterResource(R.drawable.ic_time), null)
//                Spacer(Modifier.width(8.dp))
//                Text(
//                    text = stringResource(R.string.all_day),
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//            Switch(
//                checked = allDay,
//                colors = SwitchDefaults.colors(
//                    checkedThumbColor = Color(0xFF876BCE),
//                    checkedTrackColor = MaterialTheme.colorScheme.primary
//                ),
//                onCheckedChange = { onAllDayChange(it) }
//            )
//        }
//
//        // Start Date and Time
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(
//                text = start.timeInMillis.formatDate(),
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier
//                    .clickable { showStartDatePicker = true }
//                    .padding(horizontal = 28.dp, vertical = 16.dp)
//            )
//            Text(
//                text = start.timeInMillis.formatTime(),
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier
//                    .clickable { showStartTimePicker = true }
//                    .padding(horizontal = 18.dp, vertical = 16.dp)
//            )
//        }
//
//        // End Date and Time
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(
//                text = end.timeInMillis.formatDate(),
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier
//                    .clickable { showEndDatePicker = true }
//                    .padding(horizontal = 28.dp, vertical = 16.dp)
//            )
//            Text(
//                text = end.timeInMillis.formatTime(),
//                style = MaterialTheme.typography.bodyMedium,
//                modifier = Modifier
//                    .clickable { showEndTimePicker = true }
//                    .padding(horizontal = 18.dp, vertical = 16.dp)
//            )
//        }
//
//        // Frequency
//        var openDialog by remember { mutableStateOf(false) }
//        Row(
//            modifier = Modifier
//                .clickable { openDialog = true }
//                .fillMaxWidth()
//                .padding(top = 12.dp, bottom = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Icon(painter = painterResource(id = R.drawable.ic_refresh), null)
//            Spacer(Modifier.width(8.dp))
//            Text(frequency.toUIFrequency(), style = MaterialTheme.typography.bodyMedium)
//            FrequencyDialog(
//                selectedFrequency = frequency,
//                onFrequencySelected = {
//                    onFrequencySelected(it)
//                    openDialog = false
//                },
//                open = openDialog,
//                onClose = { openDialog = false }
//            )
//        }
//
//        // Material 3 Date Picker for Start Date
//        if (showStartDatePicker) {
//            val datePickerState = rememberDatePickerState(
//                selectableDates = PresentAndFutureSelectableDates
//            )
//            DatePickerDialog(
//                onDismissRequest = { showStartDatePicker = false },
//                confirmButton = {
//                    TextButton(onClick = {
//                        datePickerState.selectedDateMillis?.let { millis ->
//                            val newEvent = java.util.Calendar.getInstance().apply {
//                                timeInMillis = millis
//                                set(java.util.Calendar.HOUR_OF_DAY, start.get(java.util.Calendar.HOUR_OF_DAY))
//                                set(java.util.Calendar.MINUTE, start.get(java.util.Calendar.MINUTE))
//                            }
//                            onStartDateSelected(newEvent)
//                            if (newEvent.timeInMillis > end.timeInMillis) {
//                                onEndDateSelected(newEvent.apply { timeInMillis += HOUR_IN_MILLIS })
//                            }
//                        }
//                        showStartDatePicker = false
//                    }) {
//                        Text("OK")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showStartDatePicker = false }) {
//                        Text("Cancel")
//                    }
//                }
//            ) {
//                DatePicker(
//                    state = datePickerState,
//                    colors = DatePickerDefaults.colors(
//                        selectedDayContentColor = Color.LightGray,
//                        selectedYearContainerColor = Color(0xFF7C60A1),
//                        selectedDayContainerColor = Color(0xFF7C60A1),
//                    )
//                )
//            }
//        }
//
//        // Material 3 Date Picker for End Date
//        if (showEndDatePicker) {
//            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = end.timeInMillis)
//            DatePickerDialog(
//                onDismissRequest = { showEndDatePicker = false },
//                confirmButton = {
//                    TextButton(onClick = {
//                        datePickerState.selectedDateMillis?.let { millis ->
//                            val newEvent = java.util.Calendar.getInstance().apply {
//                                timeInMillis = millis
//                                set(java.util.Calendar.HOUR_OF_DAY, end.get(java.util.Calendar.HOUR_OF_DAY))
//                                set(java.util.Calendar.MINUTE, end.get(java.util.Calendar.MINUTE))
//                            }
//                            onEndDateSelected(newEvent)
//                            if (newEvent.timeInMillis < start.timeInMillis) {
//                                onStartDateSelected(newEvent.apply { timeInMillis -= HOUR_IN_MILLIS })
//                            }
//                        }
//                        showEndDatePicker = false
//                    }) {
//                        Text("OK")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showEndDatePicker = false }) {
//                        Text("Cancel")
//                    }
//                }
//            ) {
//                DatePicker(
//                    state = datePickerState,
//                    colors = DatePickerDefaults.colors(
//                        selectedDayContentColor = Color.LightGray,
//                        selectedYearContainerColor = Color(0xFF7C60A1),
//                        selectedDayContainerColor = Color(0xFF7C60A1),
//                    )
//                )
//            }
//        }
//
//        // Material 3 Time Picker for Start Time
//        if (showStartTimePicker) {
//            val timePickerState = rememberTimePickerState(
//                initialHour = start.get(java.util.Calendar.HOUR_OF_DAY),
//                initialMinute = start.get(java.util.Calendar.MINUTE),
//                is24Hour = false
//            )
//            AlertDialog(
//                onDismissRequest = { showStartTimePicker = false },
//                confirmButton = {
//                    TextButton(onClick = {
//                        val newEvent = java.util.Calendar.getInstance().apply {
//                            set(java.util.Calendar.HOUR_OF_DAY, timePickerState.hour)
//                            set(java.util.Calendar.MINUTE, timePickerState.minute)
//                            set(java.util.Calendar.YEAR, start.get(java.util.Calendar.YEAR))
//                            set(java.util.Calendar.MONTH, start.get(java.util.Calendar.MONTH))
//                            set(java.util.Calendar.DAY_OF_MONTH, start.get(java.util.Calendar.DAY_OF_MONTH))
//                        }
//                        onStartDateSelected(newEvent)
//                        if (newEvent.timeInMillis > end.timeInMillis) {
//                            onEndDateSelected(newEvent.apply { timeInMillis += HOUR_IN_MILLIS })
//                        }
//                        showStartTimePicker = false
//                    }) {
//                        Text("OK")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showStartTimePicker = false }) {
//                        Text("Cancel")
//                    }
//                },
//                text = {
//                    TimePicker(
//                        state = timePickerState,
//                        colors = TimePickerDefaults.colors(
//                            timeSelectorSelectedContainerColor = Color(0xFF7B4BCE), // Solid deep purple
//                            selectorColor = Color(0xFFB388FF),                    // Mid-tone purple
//                            timeSelectorSelectedContentColor = Color.White,       // White for contrast
//                            timeSelectorUnselectedContentColor = Color.White , // Neutral gray for unselected
//                            timeSelectorUnselectedContainerColor = Color.DarkGray
//                        )
//                    )
//                }
//            )
//        }
//
//        // Material 3 Time Picker for End Time
//        if (showEndTimePicker) {
//            val timePickerState = rememberTimePickerState(
//                initialHour = end.get(java.util.Calendar.HOUR_OF_DAY),
//                initialMinute = end.get(java.util.Calendar.MINUTE),
//                is24Hour = false
//            )
//            AlertDialog(
//                onDismissRequest = { showEndTimePicker = false },
//                confirmButton = {
//                    TextButton(onClick = {
//                        val newEvent = java.util.Calendar.getInstance().apply {
//                            set(java.util.Calendar.HOUR_OF_DAY, timePickerState.hour)
//                            set(java.util.Calendar.MINUTE, timePickerState.minute)
//                            set(java.util.Calendar.YEAR, end.get(java.util.Calendar.YEAR))
//                            set(java.util.Calendar.MONTH, end.get(java.util.Calendar.MONTH))
//                            set(java.util.Calendar.DAY_OF_MONTH, end.get(java.util.Calendar.DAY_OF_MONTH))
//                        }
//                        onEndDateSelected(newEvent)
//                        if (newEvent.timeInMillis < start.timeInMillis) {
//                            onStartDateSelected(newEvent.apply { timeInMillis -= HOUR_IN_MILLIS })
//                        }
//                        showEndTimePicker = false
//                    }) {
//                        Text("OK")
//                    }
//                },
//                dismissButton = {
//                    TextButton(onClick = { showEndTimePicker = false }) {
//                        Text("Cancel")
//                    }
//                },
//                text = {
//                    TimePicker(state = timePickerState)
//                }
//            )
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FrequencyDialog(
//    selectedFrequency: String,
//    onFrequencySelected: (String) -> Unit,
//    open: Boolean,
//    onClose: () -> Unit,
//) {
//    val frequencies = listOf(
//        CALENDAR_FREQ_NEVER,
//        CALENDAR_FREQ_DAILY,
//        CALENDAR_FREQ_WEEKLY,
//        CALENDAR_FREQ_MONTHLY,
//        CALENDAR_FREQ_YEARLY
//    )
//    if (open)
//        BasicAlertDialog(
//            onDismissRequest = { onClose() },
//            content = {
//                Column(
//                    modifier = Modifier.padding(12.dp),
//                    verticalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    frequencies.forEach { frequency ->
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Text(
//                                text = frequency.toUIFrequency(),
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                            RadioButton(selected = frequency == selectedFrequency,
//                                onClick = { onFrequencySelected(frequency) })
//                        }
//                    }
//                }
//            })
//}
//
//@Composable
//fun DeleteEventDialog(
//    openDialog: Boolean, onDelete: () -> Unit, onDismiss: () -> Unit
//) {
//    if (openDialog)
//        AlertDialog(shape = RoundedCornerShape(25.dp),
//            onDismissRequest = { onDismiss() },
//            title = { Text(stringResource(R.string.delete_event_confirmation_title)) },
//            text = {
//                Text(
//                    stringResource(
//                        R.string.delete_event_confirmation_message
//                    )
//                )
//            },
//            confirmButton = {
//                Button(
//                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
//                    shape = RoundedCornerShape(25.dp),
//                    onClick = {
//                        onDelete()
//                    },
//                ) {
//                    Text(stringResource(R.string.delete_event), color = Color.White)
//                }
//            },
//            dismissButton = {
//                Button(shape = RoundedCornerShape(25.dp), onClick = {
//                    onDismiss()
//                }) {
//                    Text(stringResource(R.string.cancel), color = Color.White)
//                }
//            })
//}