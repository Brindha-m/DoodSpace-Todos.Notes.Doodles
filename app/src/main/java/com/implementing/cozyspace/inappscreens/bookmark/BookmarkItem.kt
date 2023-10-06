package com.implementing.cozyspace.inappscreens.bookmark

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.model.Bookmark
import com.implementing.cozyspace.util.isValidUrl

@Composable
fun BookmarkItem(
    modifier: Modifier = Modifier,
    bookmark: Bookmark,
    onClick: (Bookmark) -> Unit,
    onInvalidUrl: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)

    ) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .clickable { onClick(bookmark) }
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            if (bookmark.title.isNotBlank()){
                Text(
                    bookmark.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
            }
            Row {

            }
            Text(
                bookmark.url,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
            IconButton(
                onClick = {
                    if (bookmark.url.isValidUrl()){
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(if (!bookmark.url.startsWith("http://") && !bookmark.url.startsWith("https://")) "http://${bookmark.url}" else bookmark.url)
                        context.startActivity(intent)
                    } else
                        onInvalidUrl()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    painterResource(id = R.drawable.open_link_bookmark),
                    stringResource(id = R.string.open_link),
                    modifier = Modifier.size(25.dp)
                )
            }
        }
    }
}