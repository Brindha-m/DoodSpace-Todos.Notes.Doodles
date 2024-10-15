package com.implementing.cozyspace.inappscreens.calendar.screen.items

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.implementing.cozyspace.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.implementing.cozyspace.inappscreens.calendar.screen.NoReadCalendarPermissionMessage
import com.implementing.cozyspace.model.CalendarEvent


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CalendarDashboardWidget(
    modifier: Modifier = Modifier,
    events: Map<String, List<CalendarEvent>>,
    onPermission: (Boolean) -> Unit = {},
    onClick: () -> Unit = {},
    onAddEventClicked: () -> Unit = {},
    onEventClicked: (CalendarEvent) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val context = LocalContext.current
        val readCalendarPermissionState = rememberPermissionState(
            Manifest.permission.READ_CALENDAR
        )


//        val isDark = !MaterialTheme.colorScheme.isLight

        val isDark = true

        Column(
            modifier = modifier
                .clickable { onClick() }
                .padding(8.dp)
        ) {
            Row(
                Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.calendar), style = MaterialTheme.typography.bodyMedium)
                Icon(
                    painterResource(R.drawable.ic_add),
                    stringResource(R.string.add_event),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            onAddEventClicked()
                        }
                )
            }
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFF221F3E), // Light grey at the start
                                0.5f to Color(0xFF775A6D), // Darker grey in the middle
                                1f to Color(0xFF93929D) // Dark grey at the end
                            )
                        )),
//                    .background(if (isDark) Color.DarkGray else Color.LightGray),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (readCalendarPermissionState.status.isGranted) {
                    if (events.isEmpty()) {
                        item {
                            LaunchedEffect(true) { onPermission(true) }
                            Text(
                                text = stringResource(R.string.no_events),
                                modifier = Modifier.padding(16.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        events.forEach { (day, events) ->
                            item {
                                LaunchedEffect(true) { onPermission(true) }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Text(
                                        text = day,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    events.forEach { event ->
                                        CalendarEventSmallItem(event = event, onClick = {
                                            onEventClicked(event)
                                        })
                                    }
                                }
                            }
                        }
                    }
                } else {
                    item {
                        LaunchedEffect(true) { onPermission(false) }
                        NoReadCalendarPermissionMessageDashboard(
                            shouldShowRationale = readCalendarPermissionState.status.shouldShowRationale,
                            context
                        ) {
                            readCalendarPermissionState.launchPermissionRequest()
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun NoReadCalendarPermissionMessageDashboard(
    shouldShowRationale: Boolean,
    context: Context,
    onRequest: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
    ) {
        Text(
            text = stringResource(R.string.no_read_calendar_permission_message),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.White
//            color = MaterialTheme.colorScheme.inverseOnSurface
        )
        Spacer(Modifier.height(12.dp))
        if (shouldShowRationale) {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF67509F), contentColor = Color.White),
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", context.packageName, null)
                    context.startActivity(intent)
                }) {
                Text(text = stringResource(R.string.go_to_settings), style = MaterialTheme.typography.bodyMedium)
            }

        } else {
            TextButton(
                onClick = { onRequest() },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color(0xFF3C385C),
                    contentColor = Color.White
                )
            ) {

                Text(
                    text = stringResource(R.string.grant_permission),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}