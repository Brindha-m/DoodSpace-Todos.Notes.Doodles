package com.implementing.cozyspace.inappscreens.note.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.note.NoteEvent
import com.implementing.cozyspace.inappscreens.note.screens.components.MarkdownAction
import com.implementing.cozyspace.inappscreens.note.screens.components.MarkdownActionToolbar
import com.implementing.cozyspace.inappscreens.note.screens.components.NoteContentFieldComponent
import com.implementing.cozyspace.inappscreens.note.screens.components.NoteMarkdownFieldComponent
import com.implementing.cozyspace.inappscreens.note.screens.components.NoteTextFieldComponent
import com.implementing.cozyspace.inappscreens.note.viewmodel.NotesViewModel
import com.implementing.cozyspace.model.Note
import com.implementing.cozyspace.model.NoteFolder
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.ui.theme.Red
import com.implementing.cozyspace.util.formatDateDependingOnDay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsScreen(
    navController: NavHostController,
    noteId: Int,
    folderId: Int,
    viewModel: NotesViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        if (noteId != -1) viewModel.onEvent(NoteEvent.GetNote(noteId))
        if (folderId != -1) viewModel.onEvent(NoteEvent.GetFolder(folderId))
    }
    val state = viewModel.notesUiState
    val snackbarHostState = remember { SnackbarHostState() }
    var openDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var openFolderDialog by rememberSaveable { mutableStateOf(false) }

    var title by rememberSaveable { mutableStateOf(state.note?.title ?: "") }
    var content by rememberSaveable { mutableStateOf(state.note?.content ?: "") }

    var lastModified by remember { mutableStateOf("") }
    val wordCountString by remember {
        derivedStateOf { content.split(" ").size.toString() }
    }

//    var readingMode by remember { mutableStateOf(state.readingMode) }
    var pinned by rememberSaveable { mutableStateOf(state.note?.pinned ?: false) }
    val readingMode = state.readingMode
    var folder: NoteFolder? by remember { mutableStateOf(state.folder) }

    var usingTemplate by remember { mutableStateOf(false) }
    var selectedTemplate: String? by remember { mutableStateOf(null) }

    var openDialog by rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(state.note) {
        if (state.note != null) {
            title = state.note.title
            content = state.note.content
            pinned = state.note.pinned
            lastModified = state.note.updatedDate.formatDateDependingOnDay()
            folder = state.folder
        }

    }
    LaunchedEffect(state.navigateUp) {
        if (state.navigateUp) {
            openDeleteDialog = false
            navController.navigateUp()
        }
    }
