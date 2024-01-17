package com.implementing.cozyspace.inappscreens.note.screens.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.note.NoteEvent
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
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = stringResource(R.string.title)) },
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
    enable: Boolean = true,
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(
            text = stringResource(R.string.note_content),
        ) },
        label = {
            Text(
                text = stringResource(R.string.note_content_label),
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        modifier = modifier.fillMaxWidth(),
        colors = colors,
        keyboardOptions = keyboardOptions,
        maxLines = maxLine,
        enabled = enable
    )

}


@Composable
fun NoteMarkdownFieldComponent(
    content: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
    )
) {
    MarkdownText(
        markdown = content,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(vertical = 5.dp)
//            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            .padding(10.dp),
        color = MaterialTheme.colorScheme.primary,
        fontSize = 13.sp,
        onClick = onClick,
    )
}
