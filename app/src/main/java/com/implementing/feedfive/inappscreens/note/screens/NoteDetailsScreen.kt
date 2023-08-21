package com.implementing.feedfive.inappscreens.note.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.note.NoteEvent
import com.implementing.feedfive.inappscreens.note.viewmodel.NotesViewModel
import com.implementing.feedfive.model.Note
import com.implementing.feedfive.model.NoteFolder
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.ui.theme.Orange
import dev.jeziellago.compose.markdowntext.MarkdownText

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
    var pinned by rememberSaveable { mutableStateOf(state.note?.pinned ?: false) }
    val readingMode = state.readingMode
    var folder: NoteFolder? by remember { mutableStateOf(state.folder) }

    var openDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state.note) {
        if (state.note != null) {
            title = state.note.title
            content = state.note.content
            pinned = state.note.pinned
            folder = state.folder
        }
    }
    LaunchedEffect(state) {
        if (state.navigateUp) {
            openDeleteDialog = false
            navController.popBackStack(route = Screen.NotesScreen.route, inclusive = false)
        }
        if (state.error != null) {
            snackbarHostState.showSnackbar(
                state.error
            )
            viewModel.onEvent(NoteEvent.ErrorDisplayed)
        }
        if (state.folder != folder) folder = state.folder
    }
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
                                .clip(RoundedCornerShape(25.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(25.dp))
                                .clickable { openFolderDialog = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_folder),
                                stringResource(R.string.folders),
                                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                            )
                            Spacer(Modifier.width(8.dp))
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
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                },
                actions = {
                    if (state.note != null) IconButton(onClick = { openDeleteDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.delete_task)
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
                            else painterResource(id = R.drawable.ic_pin),
                            contentDescription = stringResource(R.string.pin_note),
                            modifier = Modifier.size(24.dp),
                            tint = Orange
                        )
                    }
                    IconButton(onClick = {
                        viewModel.onEvent(NoteEvent.ToggleReadingMode)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_read_mode),
                            contentDescription = stringResource(R.string.reading_mode),
                            modifier = Modifier.size(24.dp),
                            tint = if (readingMode) Color.Green else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
//                elevation = 0.dp,
            )
        },
    ) { paddingValues ->

        Column(
            Modifier
                .fillMaxSize().padding(paddingValues)
                .padding(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = stringResource(R.string.title)) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
            )
            if (readingMode)
                MarkdownText(
                    markdown = content.ifBlank { stringResource(R.string.note_content) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(vertical = 6.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(20.dp))
                        .padding(10.dp),
                    onClick = {
                        viewModel.onEvent(NoteEvent.ToggleReadingMode)
                    }
                )
            else
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = {
                        Text(text = stringResource(R.string.note_content))
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                )
        }
        if (openDeleteDialog)
            AlertDialog(
                shape = RoundedCornerShape(25.dp),
                onDismissRequest = { openDeleteDialog = false },
                title = { Text(stringResource(R.string.delete_note_confirmation_title)) },
                text = {
                    Text(
                        stringResource(
                            R.string.delete_note_confirmation_message,
                            state.note?.title!!
                        )
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
                        Text(stringResource(R.string.delete_note), color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            openDeleteDialog = false
                        }) {
                        Text(stringResource(R.string.cancel), color = Color.White)
                    }
                }
            )
        if (openFolderDialog)
            AlertDialog(
                shape = RoundedCornerShape(25.dp),
                onDismissRequest = { openFolderDialog = false },
                title = { Text(stringResource(R.string.change_folder)) },
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
                                    .background(if (folder?.id == it.id) MaterialTheme.colorScheme.onBackground else Color.Transparent),
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
                                    tint = if (folder?.id == it.id) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
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
                                    color = if (folder?.id == it.id) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
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
                        Text(stringResource(R.string.delete_note), color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            openDialog = false
                        }) {
                        Text(stringResource(R.string.cancel), color = Color.White)
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