//    LaunchedEffect(state) {
//        if (state.navigateUp) {
//            openDeleteDialog = false
//            navController.popBackStack(route = Screen.NotesScreen.route, inclusive = false)
//        }
//        if (state.error != null) {
//            snackbarHostState.showSnackbar(
//                state.error
//            )
//            viewModel.onEvent(NoteEvent.ErrorDisplayed)
//        }
//        if (state.folder != folder) folder = state.folder
//    }


    BackHandler {
        addOrUpdateNote(
            Note(
                title = title,
                content = content,
                pinned = pinned,
                folderId = folder?.id
            ),
            state.note,
            onNotChanged = {
                navController.popBackStack(
                    route = Screen.NotesScreen.route,
                    inclusive = false
                )
            },
            onUpdate = {
                if (state.note != null) {
                    viewModel.onEvent(
                        NoteEvent.UpdateNote(
                            state.note.copy(
                                title = title,
                                content = content,
                                folderId = folder?.id
                            )
                        )
                    )
                } else {
                    viewModel.onEvent(
                        NoteEvent.AddNote(
                            Note(
                                title = title,
                                content = content,
                                pinned = pinned,
                                folderId = folder?.id
                            )
                        )
                    )
                }

            }
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    if (folder != null) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(5.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(22.dp))
                                .clickable { openFolderDialog = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_folder),
                                stringResource(R.string.folders),
                                modifier = Modifier.padding(end = 8.dp, top = 8.dp, bottom = 8.dp).size(18.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(Modifier.width(5.dp))
                            Text(
                                text = folder?.name!!,
                                modifier = Modifier.padding(end = 8.dp, top = 8.dp, bottom = 8.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        IconButton(onClick = { openFolderDialog = true }) {
                            Icon(
                                painterResource(R.drawable.ic_create_folder),
                                stringResource(R.string.folders),
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onBackground

                            )
                        }
                    }
                },
                actions = {
                    if (state.note != null) IconButton(onClick = { openDeleteDialog = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.delete_icon),
                            contentDescription = stringResource(R.string.delete_task),
                            modifier = Modifier.size(21.dp)
                        )
                    }
                    IconButton(onClick = { usingTemplate = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.template_note),
                            contentDescription = "templates",
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    DropdownMenu(
                        expanded = usingTemplate,
                        onDismissRequest = { usingTemplate = false },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "😎 Dood's Note Template",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedTemplate = DOOD_MARKDOWN_TEMPLATE
                                content = DOOD_MARKDOWN_TEMPLATE
                                usingTemplate = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "🍨 Food Note Template",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedTemplate = FOOD_DIARY_TEMPLATE
                                content = FOOD_DIARY_TEMPLATE
                                usingTemplate = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "✈️ Travel Note Template",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedTemplate = TRAVEL_DIARY_TEMPLATE
                                content = TRAVEL_DIARY_TEMPLATE
                                usingTemplate = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "🔰 Learning Note Template",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedTemplate = LEARNING_DIARY_TEMPLATE
                                content = LEARNING_DIARY_TEMPLATE
                                usingTemplate = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "💗 Self Care Note Template",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedTemplate = SELF_CARE_TEMPLATE
                                content = SELF_CARE_TEMPLATE
                                usingTemplate = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "🙏🏻 Gratitude Note Template",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedTemplate = GRATITUDE_TEMPLATE
                                content = GRATITUDE_TEMPLATE
                                usingTemplate = false
                            }
                        )
                    }

                    IconButton(onClick = {
                        pinned = !pinned
                        if (state.note != null) {
                            viewModel.onEvent(NoteEvent.PinNote)
                        }
                    }) {
                        Icon(
                            painter = if (pinned) painterResource(id = R.drawable.ic_pin_filled)
                            else painterResource(id = R.drawable.pin_note),
                            contentDescription = stringResource(R.string.pin_note),
                            modifier = Modifier.size(20.dp),
                            tint = Red
                        )
                    }
                    IconButton(onClick = {
                        viewModel.onEvent(NoteEvent.ToggleReadingMode)
                    }) {
                        val saveMode = R.drawable.save_note
                        val editMode = R.drawable.edit_ic
                        if (readingMode)
                            Image(
                                painter = painterResource(id = saveMode),
                                contentDescription = stringResource(R.string.reading_mode),
                                modifier = Modifier.size(20.dp),
                            )
                        else
                            Image(
                                painter = painterResource(id = editMode),
                                contentDescription = stringResource(R.string.reading_mode),
                                modifier = Modifier.size(20.dp),
                            )

                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        addOrUpdateNote(
                            Note(
                                title = title,
                                content = content,
                                pinned = pinned,
                                folderId = folder?.id
                            ),
                            state.note,
                            onNotChanged = {
                                navController.popBackStack(
                                    route = Screen.NotesScreen.route,
                                    inclusive = false
                                )
                            },
                            onUpdate = {
                                if (state.note != null) {
                                    viewModel.onEvent(
                                        NoteEvent.UpdateNote(
                                            state.note.copy(
                                                title = title,
                                                content = content,
                                                folderId = folder?.id
                                            )
                                        )
                                    )
                                } else {
                                    viewModel.onEvent(
                                        NoteEvent.AddNote(
                                            Note(
                                                title = title,
                                                content = content,
                                                pinned = pinned,
                                                folderId = folder?.id
                                            )
                                        )
                                    )
                                }
                            }
                        )

                        navController.navigateUp()

                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.backarrow_ic),
                            contentDescription = "back",
                            modifier = Modifier.size(26.dp),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//                elevation = 0.dp,
            )
        },
    ) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {

            NoteTextFieldComponent(
                value = title,
                onValueChange = { title = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
            )


            MarkdownActionToolbar(
                onMarkdownAction = { action ->
                    when (action) {
                        MarkdownAction.Bold -> content += "**Bold**"
                        MarkdownAction.Italic -> content += "*Italic*"
                        MarkdownAction.Strikethrough -> content += "~~Strikethrough Text~~"
                        MarkdownAction.Underline -> content += "<u>Underlined Text</u>"
                        MarkdownAction.Heading -> content += "# Heading Text"
                        MarkdownAction.Checkbox -> content += "* [x] Do laundry"
                        MarkdownAction.Quote -> content += "> Quote"
                        MarkdownAction.Code -> content += "```\nCode Block\n```"
                        MarkdownAction.Highlight -> content += "`Highlighted Text`"

                    }
                }
            )
            Spacer(modifier = Modifier.height(6.dp))



            if (readingMode) {
                NoteContentFieldComponent(
                    value = content,
                    onValueChange = {
                        content = it
//                        print("From outline text ------ $content ")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )

            } else {
                NoteMarkdownFieldComponent(
                    content = content.ifBlank { stringResource(R.string.note_content) },
                    onClick = {
                        viewModel.onEvent(NoteEvent.ToggleReadingMode)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            Spacer(modifier = Modifier.height(25.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.End),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "$lastModified",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
                Text(
                    text = "Word Count: $wordCountString",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }

        }


        if (openDeleteDialog)
            AlertDialog(
                shape = RoundedCornerShape(25.dp),
                onDismissRequest = { openDeleteDialog = false },
                title = {
                    Text(
                        stringResource(R.string.delete_note_confirmation_title),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 17.sp
                    )
                },
                text = {
                    Text(
                        stringResource(
                            R.string.delete_note_confirmation_message,
                            state.note?.title!!
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            viewModel.onEvent(NoteEvent.DeleteNote(state.note!!))
                        },
                    ) {
                        Text(
                            stringResource(R.string.delete_note),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                dismissButton = {
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            openDeleteDialog = false
                        }) {
                        Text(
                            stringResource(R.string.cancel),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
        if (openFolderDialog)
            AlertDialog(
                shape = RoundedCornerShape(25.dp),
                onDismissRequest = { openFolderDialog = false },
                title = {
                    Text(
                        stringResource(R.string.change_folder),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                text = {
                    FlowRow {
                        Row(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                                .clickable {
                                    folder = null
                                    openFolderDialog = false
                                }
                                .background(if (folder == null) MaterialTheme.colorScheme.onBackground else Color.Transparent),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.none),
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (folder == null) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
                            )
                        }
                        state.folders.forEach {
                            Row(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(25.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                                    .clickable {
                                        folder = it
                                        openFolderDialog = false
                                    }
                                    .background(if (folder?.id == it.id) Color.LightGray else Color.Transparent),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_folder),
                                    stringResource(R.string.folders),
                                    modifier = Modifier.padding(
                                        start = 8.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                    tint = if (folder?.id == it.id) Color.Black else Color.LightGray
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = it.name,
                                    modifier = Modifier.padding(
                                        end = 8.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (folder?.id == it.id) Color.Red else Color(0xFFACA9A9)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            viewModel.onEvent(NoteEvent.DeleteNote(state.note!!))
                        },
                    ) {
                        Text(
                            stringResource(R.string.delete_note),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                dismissButton = {
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            openDialog = false
                        }) {
                        Text(
                            stringResource(R.string.cancel),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
    }
}

private fun addOrUpdateNote(
    newNote: Note,
    note: Note? = null,
    onNotChanged: () -> Unit = {},
    onUpdate: (Note) -> Unit,
) {
    if (note != null) {
        if (noteChanged(newNote, note))
            onUpdate(note)
        else
            onNotChanged()
    } else {
        onUpdate(newNote)
    }
}

private fun noteChanged(
    note: Note,
    newNote: Note
): Boolean {
    return note.title != newNote.title ||
            note.content != newNote.content ||
            note.folderId != newNote.folderId
}