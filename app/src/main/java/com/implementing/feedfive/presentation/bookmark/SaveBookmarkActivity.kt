package com.implementing.feedfive.presentation.bookmark

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.implementing.feedfive.R
import com.implementing.feedfive.model.Bookmark
import com.implementing.feedfive.presentation.bookmark.viewmodel.BookmarksViewModel
import com.implementing.feedfive.util.isValidUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaveBookmarkActivity : ComponentActivity() {

    private val viewModel: BookmarksViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null) {
            if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
                val url = intent.getStringExtra(Intent.EXTRA_TEXT)
                if (!url.isNullOrBlank()) {
                    if (url.isValidUrl()) {
                        viewModel.onEvent(
                            BookmarkEvent.AddBookmark(
                                Bookmark(
                                    url = url.trim(),
                                    createdDate = System.currentTimeMillis(),
                                    updatedDate = System.currentTimeMillis()
                                )
                            )
                        )
                        Toast.makeText(this, getString(R.string.bookmark_saved), Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, getString(R.string.invalid_url), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.invalid_url), Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }
}