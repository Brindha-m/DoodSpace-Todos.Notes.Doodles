package com.implementing.cozyspace.inappscreens.note.screens.components.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun SimpleMarkdownText(
    text: String,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        text.lines().forEach { line ->
            when {
                line.startsWith("# ") -> {
                    // Heading
                    MarkdownHeading(line, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                line.startsWith("> ") -> {
                    // Quote
                    MarkdownQuote(line.removePrefix("> "), fontSize = fontSize)
                }
                line.startsWith("- [ ]") || line.startsWith("- [x]") -> {
                    // Checkbox item
                    val isChecked = line.startsWith("- [x]")
                    MarkdownCheckboxItem(line.removePrefix("- [ ]").removePrefix("- [x]"), isChecked)
                }
                line.startsWith("```") -> {
                    // Code block
                    MarkdownCodeBlock(line)
                }
//                line.startsWith("!") -> {
//                    // Image
//                    MarkdownImage(line)
//                }
                else -> {
                    // Normal text
                    Text(
                        text = line,
                        fontSize = fontSize,
                        fontWeight = fontWeight
                    )
                }
            }
        }
    }
}

@Composable
fun MarkdownHeading(text: String, fontSize: TextUnit, fontWeight: FontWeight) {
    Text(
        text = text.removePrefix("# "),
        fontSize = fontSize,
        fontWeight = fontWeight,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

//@Composable
//fun MarkdownQuote(text: String, fontSize: TextUnit) {
//    Row(modifier = Modifier.padding(start = 8.dp)) {
//        Box(
//            modifier = Modifier
//                .width(4.dp)
//                .height(20.dp)
//                .background(Color.Gray)
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = text,
//            fontSize = fontSize,
//            fontStyle = FontStyle.Italic
//        )
//    }
//}

@Composable
fun MarkdownCheckboxItem(text: String, isChecked: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = null // Make it non-interactive for preview
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
fun MarkdownCodeBlock(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Text(
            text = text.removeSurrounding("```"),
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}

//@Composable
//fun MarkdownImage(text: String) {
//    // Example using Coil for loading an image URL in markdown
//    val imageUrl = text.substringAfter("![](").substringBefore(")")
//    AsyncImage(
//        model = imageUrl,
//        contentDescription = null,
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clip(RoundedCornerShape(4.dp))
//    )
//}
