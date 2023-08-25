package com.implementing.feedfive.inappscreens.calendar.screen.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.implementing.feedfive.model.CalendarEvent
import com.implementing.feedfive.util.formatEventStartEnd

@Composable
fun CalendarEventSmallItem(
    event: CalendarEvent,
    onClick: (CalendarEvent) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(26.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(event.color)),
            )
            Spacer(Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .clickable { onClick(event) }
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    event.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    formatEventStartEnd(
                        start = event.start,
                        end = event.end,
                        location = event.location,
                        allDay = event.allDay,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )

            }
        }
    }
}
