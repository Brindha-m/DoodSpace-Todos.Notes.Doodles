package com.implementing.feedfive.inappscreens.bookmark.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.feedfive.R
import com.implementing.feedfive.model.Bookmark
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.inappscreens.bookmark.BookmarkEvent
import com.implementing.feedfive.inappscreens.bookmark.viewmodel.BookmarksViewModel
import com.implementing.feedfive.util.isValidUrl

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookmarkDetailsScreen(
    navController: NavHostController,
    bookmarkId: Int,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        if (bookmarkId != -1) {
            viewModel.onEvent(BookmarkEvent.GetBookmark(bookmarkId))
        }
    }
    val state = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }

    var openDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    var title by rememberSaveable { mutableStateOf(state.bookmark?.title ?: "") }
    var description by rememberSaveable { mutableStateOf(state.bookmark?.description ?: "") }
    var url by rememberSaveable { mutableStateOf(state.bookmark?.url ?: "") }

    LaunchedEffect(state.bookmark) {
        if (state.bookmark != null) {
            title = state.bookmark.title
            description = state.bookmark.description
            url = state.bookmark.url
        }
    }
    LaunchedEffect(state) {
        if (state.navigateUp) {
            openDialog = false
            navController.popBackStack(route = Screen.BookmarksScreen.route, inclusive = false)
        }
        if (state.error != null) {
            snackbarHostState.showSnackbar(
                state.error
            )
            viewModel.onEvent(BookmarkEvent.ErrorDisplayed)
        }
    }
    BackHandler {
        addOrUpdateBookmark(
            Bookmark(
                title = title,
                description = description,
                url = url
            ),
            state.bookmark,
            onNotChanged = {
                navController.popBackStack(
                    route = Screen.BookmarksScreen.route,
                    inclusive = false
                )
            },
            onUpdate = {
                if (state.bookmark != null) {
                    viewModel.onEvent(
                        BookmarkEvent.UpdateBookmark(
                            state.bookmark.copy(
                                title = title,
                                description = description,
                                url = url
                            )
                        )
                    )
                } else {
                    viewModel.onEvent(
                        BookmarkEvent.AddBookmark(
                            Bookmark(
                                title = title,
                                description = description,
                                url = url
                            )
                        )
                    )
                }

            }
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) } ,
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    if (state.bookmark != null) IconButton(onClick = { openDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.delete_bookmark)
                        )
                    }
                    if (url.isValidUrl())
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(if (!url.startsWith("http://") && !url.startsWith("https://")) "http://$url" else url)
                            context.startActivity(intent)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_open_link),
                                contentDescription = stringResource(R.string.open_link),
                                modifier = Modifier.size(24.dp),
                            )
                        }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
//                elevation = 0.dp,
            )
        },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            Spacer(Modifier.height(60.dp))

            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text(text = stringResource(R.string.url)) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = stringResource(R.string.title)) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = stringResource(R.string.description)) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            TextButton(
                onClick = {
                    if (state.bookmark != null)
                        url = state.bookmark.url
                    else navController.popBackStack(
                        route = Screen.BookmarksScreen.route,
                        inclusive = false
                    )
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = if (state.bookmark != null) stringResource(R.string.cancel_changes)
                    else stringResource(R.string.cancel)
                )
            }
        }
        if (openDialog)
            AlertDialog(
                shape = RoundedCornerShape(25.dp),
                onDismissRequest = { openDialog = false },
                title = { Text(stringResource(R.string.delete_bookmark_confirmation_title)) },
                text = {
                    Text(
                        stringResource(
                            R.string.delete_bookmark_confirmation_message
                        )
                    )
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            viewModel.onEvent(BookmarkEvent.DeleteBookmark(state.bookmark!!))
                        },
                    ) {
                        Text(stringResource(R.string.delete_bookmark), color = Color.White)
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

private fun addOrUpdateBookmark(
    newBookmark: Bookmark,
    bookmark: Bookmark? = null,
    onNotChanged: () -> Unit = {},
    onUpdate: (Bookmark) -> Unit,
) {
    if (bookmark != null) {
        if (bookmarkChanged(newBookmark, bookmark))
            onUpdate(bookmark)
        else
            onNotChanged()
    } else {
        onUpdate(newBookmark)
    }
}

private fun bookmarkChanged(
    bookmark: Bookmark,
    newBookmark: Bookmark
): Boolean {
    return bookmark.title != newBookmark.title ||
            bookmark.description != newBookmark.description ||
            bookmark.url != newBookmark.url
}