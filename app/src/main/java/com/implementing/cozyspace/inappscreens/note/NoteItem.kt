package com.implementing.cozyspace.inappscreens.note

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.note.screens.components.customListComponent
import com.implementing.cozyspace.model.Note
import com.implementing.cozyspace.util.formatDateDependingOnDay
import com.mikepenz.markdown.coil2.Coil2ImageTransformerImpl
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onClick: (Note) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
        onClick = { onClick(note) },
        border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
//        Box(modifier = modifier) {
//            Canvas(modifier = Modifier.matchParentSize()) {
//                //define a rectangle path, which would be cut at one corner
//                val clipPath = Path().apply {
//                    val cutCornerSize: Dp = 20.dp
//                    lineTo(size.width - cutCornerSize.toPx(), 0f)
//                    lineTo(size.width, cutCornerSize.toPx())
//                    lineTo(size.width, size.height)
//                    lineTo(0f, size.height)
//                    close()
//                }
//
//                clipPath(clipPath) {
//                    val cornerRadius: Dp = 10.dp
//                    drawRoundRect(
//                        color = Color(0xFF4F709C),
//                        size = size,
//                        cornerRadius = CornerRadius(cornerRadius.toPx())
//                    )
//
//                    val cutCornerSize: Dp = 10.dp
//                    drawRoundRect(
//                        color = Color(
//                            ColorUtils.blendARGB(0xffffff, 0x000000, 0.2f)
//                        ),
//                        topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
//                        size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
//                        cornerRadius = CornerRadius(cornerRadius.toPx())
//                    )
//                }
//            }
//        }


        Column(
            modifier = Modifier
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
                            .size(14.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                }
                Text(
                    note.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    fontSize = 13.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(8.dp))

            /*** First markdown dependency
            MarkdownText(
            markdown = note.content,
            maxLines = 14,
            onClick = { onClick(note) },
            imageLoader = myImageLoader(),
            fontSize = 12.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            )
             ***/


            Markdown(
                content = note.content,
                imageTransformer = Coil2ImageTransformerImpl,
                typography = markdownTypography(
                    text = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    h1 = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold,fontSize = 18.sp),
                    h2 = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                    h3 = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                    h4 = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                    h5 = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold,fontSize = 12.sp),
                    h6 = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp),
                    code = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp,fontFamily = FontFamily.Monospace,fontWeight = FontWeight.Bold,),
                    paragraph = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    quote = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, fontSize = 12.sp),
                    link = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    list = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),

                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 190.dp),
                colors = markdownColor(
                    linkText = Color(0xFF08A3C7),
                    inlineCodeBackground = Color(0xE6EC8C8C)
                ),
                components = markdownComponents(
                    unorderedList = customListComponent,
                )

            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = note.updatedDate.formatDateDependingOnDay(),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontSize = 7.sp
                ),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }

}

