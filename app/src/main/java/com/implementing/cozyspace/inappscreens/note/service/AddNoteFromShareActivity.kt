package com.implementing.cozyspace.inappscreens.note.service

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.bookmark.viewmodel.BookmarksViewModel
import com.implementing.cozyspace.inappscreens.note.NoteEvent
import com.implementing.cozyspace.inappscreens.note.viewmodel.NotesViewModel
import com.implementing.cozyspace.model.Note
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFromShareActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null) {
            if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
                val content = intent.getStringExtra(Intent.EXTRA_TEXT)
                val title = intent.getStringExtra(Intent.EXTRA_SUBJECT)
                if (!content.isNullOrBlank()) {
                    viewModel.onEvent(
                        NoteEvent.AddNote(
                            Note(
                                title = title ?: "",
                                content = content,
                                createdDate = System.currentTimeMillis(),
                                updatedDate = System.currentTimeMillis(),
                            )
                        )
                    )
                    Toast.makeText(this, getString(R.string.note_saved), Toast.LENGTH_SHORT)
                        .show()
                } else
                    Toast.makeText(this, getString(R.string.error_empty_title), Toast.LENGTH_SHORT)
                        .show()
            }
        }
        finish()
    }
}