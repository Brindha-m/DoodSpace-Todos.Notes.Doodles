package com.implementing.feedfive.inappscreens.note.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.note.NoteEvent
import com.implementing.feedfive.inappscreens.note.NoteItem
import com.implementing.feedfive.inappscreens.note.viewmodel.NotesViewModel
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.ItemView

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteFolderDetailsScreen(
    navController: NavHostController,
    id: Int,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val uiState = viewModel.notesUiState
    val folder = uiState.folder

    var openDeleteDialog by remember { mutableStateOf(false) }
    var openEditDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {viewModel.onEvent(NoteEvent.GetFolderNotes(id)) }
    LaunchedEffect(uiState) {
        if (viewModel.notesUiState.navigateUp) {
            navController.popBackStack(route = Screen.NotesScreen.route, inclusive = false)
        }
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(
                uiState.error
            )
            viewModel.onEvent(NoteEvent.ErrorDisplayed)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = folder?.name ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(painter = painterResource(id = R.drawable.backarrow_ic), contentDescription = "back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
//                elevation = 0.dp,
                actions = {
                    IconButton(onClick = { openDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, stringResource(R.string.delete_folder))
                    }
                    IconButton(onClick = { openEditDialog = true }) {
                        Icon(Icons.Default.Edit, stringResource(R.string.delete_folder))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(
                        Screen.NoteDetailsScreen.route.replace(
                            "{${Constants.NOTE_ID_ARG}}",
                            "${-1}"
                        ).replace(
                            "{${Constants.FOLDER_ID}}",
                            "$id"
                        )
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_note),
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->

        if (uiState.noteView == ItemView.LIST) {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(
                    top = 12.dp,
                    bottom = 24.dp,
                    start = 12.dp,
                    end = 12.dp
                )
            ) {
                items(uiState.folderNotes, key = { it.id }) { note ->
                    NoteItem(
                        note = note,
                        onClick = {
                            navController.navigate(
                                Screen.NoteDetailsScreen.route.replace(
                                    "{${Constants.NOTE_ID_ARG}}",
                                    "${note.id}"
                                ).replace(
                                    "{${Constants.FOLDER_ID}}",
                                    "-1"
                                )
                            )
                        }
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier.padding(top = 80.dp, start = 15.dp, end = 15.dp),
                columns = GridCells.Adaptive(150.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.folderNotes) { note ->
                    key(note.id) {

                        NoteItem(
                            note = note,
                            onClick = {
                                navController.navigate(
                                    Screen.NoteDetailsScreen.route.replace(
                                        "{${Constants.NOTE_ID_ARG}}",
                                        "${note.id}"
                                    ).replace(
                                        "{${Constants.FOLDER_ID}}",
                                        "-1"
                                    )
                                )
                            },
//                            modifier = Modifier.padding(bottom = 12.dp)
                            modifier = Modifier.animateItemPlacement().height(69.dp)
                        )
                    }
                }
            }
        }
        if (openDeleteDialog)
            AlertDialog(
                shape = RoundedCornerShape(25.dp),
                onDismissRequest = { openDeleteDialog = false },
                title = { Text(stringResource(R.string.delete_note_confirmation_title)) },
                text = {
                    Text(
                        stringResource(
                            R.string.delete_folder_confirmation_message,
                        )
                    )
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            viewModel.onEvent(NoteEvent.DeleteFolder(folder!!))
                            openDeleteDialog = false
                        },
                    ) {
                        Text(stringResource(R.string.delete_folder), color = Color.White)
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
        if (openEditDialog){
            var name by remember { mutableStateOf(folder?.name ?: "") }
            AlertDialog(
                onDismissRequest = { openEditDialog = false },
                title = {
                    Text(
                        text = stringResource(id = R.string.edit_folder),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                text = {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = {
                            Text(
                                text = stringResource(id = R.string.name),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                    )
                },
                confirmButton = {
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            viewModel.onEvent(NoteEvent.UpdateFolder(folder?.copy(name = name)!!))
                            openEditDialog = false
                        },
                    ) {
                        Text(stringResource(R.string.save), color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(
                        shape = RoundedCornerShape(25.dp),
                        onClick = { openEditDialog = false },
                    ) {
                        Text(stringResource(R.string.cancel), color = Color.White)
                    }
                }
            )
        }
    }
}