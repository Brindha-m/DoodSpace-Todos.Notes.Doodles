package com.implementing.feedfive.inappscreens.bookmark.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.feedfive.R
import com.implementing.feedfive.getString
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.inappscreens.bookmark.BookmarkEvent
import com.implementing.feedfive.inappscreens.bookmark.BookmarkItem
import com.implementing.feedfive.inappscreens.bookmark.viewmodel.BookmarksViewModel
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.ItemView
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookmarkSearchScreen(
    navController: NavHostController,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold( snackbarHost = { SnackbarHost(snackbarHostState) } )
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            var query by rememberSaveable {
                mutableStateOf("")
            }
            LaunchedEffect(query){viewModel.onEvent(BookmarkEvent.SearchBookmarks(query))}
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(true){focusRequester.requestFocus()}
            OutlinedTextField(
                value = query,
                onValueChange = {query = it},
                label = { Text(stringResource(R.string.search_bookmarks)) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester)
            )
            if (state.bookmarksView == ItemView.LIST){
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(state.searchBookmarks, key = {it.id}) { bookmark ->
                        BookmarkItem(
                            bookmark = bookmark,
                            onClick = {
                                navController.navigate(
                                    Screen.BookmarkDetailScreen.route.replace(
                                        "{${Constants.BOOKMARK_ID_ARG}}",
                                        "${bookmark.id}"
                                    )
                                )
                            },
                            onInvalidUrl = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        getString(R.string.invalid_url)
                                    )
                                }
                            }
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(12.dp)
                ){
                    items(state.searchBookmarks){ bookmark ->
                        key(bookmark.id) {
                            BookmarkItem(
                                bookmark = bookmark,
                                onClick = {
                                    navController.navigate(
                                        Screen.BookmarkDetailScreen.route.replace(
                                            "{${Constants.BOOKMARK_ID_ARG}}",
                                            "${bookmark.id}"
                                        )
                                    )
                                },
                                onInvalidUrl = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            getString(R.string.invalid_url)
                                        )
                                    }
                                },
                                modifier = Modifier.animateItemPlacement()
                            )
                        }
                    }
                }
            }
        }
    }
}