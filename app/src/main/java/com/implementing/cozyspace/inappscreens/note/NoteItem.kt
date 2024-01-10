package com.implementing.cozyspace.inappscreens.note

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.implementing.cozyspace.R
import com.implementing.cozyspace.model.Note
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onClick: (Note) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = modifier) {
            Canvas(modifier = Modifier.matchParentSize()) {
                //define a rectangle path, which would be cut at one corner
                val clipPath = Path().apply {
                    val cutCornerSize: Dp = 20.dp
                    lineTo(size.width - cutCornerSize.toPx(), 0f)
                    lineTo(size.width, cutCornerSize.toPx())
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }

                clipPath(clipPath) {
                    val cornerRadius: Dp = 10.dp
                    drawRoundRect(
                        color = Color(0xFF4F709C),
                        size = size,
                        cornerRadius = CornerRadius(cornerRadius.toPx())
                    )

                    val cutCornerSize: Dp = 10.dp
                    drawRoundRect(
                        color = Color(
                            ColorUtils.blendARGB(0xffffff, 0x000000, 0.2f)
                        ),
                        topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                        size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                        cornerRadius = CornerRadius(cornerRadius.toPx())
                    )
                }
            }
            Column(
                modifier = Modifier
                    .clickable { onClick(note) }
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (note.pinned) {
                        Icon(
                            painter = painterResource(R.drawable.pin_note_fill),
                            contentDescription = stringResource(R.string.pin_note),
                            tint = Color.Red,
                            modifier = Modifier
                                .size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                    Text(
                        note.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(8.dp))
                MarkdownText(
                    markdown = note.content,
                    maxLines = 14,
                    onClick = { onClick(note) },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary,
                )

            }
        }
    }
}

