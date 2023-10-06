package com.implementing.cozyspace.inappscreens.note.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implementing.cozyspace.R
import com.implementing.cozyspace.domain.usecase.notes.AddNoteUseCase
import com.implementing.cozyspace.domain.usecase.notes.DeleteNoteUseCase
import com.implementing.cozyspace.domain.usecase.notes.GetAllNoteFoldersUseCase
import com.implementing.cozyspace.domain.usecase.notes.GetAllNotesUseCase
import com.implementing.cozyspace.domain.usecase.notes.GetNoteUseCase
import com.implementing.cozyspace.domain.usecase.notes.GetNotesByFolderUseCase
import com.implementing.cozyspace.domain.usecase.notes.SearchNotesUseCase
import com.implementing.cozyspace.domain.usecase.notes.UpdateNoteUseCase
import com.implementing.cozyspace.domain.usecase.notes.notefolder.AddNoteFolderUseCass
import com.implementing.cozyspace.domain.usecase.notes.notefolder.DeleteNoteFolderUseCass
import com.implementing.cozyspace.domain.usecase.notes.notefolder.UpdateNoteFolderUseCass
import com.implementing.cozyspace.domain.usecase.settings.GetSettingsUseCase
import com.implementing.cozyspace.domain.usecase.settings.SaveSettingsUseCase
import com.implementing.cozyspace.getString
import com.implementing.cozyspace.inappscreens.note.NoteEvent
import com.implementing.cozyspace.model.Note
import com.implementing.cozyspace.model.NoteFolder
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.ItemView
import com.implementing.cozyspace.util.Order
import com.implementing.cozyspace.util.OrderType
import com.implementing.cozyspace.util.toInt
import com.implementing.cozyspace.util.toNotesView
import com.implementing.cozyspace.util.toOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val allNotes: GetAllNotesUseCase,
    private val getNote: GetNoteUseCase,
    private val updateNote: UpdateNoteUseCase,
    private val addNote: AddNoteUseCase,
    private val searchNotes: SearchNotesUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val getSettings: GetSettingsUseCase,
    private val saveSettings: SaveSettingsUseCase,
    private val getAllFolders: GetAllNoteFoldersUseCase,
    private val createFolder: AddNoteFolderUseCass,
    private val deleteFolder: DeleteNoteFolderUseCass,
    private val updateFolder: UpdateNoteFolderUseCass,
    private val getFolderNotes: GetNotesByFolderUseCase
): ViewModel() {

    var notesUiState by mutableStateOf((UiState()))
        private set

    private var getNotesJob: Job? = null
    private var getFolderNotesJob: Job? = null

    init {
        viewModelScope.launch {
            combine(
                getSettings(
                    intPreferencesKey(Constants.NOTES_ORDER_KEY),
                    Order.DateModified(OrderType.ASC()).toInt()
                ),
                getSettings(
                    intPreferencesKey(Constants.NOTE_VIEW_KEY),
                    ItemView.LIST.value
                ),
                getAllFolders()
            ) { order, view, folders ->
                notesUiState = notesUiState.copy(notesOrder = order.toOrder(), folders = folders)
                getNotes(order.toOrder())
                if (notesUiState.noteView.value != view) {
                    notesUiState = notesUiState.copy(noteView = view.toNotesView())
                }
            }.collect()
        }
    }

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.AddNote -> viewModelScope.launch {
                notesUiState = if (event.note.title.isBlank() && event.note.content.isBlank()) {
                    notesUiState.copy(navigateUp = true)
                } else {
                    addNote(
                        event.note.copy(
                            createdDate = System.currentTimeMillis(),
                            updatedDate = System.currentTimeMillis()
                        )
                    )
                    notesUiState.copy(navigateUp = true)
                }
            }
            is NoteEvent.DeleteNote -> viewModelScope.launch {
                deleteNote(event.note)
                notesUiState = notesUiState.copy(navigateUp = true)
            }
            is NoteEvent.GetNote -> viewModelScope.launch {
                val note = getNote(event.noteId)
                val folder = getAllFolders().first().firstOrNull { it.id == note.folderId }
                notesUiState = notesUiState.copy(note = note, folder = folder)
            }
            is NoteEvent.SearchNotes -> viewModelScope.launch {
                val notes = searchNotes(event.query)
                notesUiState = notesUiState.copy(searchNotes = notes)
            }
            is NoteEvent.UpdateNote -> viewModelScope.launch {
                notesUiState = if (event.note.title.isBlank() && event.note.content.isBlank())
                    notesUiState.copy(error = getString(R.string.error_empty_note))
                else {
                    updateNote(event.note.copy(updatedDate = System.currentTimeMillis()))
                    notesUiState.copy(navigateUp = true)
                }
            }
            is NoteEvent.UpdateOrder -> viewModelScope.launch {
                saveSettings(
                    intPreferencesKey(Constants.NOTES_ORDER_KEY),
                    event.order.toInt()
                )
            }
            is NoteEvent.ErrorDisplayed -> notesUiState = notesUiState.copy(error = null)
            NoteEvent.ToggleReadingMode -> notesUiState =
                notesUiState.copy(readingMode = !notesUiState.readingMode)
            is NoteEvent.PinNote -> viewModelScope.launch {
                updateNote(notesUiState.note?.copy(pinned = !notesUiState.note?.pinned!!)!!)
            }
            is NoteEvent.UpdateView -> viewModelScope.launch {
                saveSettings(
                    intPreferencesKey(Constants.NOTE_VIEW_KEY),
                    event.view.value
                )
            }
            is NoteEvent.CreateFolder -> viewModelScope.launch {
                if (event.folder.name.isBlank()) {
                    notesUiState = notesUiState.copy(error = getString(R.string.error_empty_title))
                } else {
                    if (!notesUiState.folders.contains(event.folder)) {
                        createFolder(event.folder)
                    } else {
                        notesUiState = notesUiState.copy(error = getString(R.string.error_folder_exists))
                    }
                }
            }
            is NoteEvent.DeleteFolder -> viewModelScope.launch {
                deleteFolder(event.folder)
                notesUiState = notesUiState.copy(navigateUp = true)
            }
            is NoteEvent.UpdateFolder -> viewModelScope.launch {
                if (event.folder.name.isBlank()) {
                    notesUiState = notesUiState.copy(error = getString(R.string.error_empty_title))
                } else {
                    if (!notesUiState.folders.contains(event.folder)) {
                        updateFolder(event.folder)
                        notesUiState = notesUiState.copy(folder = event.folder)
                    } else {
                        notesUiState = notesUiState.copy(error = getString(R.string.error_folder_exists))
                    }
                }
            }
            is NoteEvent.GetFolderNotes -> {
                getNotesFromFolder(event.id, notesUiState.notesOrder)
            }
            is NoteEvent.GetFolder -> viewModelScope.launch {
                val folder = getAllFolders().first().firstOrNull { it.id == event.id }
                notesUiState = notesUiState.copy(folder = folder)
            }
        }
    }


    data class UiState(
        val notes: List<Note> = emptyList(),
        val note: Note? = null,
        val notesOrder: Order = Order.DateModified(OrderType.ASC()),
        val error: String? = null,
        val noteView: ItemView = ItemView.GRID,
        val navigateUp: Boolean = false,
        val readingMode: Boolean = true,
        val searchNotes: List<Note> = emptyList(),
        val folders: List<NoteFolder> = emptyList(),
        val folderNotes: List<Note> = emptyList(),
        val folder: NoteFolder? = null
    )

    private fun getNotes(order: Order) {
        getNotesJob?.cancel()
        getNotesJob = allNotes(order)
            .onEach { notes ->
                notesUiState = notesUiState.copy(
                    notes = notes,
                    notesOrder = order
                )
            }.launchIn(viewModelScope)
    }

    private fun getNotesFromFolder(id: Int, order: Order) {
        getFolderNotesJob?.cancel()
        getFolderNotesJob = getFolderNotes(id, order)
            .onEach { notes ->
                val noteFolder = getAllFolders().first().firstOrNull() { it.id == id }
                notesUiState = notesUiState.copy(
                    folderNotes = notes,
                    folder = noteFolder
                )
            }.launchIn(viewModelScope)
    }

}