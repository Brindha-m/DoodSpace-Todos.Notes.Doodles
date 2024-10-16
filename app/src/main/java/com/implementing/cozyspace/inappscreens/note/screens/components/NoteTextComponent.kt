package com.implementing.cozyspace.inappscreens.note.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.implementing.cozyspace.R
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun NoteTextFieldComponent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLine: Int = Int.MAX_VALUE,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
    ),
    enable: Boolean = true,
    placeholderText: String = "Title" // Customizable placeholder
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholderText, style = MaterialTheme.typography.bodyMedium) },
        modifier = modifier.fillMaxWidth(),
        colors = colors,
        keyboardOptions = keyboardOptions,
        maxLines = maxLine,
        enabled = enable
    )
}

@Composable
fun NoteContentFieldComponent(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLine: Int = Int.MAX_VALUE,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
    ),
    style: TextStyle,
    enable: Boolean = true,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .imePadding()  // Adjusts for keyboard visibility
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.note_content),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
//            label = {
//                Text(
//                    text = stringResource(R.string.note_content_label),
//                    fontSize = 15.sp,
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            },
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), // Makes the TextField scrollable
            colors = colors,
            keyboardOptions = keyboardOptions,
            maxLines = maxLine,
            enabled = enable,
        )
    }

}

@Composable
fun NoteMarkdownFieldComponent(
    content: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
    )
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .imePadding()  // Adjusts for keyboard visibility
            .verticalScroll(rememberScrollState())  // Enables scrolling
    ) {
        MarkdownText(
            markdown = content,
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            onClick = onClick,
        )
    }

}

// Enum to represent different Markdown actions
enum class MarkdownAction {
    Bold, Italic, Strikethrough, Underline, Heading, Checkbox, Quote, Code, Highlight
}


@Composable
fun MarkdownActionToolbar(
    onMarkdownAction: (MarkdownAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Bold Icon
        IconButton(onClick = { onMarkdownAction(MarkdownAction.Bold) }) {
            Icon(
                painter = painterResource(R.drawable.ic_bold),
                contentDescription = "Bold",
                modifier = Modifier.size(15.dp)
            )
        }

        // Italic Icon
        IconButton(onClick = { onMarkdownAction(MarkdownAction.Italic) }) {
            Icon(
                painter = painterResource(R.drawable.ic_italic),
                contentDescription = "Italic",
                modifier = Modifier.size(15.dp)

            )
        }

        // Strikethrough Icon
        IconButton(onClick = { onMarkdownAction(MarkdownAction.Strikethrough) }) {
            Icon(
                painter = painterResource(R.drawable.ic_strikethrough),
                contentDescription = "Strikethrough",
                modifier = Modifier.size(15.dp)

            )
        }

        // Checkbox Icon
        IconButton(onClick = { onMarkdownAction(MarkdownAction.Checkbox) }) {
            Icon(
                painter = painterResource(R.drawable.ic_checkbox),
                contentDescription = "Checkbox",
                modifier = Modifier.size(15.dp)

            )
        }

        // Quote Icon
        IconButton(onClick = { onMarkdownAction(MarkdownAction.Quote) }) {
            Icon(
                painter = painterResource(R.drawable.ic_quote),
                contentDescription = "Quote",
                modifier = Modifier.size(15.dp)

            )
        }

        // Code Block Icon
        IconButton(onClick = { onMarkdownAction(MarkdownAction.Code) }) {
            Icon(
                painter = painterResource(R.drawable.ic_codemark),
                contentDescription = "Code Block",
                modifier = Modifier.size(15.dp)

            )
        }

        // Highlight Icon
        IconButton(onClick = { onMarkdownAction(MarkdownAction.Highlight) }) {
            Icon(
                painter = painterResource(R.drawable.ic_highlighter),
                contentDescription = "Highlight",
                modifier = Modifier.size(15.dp)

            )
        }


        // Underline Icon
        IconButton(onClick = { onMarkdownAction(MarkdownAction.Underline) }) {
            Icon(
                painter = painterResource(R.drawable.ic_underline),
                contentDescription = "Underline",
                modifier = Modifier.size(15.dp)

            )
        }

        // Heading Icon
        IconButton(onClick = { onMarkdownAction(MarkdownAction.Heading) }) {
            Icon(
                painter = painterResource(R.drawable.ic_heading),
                contentDescription = "Heading",
                modifier = Modifier.size(15.dp)
            )
        }

    }
}

