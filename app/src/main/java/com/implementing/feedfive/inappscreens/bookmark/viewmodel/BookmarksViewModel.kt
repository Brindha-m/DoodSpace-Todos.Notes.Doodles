package com.implementing.feedfive.inappscreens.bookmark.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implementing.feedfive.R
import com.implementing.feedfive.domain.usecase.bookmark.AddBookmarkUsecase
import com.implementing.feedfive.domain.usecase.bookmark.DeleteBookmarkUseCase
import com.implementing.feedfive.domain.usecase.bookmark.GetAllBookmarksUseCase
import com.implementing.feedfive.domain.usecase.bookmark.GetBookmarkUseCase
import com.implementing.feedfive.domain.usecase.bookmark.SearchBookmarkUseCase
import com.implementing.feedfive.domain.usecase.bookmark.UpdateBookmarkUseCase
import com.implementing.feedfive.domain.usecase.settings.GetSettingsUseCase
import com.implementing.feedfive.domain.usecase.settings.SaveSettingsUseCase
import com.implementing.feedfive.getString
import com.implementing.feedfive.model.Bookmark
import com.implementing.feedfive.inappscreens.bookmark.BookmarkEvent
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.ItemView
import com.implementing.feedfive.util.Order
import com.implementing.feedfive.util.OrderType
import com.implementing.feedfive.util.isValidUrl
import com.implementing.feedfive.util.toInt
import com.implementing.feedfive.util.toNotesView
import com.implementing.feedfive.util.toOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val addBookmark: AddBookmarkUsecase,
    private val updateBookmark: UpdateBookmarkUseCase,
    private val deleteBookmark: DeleteBookmarkUseCase,
    private val searchBookmarks: SearchBookmarkUseCase,
    private val getAllBookmarks: GetAllBookmarksUseCase,
    private val getBookmark: GetBookmarkUseCase,
    getSettings: GetSettingsUseCase,
    private val saveSettings: SaveSettingsUseCase

): ViewModel() {
/*
 Create -
 1. init {}
 2. onEvent() function
 3. uiState data class
 4. In coroutines we have Job datatype, flow, combine, onEach, launchIn
 5. getOrders() func inside init
  */

    var uiState by mutableStateOf(UiState())
        private set
    // private set --> private setter for the uiState property.

    private var getBookmarksJob: Job? = null

    init {
        viewModelScope.launch {
            combine(
                getSettings(
                    intPreferencesKey(Constants.BOOKMARK_ORDER_KEY),
                    Order.DateModified(OrderType.ASC()).toInt()
                ),
                getSettings(
                    intPreferencesKey(Constants.BOOKMARK_VIEW_KEY),
                    ItemView.LIST.value
                )
            ) { order, view ->
                uiState = uiState.copy(bookmarksOrder = order.toOrder())

                getBookmarks(order.toOrder())

                if (uiState.bookmarksView.value != view) {
                    uiState = uiState.copy(bookmarksView = view.toNotesView())
                }

            }.collect()
        }
    }


    fun onEvent(event: BookmarkEvent) {
        when(event) {

            is BookmarkEvent.AddBookmark -> viewModelScope.launch {
                uiState = if (
                    event.bookmark.url.isBlank() &&
                    event.bookmark.title.isBlank() &&
                    event.bookmark.description.isBlank()
                )
                    uiState.copy(navigateUp = true)

                else{
                    if (event.bookmark.url.isValidUrl()) {
                        addBookmark(event.bookmark)
                        uiState.copy(navigateUp = true)
                    } else
                        uiState.copy(error = "Invalid URL")
                }
            }

            is BookmarkEvent.DeleteBookmark -> viewModelScope.launch {
                deleteBookmark(event.bookmark)
                uiState = uiState.copy(navigateUp = true)
            }

            is BookmarkEvent.GetBookmark -> viewModelScope.launch {
                val bookmark = getBookmark(event.bookmarkId)
                uiState = uiState.copy(bookmark = bookmark)
            }

            is BookmarkEvent.SearchBookmarks -> viewModelScope.launch {
                val bookmark = searchBookmarks(event.query)
                uiState = uiState.copy(searchBookmarks = bookmark)
            }

            is BookmarkEvent.UpdateBookmark -> viewModelScope.launch {
                uiState = if (!event.bookmark.url.isValidUrl()) {
                    uiState.copy(error = getString(R.string.invalid_url))
                } else {
                    updateBookmark(event.bookmark.copy(updatedDate = System.currentTimeMillis()))
                    uiState.copy(navigateUp = true)
                }
            }
            is BookmarkEvent.UpdateOrder -> viewModelScope.launch {
                saveSettings(
                    intPreferencesKey(Constants.BOOKMARK_ORDER_KEY),
                    event.order.toInt()
                )
            }
            is BookmarkEvent.UpdateView -> viewModelScope.launch {
                saveSettings(
                    intPreferencesKey(Constants.BOOKMARK_VIEW_KEY),
                    event.view.value
                )
            }

            BookmarkEvent.ErrorDisplayed -> uiState = uiState.copy(error = null)

        }
    }

    data class UiState(
        val bookmarks: List<Bookmark> = emptyList(),
        val bookmarksOrder: Order = Order.DateModified(OrderType.ASC()),
        val bookmarksView: ItemView = ItemView.LIST,
        val bookmark: Bookmark? = null,
        val error: String? = null,
        val searchBookmarks: List<Bookmark> = emptyList(),
        val navigateUp: Boolean = false
    )

    private fun getBookmarks(order: Order) {
        getBookmarksJob?.cancel()
        getBookmarksJob = getAllBookmarks(order)
            .onEach { bookmarks ->
                uiState = uiState.copy(
                    bookmarks = bookmarks,
                    bookmarksOrder = order
                )
            }.launchIn(viewModelScope)
    }

}


