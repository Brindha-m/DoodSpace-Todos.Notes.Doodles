package com.implementing.feedfive.inappscreens.calendar.screen.items


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.google.gson.Gson
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.glance_widgets.calendar.CalendarWidgetItemClick
import com.implementing.feedfive.inappscreens.glance_widgets.calendar.eventJson
import com.implementing.feedfive.model.CalendarEvent
import com.implementing.feedfive.util.formatEventStartEnd

@Composable
fun CalendarEventWidgetItem(
    event: CalendarEvent,
) {
    Box(
        GlanceModifier
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = GlanceModifier
                .cornerRadius(16.dp)
                .background(ImageProvider(R.drawable.small_item_rounded_corner_shape))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = GlanceModifier.padding(start = 8.dp)
            ) {
                Box(
                    modifier = GlanceModifier
                        .width(6.dp)
                        .height(26.dp)
                        .cornerRadius(6.dp)
                        .background(Color(event.color)),
                ) {}
                Spacer(GlanceModifier.width(4.dp))
                Column(
                    modifier = GlanceModifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        event.title,
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        maxLines = 2
                    )
                    Spacer(GlanceModifier.height(6.dp))
                    Text(
                        formatEventStartEnd(
                            start = event.start,
                            end = event.end,
                            location = event.location,
                            allDay = event.allDay,
                        ),
                        style = TextStyle(color = ColorProvider(Color.LightGray))
                    )
                }
            }
            Box(GlanceModifier.fillMaxSize().clickable(
                actionRunCallback<CalendarWidgetItemClick>(
                    parameters = actionParametersOf(
                        eventJson to Gson().toJson(event, CalendarEvent::class.java)
                    )
                )
            )) {}
        }
    }
}