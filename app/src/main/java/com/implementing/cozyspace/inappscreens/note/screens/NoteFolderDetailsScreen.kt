package com.implementing.cozyspace.inappscreens.note.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.note.NoteEvent
import com.implementing.cozyspace.inappscreens.note.NoteItem
import com.implementing.cozyspace.inappscreens.note.viewmodel.NotesViewModel
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.ItemView

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
            navController.navigateUp()
//            navController.popBackStack(route = Screen.NotesScreen.route, inclusive = false)
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
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(painter = painterResource(id = R.drawable.backarrow_ic), contentDescription = "back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//                elevation = 0.dp,
                actions = {
                    IconButton(onClick = { openDeleteDialog = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.delete_icon),
                            contentDescription = stringResource(R.string.delete_folder),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { openEditDialog = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.edit_ic),
                            contentDescription = "Edit",
                            modifier = Modifier.size(24.dp)
                        )
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
                    tint = MaterialTheme.colorScheme.scrim
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
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(
                    top = 20.dp,
                    bottom = 24.dp,
                    start = 12.dp,
                    end = 12.dp
                )
            ){
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
                            modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null).padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }
        if (openDeleteDialog)
            AlertDialog(
                shape = RoundedCornerShape(25.dp),
                onDismissRequest = { openDeleteDialog = false },
                title = { Text(stringResource(R.string.delete_note_confirmation_title), style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp) },
                text = {
                    Text(
                        stringResource(
                            R.string.delete_folder_confirmation_message,
                        ),
                        style = MaterialTheme.typography.bodyMedium
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
                        Text(stringResource(R.string.delete_folder), color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    }
                },
                dismissButton = {
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            openDeleteDialog = false
                        }) {
                        Text(stringResource(R.string.cancel), color = Color.White, style = MaterialTheme.typography.bodyMedium)
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
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primaryContainer
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
                        Text(stringResource(R.string.save), color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    }
                },
                dismissButton = {
                    TextButton(
                        shape = RoundedCornerShape(25.dp),
                        onClick = { openEditDialog = false },
                    ) {
                        Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.primaryContainer, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            )
        }
    }
}