package com.implementing.cozyspace.mainscreens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.calendar.screen.items.CalendarDashboardWidget
import com.implementing.cozyspace.inappscreens.diary.chart.MoodCircularBar
import com.implementing.cozyspace.inappscreens.diary.chart.MoodFlowChart
import com.implementing.cozyspace.inappscreens.task.screens.widgets.dashboard.TasksDashboardWidget
import com.implementing.cozyspace.inappscreens.task.screens.widgets.dashboard.TasksSummaryCard
import com.implementing.cozyspace.mainscreens.viewmodel.DashboardEvent
import com.implementing.cozyspace.mainscreens.viewmodel.MainViewModel
import com.implementing.cozyspace.model.CalendarEvent
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.ThemeSettings
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val themeMode = viewModel.themeMode.collectAsState(initial = ThemeSettings.DARK.value)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        )
                    {
                        Text(
                            text = "Dashboard",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        )

                        Image(
                            painter = when (themeMode.value) {
                                ThemeSettings.DARK.value -> painterResource(id = R.drawable.dood_ic_dark)
                                ThemeSettings.LIGHT.value -> painterResource(id = R.drawable.dood_ic_light)
                                else -> {painterResource(id = R.drawable.dood_ic_light) }
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .size(89.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ){
        LaunchedEffect(true) { viewModel.onDashboardEvent(DashboardEvent.InitAll) }
        LazyColumn {
            item {
                Spacer(Modifier.height(60.dp))
                CalendarDashboardWidget(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f),
                    events = viewModel.uiState.dashBoardEvents,
                    onClick = {
                        navController.navigate(
                            Screen.CalendarScreen.route
                        )
                    },
                    onPermission = {
                        viewModel.onDashboardEvent(DashboardEvent.ReadPermissionChanged(it))
                    },
                    onAddEventClicked = {
                        navController.navigate(
                            Screen.CalendarEventDetailsScreen.route.replace(
                                "{${Constants.CALENDAR_EVENT_ARG}}",
                                " "
                            )
                        )
                    },
                    onEventClicked = {
                        val eventJson = Gson().toJson(it, CalendarEvent::class.java)
                        // encoding the string to avoid crashes when the event contains fields that equals a URL
                        val encodedJson = URLEncoder.encode(eventJson, StandardCharsets.UTF_8.toString())
                        navController.navigate(
                            Screen.CalendarEventDetailsScreen.route.replace(
                                "{${Constants.CALENDAR_EVENT_ARG}}",
                                encodedJson
                            )
                        )
                    }
                )
            }

            item {
                MoodFlowChart(
                    entries = viewModel.uiState.dashBoardEntries,
                    monthly = false
                )

            }

            item {
                TasksDashboardWidget(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f),
                    tasks = viewModel.uiState.dashBoardTasks,
                    onCheck = {
                        viewModel.onDashboardEvent(DashboardEvent.UpdateTask(it))
                    },
                    onTaskClick = {
                        navController.navigate(
                            Screen.TaskDetailScreen.route
                                .replace(
                                    "{${Constants.TASK_ID_ARG}}",
                                    it.id.toString()
                                )
                        )
                    },
                    onAddClick = {
                        navController.navigate(
                            Screen.TasksScreen
                                .route
                                .replace(
                                    "{${Constants.ADD_TASK_ARG}}",
                                    "true"
                                )
                        )
                    },
                    onClick = {
                        navController.navigate(
                            Screen.TasksScreen.route
                        )
                    }
                )
            }
            item {
                Row {
                    MoodCircularBar(
                        entries = viewModel.uiState.dashBoardEntries,
                        showPercentage = false,
                        modifier = Modifier.weight(1f, fill = true),
                        onClick = {
                            navController.navigate(
                                Screen.DiaryChartScreen.route
                            )
                        }
                    )
                    TasksSummaryCard(
                        modifier = Modifier.weight(1f, fill = true),
                        tasks = viewModel.uiState.summaryTasks
                    )
                }
            }
            item { Spacer(Modifier.height(65.dp)) }
        }
    }
}