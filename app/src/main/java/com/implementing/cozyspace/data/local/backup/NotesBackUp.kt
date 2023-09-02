package com.implementing.cozyspace.data.local.backup

import com.implementing.cozyspace.model.Note
import com.implementing.cozyspace.model.NoteFolder

data class NotesBackUp(
    val notes: List<Note>,
    val folders: List<NoteFolder>
)