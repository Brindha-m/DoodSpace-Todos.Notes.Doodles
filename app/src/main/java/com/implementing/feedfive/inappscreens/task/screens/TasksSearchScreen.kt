package com.implementing.feedfive.inappscreens.task.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.task.TaskEvent
import com.implementing.feedfive.inappscreens.task.TaskItem
import com.implementing.feedfive.inappscreens.task.viewmodel.TaskViewModel
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.util.Constants

@Composable
fun TasksSearchScreen(
    navController: NavHostController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val state = viewModel.tasksUiState
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        var query by rememberSaveable {
            mutableStateOf("")
        }
        LaunchedEffect(query){viewModel.onEvent(TaskEvent.SearchTasks(query))}
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(true){focusRequester.requestFocus()}
        OutlinedTextField(
            value = query,
            onValueChange = {query = it},
            label = {Text(stringResource(R.string.search_tasks))},
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
        ) {
            items(state.searchTasks, key = {it.id}){ task ->
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