package com.implementing.feedfive.inappscreens.task.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.task.TaskEvent
import com.implementing.feedfive.inappscreens.task.TaskItem
import com.implementing.feedfive.inappscreens.task.viewmodel.TaskViewModel
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.Order
import com.implementing.feedfive.util.OrderType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TasksScreen(
    navController: NavHostController,
    addTask: Boolean = false,
    viewModel: TaskViewModel = hiltViewModel()
) {
    var orderSettingsVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val uiState = viewModel.tasksUiState
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState()

//    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    BackHandler {
        if (sheetState.isVisible)
            scope.launch {
                sheetState.hide()
            }
        else
            navController.popBackStack()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) } ,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.tasks),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
//                elevation = 0.dp,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        sheetState
                        focusRequester.requestFocus()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_task),
                    tint = Color.White
                )
            }
        },
    ) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
//                scope.launch {
//                    sheetState.hide()
//                }
            },
            shape = RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp),
            content = {
                AddTaskBottomSheetContent(
                    onAddTask = {
                        viewModel.onEvent(TaskEvent.AddTask(it))
                        scope.launch { sheetState.hide() }
                        focusRequester.freeFocus()
                    },
                    focusRequester
                )
            })

            LaunchedEffect(uiState.error) {
                uiState.error?.let {
                    snackbarHostState.showSnackbar(
                        uiState.error
                    )
                    viewModel.onEvent(TaskEvent.ErrorDisplayed)
                }
            }
            LaunchedEffect(true){
                if (addTask) scope.launch {
                    sheetState.expand()
//                    sheetState.animateTo(ModalBottomSheetValue.Expanded)
                    focusRequester.requestFocus()
                }
            }
            if (uiState.tasks.isEmpty())
                NoTasksMessage()

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 4.dp)
            ) {
                item {
                    Column(
                        Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = { orderSettingsVisible = !orderSettingsVisible }) {
                                Icon(
                                    modifier = Modifier.size(25.dp),
                                    painter = painterResource(R.drawable.ic_settings_sliders),
                                    contentDescription = stringResource(R.string.order_by)
                                )
                            }
                            IconButton(onClick = {
                                navController.navigate(Screen.TaskSearchScreen.route)
                            }) {
                                Icon(
                                    modifier = Modifier.size(25.dp),
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = stringResource(R.string.search)
                                )
                            }
                        }
                        AnimatedVisibility(visible = orderSettingsVisible) {
                            TasksSettingsSection(
                                uiState.taskOrder,
                                uiState.showCompletedTasks,
                                onShowCompletedChange = {
                                    viewModel.onEvent(
                                        TaskEvent.ShowCompletedTasks(
                                            it
                                        )
                                    )
                                },
                                onOrderChange = {
                                    viewModel.onEvent(TaskEvent.UpdateOrder(it))
                                }
                            )
                        }
                    }
                }
                items(uiState.tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onComplete = {
                            viewModel.onEvent(
                                TaskEvent.CompleteTask(
                                    task,
                                    !task.isCompleted
                                )
                            )
                        },
                        onClick = {
                            navController.navigate(
                                Screen.TaskDetailScreen.route.replace(
                                    "{${Constants.TASK_ID_ARG}}",
                                    "${task.id}"
                                )
                            )
                        },
                    )
                }
            }
        }
    }

@Composable
fun NoTasksMessage(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.no_tasks_message),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            modifier = Modifier.size(125.dp),
            painter = painterResource(id = R.drawable.tasks_img),
            contentDescription = stringResource(R.string.no_tasks_message),
            alpha = 0.7f
        )
    }
}
@Composable
fun TasksSettingsSection(
    order: Order,
    showCompleted: Boolean,
    onOrderChange: (Order) -> Unit,
    onShowCompletedChange: (Boolean) -> Unit
) {
    val orders = listOf(
        Order.DateModified(),
        Order.DateCreated(),
        Order.Alphabetical(),
        Order.Priority()
    )
    val orderTypes = listOf(
        OrderType.ASC(),
        OrderType.DESC()
    )
    Column(
        Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = stringResource(R.string.order_by),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
        FlowRow(
            modifier = Modifier.padding(end = 8.dp)
        ) {
            orders.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = order.orderTitle == it.orderTitle,
                        onClick = {
                            if (order.orderTitle != it.orderTitle)
                                onOrderChange(
                                    it.copy(orderType = order.orderType)
                                )
                        }
                    )
                    Text(text = it.orderTitle, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Divider()
        FlowRow {
            orderTypes.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = order.orderType.orderTitle == it.orderTitle,
                        onClick = {
                            if (order.orderTitle != it.orderTitle)
                                onOrderChange(
                                    order.copy(it)
                                )
                        }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = it.orderTitle, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Divider()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = showCompleted, onCheckedChange = { onShowCompletedChange(it) })
            Text(
                text = stringResource(R.string.show_completed_tasks),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}