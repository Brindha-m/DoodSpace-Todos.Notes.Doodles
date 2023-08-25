package com.implementing.feedfive.inappscreens.calendar.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.gson.Gson
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.calendar.CalendarEventsVM
import com.implementing.feedfive.inappscreens.calendar.screen.items.CalendarEventItem
import com.implementing.feedfive.inappscreens.calendar.viewmodel.CalendarViewModel
import com.implementing.feedfive.model.Calendar
import com.implementing.feedfive.model.CalendarEvent
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.monthName
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CalendarScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    var settingsVisible by remember { mutableStateOf(false) }
    val readCalendarPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_CALENDAR
    )
    val month by remember(state.events) {
        derivedStateOf {
            state.events.values.elementAt(lazyListState.firstVisibleItemIndex)
                .first().start.monthName()
        }
    }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.calendar),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    if (state.events.isNotEmpty()) MonthDropDownMenu(
                        selectedMonth = month,
                        months = state.months,
                        onMonthSelected = { selected ->
                            scope.launch {
                                lazyListState.scrollToItem(
                                    state.events.values.indexOfFirst {
                                        it.first().start.monthName() == selected
                                    }
                                )
                            }
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//                elevation = 0.dp,
            )
        },
        floatingActionButton = {
            if (readCalendarPermissionState.hasPermission)
                FloatingActionButton(
                    onClick = {
                        navController.navigate(
                            Screen.CalendarEventDetailsScreen.route.replace(
                                "{${Constants.CALENDAR_EVENT_ARG}}",
                                " "
                            )
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.add_event),
                        tint = Color.White
                    )
                }
        },
    ) { paddingValues ->

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (readCalendarPermissionState.hasPermission) {
                LaunchedEffect(true) {
                    viewModel.onEvent(
                        CalendarEventsVM
                            .ReadPermissionChanged(readCalendarPermissionState.hasPermission)
                    )
                }
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { settingsVisible = !settingsVisible }) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(R.drawable.ic_settings_sliders),
                            contentDescription = stringResource(R.string.include_calendars)
                        )
                    }
                }
                AnimatedVisibility(visible = settingsVisible) {
                    CalendarSettingsSection(
                        calendars = state.calendars, onCalendarClicked = {
                            viewModel.onEvent(CalendarEventsVM.IncludeCalendar(it))
                        }
                    )
                }
                LazyColumn(
                    state = lazyListState,
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    state.events.forEach { (day, events) ->
                        item {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Text(
                                    text = day.substring(0, day.indexOf(",")),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                events.forEach { event ->
                                    CalendarEventItem(event = event, onClick = {
                                        val eventJson =
                                            Gson().toJson(event, CalendarEvent::class.java)
                                        // encoding the string to avoid crashes when the event contains fields that equals a URL
                                        val encodedJson = URLEncoder.encode(
                                            eventJson,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                        navController.navigate(
                                            Screen.CalendarEventDetailsScreen.route.replace(
                                                "{${Constants.CALENDAR_EVENT_ARG}}",
                                                encodedJson
                                            )
                                        )
                                    })
                                }
                            }
                        }
                    }
                }
            } else {
                NoReadCalendarPermissionMessage(
                    shouldShowRationale = readCalendarPermissionState.shouldShowRationale,
                    context
                ) {
                    readCalendarPermissionState.launchPermissionRequest()
                }
            }
        }
    }

}

@Composable
fun NoReadCalendarPermissionMessage(
    shouldShowRationale: Boolean,
    context: Context,
    onRequest: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_read_calendar_permission_message),
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
            TextButton(onClick = { onRequest() }) {
                Text(text = stringResource(R.string.grant_permission))
            }
        }
    }
}

@Composable
fun MonthDropDownMenu(
    selectedMonth: String,
    months: List<String>,
    onMonthSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        Modifier
            .clickable { expanded = true }
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedContent(targetState = selectedMonth, label = "") { month ->
                Text(
                    text = month,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            months.forEach {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        onMonthSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CalendarSettingsSection(
    calendars: Map<String, List<Calendar>>,
    onCalendarClicked: (Calendar) -> Unit
) {
    Column {
        Divider()
        Text(
            text = stringResource(R.string.include_calendars),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(8.dp)
        )
        Divider()
        calendars.keys.forEach { calendar ->
            var expanded by remember { mutableStateOf(false) }
            Box(
                Modifier
                    .clickable { expanded = true }
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = calendar,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    calendars[calendar]?.forEach { subCalendar ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = subCalendar.included,
                                        onCheckedChange = { onCalendarClicked(subCalendar) },
                                        colors = CheckboxDefaults.colors(
                                            uncheckedColor = Color(subCalendar.color),
                                            checkedColor = Color(subCalendar.color)
                                        )
                                    )
                                    Text(
                                        text = subCalendar.name,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                            onClick = {
                                onCalendarClicked(subCalendar)
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}