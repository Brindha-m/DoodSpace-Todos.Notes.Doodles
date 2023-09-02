package com.implementing.cozyspace.inappscreens.note.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.note.NoteEvent
import com.implementing.cozyspace.inappscreens.note.NoteItem
import com.implementing.cozyspace.inappscreens.note.viewmodel.NotesViewModel
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.ItemView

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotesSearchScreen(
    navController: NavHostController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.notesUiState
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        var query by rememberSaveable {
            mutableStateOf("")
        }
        LaunchedEffect(query){viewModel.onEvent(NoteEvent.SearchNotes(query))}

        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(true){focusRequester.requestFocus()}

        OutlinedTextField(
            value = query,
            onValueChange = {query = it},
            label = { Text(stringResource(R.string.search_notes)) },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester)
        )
        if (state.noteView == ItemView.LIST){
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(state.searchNotes, key = {it.id}) { note ->
                    NoteItem(
                        note = note,
                        onClick = {
                            navController.navigate(
                                Screen.NoteDetailsScreen.route.replace(
                                    "{${Constants.NOTE_ID_ARG}}",
                                    "${note.id}"
                                ).replace(
                                    "{${Constants.FOLDER_ID}}",
                                    ""
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
                contentPadding = PaddingValues(12.dp)
            ){
                items(state.searchNotes){ note ->
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
                                        ""
                                    )
                                )
                            },
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }
    }
}