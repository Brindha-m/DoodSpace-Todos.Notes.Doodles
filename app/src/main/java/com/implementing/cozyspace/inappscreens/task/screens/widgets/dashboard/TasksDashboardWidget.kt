package com.implementing.cozyspace.inappscreens.task.screens.widgets.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.model.Task

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksDashboardWidget(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit = {},
    onCheck: (Task) -> Unit = {},
    onAddClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val isDark = true

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
//        val isDark = !MaterialTheme.colorScheme.isLight
        Column(
            modifier = modifier
                .clickable { onClick() }
                .padding(8.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.tasks), style = MaterialTheme.typography.bodyMedium)
                Icon(
                    painterResource(R.drawable.ic_add),
                    stringResource(R.string.add_event),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            onAddClick()
                        }
                )
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isDark) Color.DarkGray else Color.LightGray),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (tasks.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.no_tasks_message_widget),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth().padding(5.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else items(tasks) {
                    TaskDashboardItem(
                        task = it,
                        onClick = { onTaskClick(it) },
                        onComplete = { onCheck(it.copy(isCompleted = !it.isCompleted)) },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
    }
}