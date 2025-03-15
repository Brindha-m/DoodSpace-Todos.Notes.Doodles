package com.implementing.cozyspace.inappscreens.bookmark.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.cozyspace.R
import com.implementing.cozyspace.inappscreens.bookmark.BookmarkEvent
import com.implementing.cozyspace.inappscreens.bookmark.viewmodel.BookmarksViewModel
import com.implementing.cozyspace.model.Bookmark
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.util.isValidUrl

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Bookmark",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    if (state.bookmark != null) IconButton(onClick = { openDialog = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.delete_icon),
                            contentDescription = stringResource(R.string.delete_bookmark),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    if (url.isValidUrl())
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data =
                                Uri.parse(if (!url.startsWith("http://") && !url.startsWith("https://")) "http://$url" else url)
                            context.startActivity(intent)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.open_link_bookmark),
                                contentDescription = stringResource(R.string.open_link),
                                modifier = Modifier.size(23.dp),
                            )
                        }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Image(
                            painter = painterResource(id = R.drawable.backarrow_ic),
                            contentDescription = "back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
//                elevation = 0.dp,
            )
        },
        floatingActionButton = {
            if (url.isValidUrl()) {
                FloatingActionButton(
                    onClick = {
                        val bookmarkToUpdateOrAdd = Bookmark(
                            title = title,
                            description = description,
                            url = url
                        )
                        if (state.bookmark != null) {
                            viewModel.onEvent(
                                BookmarkEvent.UpdateBookmark(
                                    state.bookmark.copy(
                                        title = bookmarkToUpdateOrAdd.title,
                                        description = bookmarkToUpdateOrAdd.description,
                                        url = bookmarkToUpdateOrAdd.url
                                    )
                                )
                            )
                        } else {
                            viewModel.onEvent(
                                BookmarkEvent.AddBookmark(bookmarkToUpdateOrAdd)
                            )
                        }
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.save_ic),
                        contentDescription = stringResource(R.string.bookmark_saved),
                        modifier = Modifier.size(25.dp),
                        tint = MaterialTheme.colorScheme.scrim
                    )
                }
            }
        }

    ) { paddingValues ->

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
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = stringResource(R.string.title)) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),

            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(text = stringResource(R.string.description)) },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
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
                    style = MaterialTheme.typography.bodyMedium,
                    text = if (state.bookmark != null) stringResource(R.string.cancel_changes)
                    else stringResource(R.string.cancel),
                )
            }
        }

        if (openDialog)
            AlertDialog(
                shape = RoundedCornerShape(25.dp),
                onDismissRequest = { openDialog = false },
                title = { Text(stringResource(R.string.delete_bookmark_confirmation_title), style = MaterialTheme.typography.bodyMedium, fontSize = 16.sp) },
                text = {
                    Text(
                        stringResource(
                            R.string.delete_bookmark_confirmation_message
                        ),
                        style = MaterialTheme.typography.bodyMedium
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
                        Text(stringResource(R.string.delete_bookmark), color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    }
                },
                dismissButton = {
                    Button(
                        shape = RoundedCornerShape(25.dp),
                        onClick = {
                            openDialog = false
                        }) {
                        Text(stringResource(R.string.cancel), color = Color.White, style = MaterialTheme.typography.bodyMedium)
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