package com.implementing.cozyspace.inappscreens.diary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.model.Diary
import com.implementing.cozyspace.ui.theme.bgCard
import com.implementing.cozyspace.util.fullDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.DiaryEntryItem(
    modifier: Modifier = Modifier,
    entry: Diary,
    onClick: (Diary) -> Unit
) {
    Card(
        modifier = modifier
            .animateItemPlacement().fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF141414))
    ) {
        Box(modifier = modifier.fillMaxWidth())
        {
//            Image(painter = painterResource(id = R.drawable.img_12), contentDescription = "")
            Column(
                modifier = Modifier
                    .clickable { onClick(entry) }
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Image(
                        painterResource(entry.mood.icon),
                        contentDescription = "",
                        modifier = Modifier.size(28.dp).alignByBaseline(), // Align the image to the baseline
                    )
                    Spacer(modifier = Modifier.width(3.dp))

                    Text(
                        text = stringResource(entry.mood.title),
                        color = entry.mood.color, // Set the desired text color
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        // Add appropriate spacing
                    )

//                Image(
//                    painter = painterResource(entry.mood.icon),
//                    stringResource(entry.mood.title),
//                    tint = entry.mood.color,
//                    modifier = Modifier.size(30.dp)
//                )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        entry.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (entry.content.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        entry.content,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = entry.createdDate.fullDate(),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End),
                )
            }
        }
    }
}

