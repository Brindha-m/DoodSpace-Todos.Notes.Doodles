package com.implementing.cozyspace.inappscreens.note.screens.components

import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.note.screens.components.markdown.MarkdownElement
import com.implementing.cozyspace.inappscreens.task.SubTaskItem
import com.implementing.cozyspace.model.SubTask
import com.mikepenz.markdown.coil2.Coil2ImageTransformerImpl
import com.mikepenz.markdown.compose.components.MarkdownComponent
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownListItems
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import org.intellij.markdown.ast.getTextInNode

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
        placeholder = {
            Text(
                text = placeholderText, style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp, // Set the font size
                )
            )
        },
        modifier = modifier.fillMaxWidth(),
        colors = colors,
        keyboardOptions = keyboardOptions,
        maxLines = maxLine,
        enabled = enable,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 13.sp, // Set the font size
        ),
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
    cursorPosition: Int,
    onCursorPositionChange: (Int) -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .imePadding()  // Adjusts for keyboard visibility
    ) {

        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                onCursorPositionChange(it.length)
            },
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
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp, // Set the font size
                fontWeight = FontWeight.Normal,
            ),
        )
    }

}


val customListComponent: MarkdownComponent = { markdownComponentModel ->
    // Iterate through each list item
    MarkdownListItems(
        markdownComponentModel.content,
        markdownComponentModel.node,
        level = 0
    ) { _, child ->
        val itemText = child.getTextInNode(markdownComponentModel.content).toString().trim()

        // Check if the item is a checkbox (starts with - [ ] or - [x])
        val isChecked = itemText.startsWith("- [x]") || itemText.startsWith("- [X]")
        val isUnchecked = itemText.startsWith("- [ ]")

//        val check = remember { mutableStateOf(true) }
        val check = remember { mutableStateOf(isChecked) }
        val displayText = itemText
            .replace(Regex("""^- \[x\]""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("""^- \[\s\]"""), "")
            .trim()
        if (isChecked || isUnchecked) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Render a clickable checkbox with toggle functionality
                Checkbox(
                    checked = check.value,
                    onCheckedChange = { newState ->
                        check.value = newState
                    },
                    modifier = Modifier.padding(7.dp).size(10.dp),
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.scrim),
                )



                Text(text = displayText, fontSize = 13.sp,)
            }
        } else if (itemText.startsWith("-")) { // Regular unordered list
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "•", // Bullet character
                    style = TextStyle(fontSize = 20.sp), // Adjust bullet size as needed
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = itemText.replace("-", "").trim(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                ) // Render the bullet text
            }
        }
    }
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .imePadding()  // Adjusts for keyboard visibility
            .verticalScroll(rememberScrollState())  // Enables scrolling
    ) {

        Markdown(
            content = content,
            imageTransformer = Coil2ImageTransformerImpl,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .padding(5.dp),
            colors = markdownColor(
                linkText = Color(0xFF08A3C7),
                inlineCodeBackground = Color(0xE6EC8C8C)
            ),
            typography = markdownTypography(
                text = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                ),
                h1 = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                h2 = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                ),
                h3 = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                ),
                h4 = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                h5 = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                h6 = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                code = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp,fontFamily = FontFamily.Monospace,fontWeight = FontWeight.Bold,),
                paragraph = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                quote = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                link = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                list = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                ),
            ),
            components = markdownComponents(
                unorderedList = customListComponent,

            )
        )
//            color = MaterialTheme.colorScheme.primary,
//            fontSize = 12.sp,
//            onClick = onClick,
//        )
    }

}


@Composable
fun MarkdownActionToolbar(
//    onMarkdownAction: (MarkdownAction) -> Unit
    value: String,
    onValueChange: (String) -> Unit,
    cursorPosition: Int,
    onCursorPositionChange: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceContainerHighest)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Bold Icon
        IconButton(onClick = {
            val insertedText = " **Bold** "
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        })
        {
            Icon(
                painter = painterResource(R.drawable.ic_bold),
                contentDescription = "Bold",
                modifier = Modifier.size(15.dp)
            )
        }

        // Italic Icon
        IconButton(onClick = {
            val insertedText = " *Italic* "
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_italic),
                contentDescription = "Italic",
                modifier = Modifier.size(15.dp)

            )
        }

        // Image Icon
        IconButton(onClick = {
            val insertedText = "\n ![](https://i.ibb.co/8Y6qJpj/doodplaybg.png) \n"
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_gallery),
                contentDescription = "Image",
                modifier = Modifier.size(18.dp)

            )
        }

        // Highlight Icon
        IconButton(onClick = {
            val insertedText = " `Highlighted Text` "
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_highlighter),
                contentDescription = "Highlight",
                modifier = Modifier.size(15.dp)

            )
        }


        // Checkbox Icon
        IconButton(onClick = {
            val insertedText = "\n - [ ] Unchecked \n - [x] Checked \n"
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        }) {
            Icon(
                painter = painterResource(R.drawable.checkbox_24),
                contentDescription = "Underline",
                modifier = Modifier.size(15.dp)

            )
        }

        // Bullet List Icon
        IconButton(onClick = {
            val insertedText = "\n - List \n"
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_checkbox),
                contentDescription = "Checkbox",
                modifier = Modifier.size(15.dp)

            )
        }

        // Quote Icon
        IconButton(onClick = {
            val insertedText = "\n > Quote \n"
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_quote),
                contentDescription = "Quote",
                modifier = Modifier.size(15.dp)

            )
        }


        // Code Block Icon
        IconButton(onClick = {
            val insertedText = "\n```\n Code Block \n\n```\n"
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_codemark),
                contentDescription = "Code Block",
                modifier = Modifier.size(15.dp)

            )
        }

        // Strikethrough Icon
        IconButton(onClick = {
            val insertedText = " ~~Strikethrough~~ "
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_strikethrough),
                contentDescription = "Strikethrough",
                modifier = Modifier.size(15.dp)

            )
        }


        // Heading Icon
        IconButton(onClick = {
            val insertedText = "\n # Heading Text \n"
            val newText = value.insertAt(cursorPosition, insertedText)
            onValueChange(newText)
            onCursorPositionChange(cursorPosition + insertedText.length)
        }) {
            Icon(
                painter = painterResource(R.drawable.ic_heading),
                contentDescription = "Heading",
                modifier = Modifier.size(15.dp)
            )
        }

    }
}

// Helper function to insert text at a specific index


//fun String.insertAt(cursorPosition: Int, insertedText: String): String {
//    // Make sure the cursor position is valid (in bounds of the string)
//    val safeCursorPosition = cursorPosition.coerceIn(0, this.length)
//    return this.substring(0, safeCursorPosition) + insertedText + this.substring(safeCursorPosition)
//}

fun String.insertAt(index: Int, text: String): String {
    val validIndex = index.coerceIn(0, this.length) // Ensure the index is within bounds
    return substring(0, validIndex) + text + substring(validIndex)
}
