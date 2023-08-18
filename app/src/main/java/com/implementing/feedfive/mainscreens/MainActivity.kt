package com.implementing.feedfive.mainscreens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.inappscreens.bookmark.screens.BookmarkDetailsScreen
import com.implementing.feedfive.inappscreens.bookmark.screens.BookmarkSearchScreen
import com.implementing.feedfive.inappscreens.bookmark.screens.BookmarksScreen
import com.implementing.feedfive.inappscreens.diary.chart.DiaryChartScreen
import com.implementing.feedfive.inappscreens.diary.screens.DiaryEntryDetailsScreen
import com.implementing.feedfive.inappscreens.diary.screens.DiaryScreen
import com.implementing.feedfive.inappscreens.diary.screens.DiarySearchScreen
import com.implementing.feedfive.inappscreens.note.screens.NoteDetailsScreen
import com.implementing.feedfive.inappscreens.note.screens.NoteFolderDetailsScreen
import com.implementing.feedfive.inappscreens.note.screens.NotesScreen
import com.implementing.feedfive.inappscreens.note.screens.NotesSearchScreen
import com.implementing.feedfive.ui.theme.FeedFiveTheme
import com.implementing.feedfive.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val startUpScreen = Screen.SpacesScreen.route

            FeedFiveTheme {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        startDestination = Screen.Main.route,
                        navController = navController
                    ) {
                        composable(Screen.Main.route) {
                            MainScreen(
                                startUpScreen = startUpScreen,
                                mainNavController = navController
                            )
                        }

                        // Bookmark Section

                        composable(Screen.BookmarksScreen.route) {
                            BookmarksScreen(navController = navController)
                        }

                        composable(
                            Screen.BookmarkDetailScreen.route,
                            arguments = listOf(navArgument(Constants.BOOKMARK_ID_ARG) {
                                type = NavType.IntType
                            })
                            ) {
                            BookmarkDetailsScreen(
                                navController = navController,
                                bookmarkId = it.arguments?.getInt(Constants.BOOKMARK_ID_ARG)!!
                            )
                        }

                        composable(Screen.BookmarkSearchScreen.route) {
                            BookmarkSearchScreen(navController = navController)
                        }
                        // Diary Section

                        composable(Screen.DiaryScreen.route) {
                            DiaryScreen(navController = navController)
                        }

                        composable(Screen.DiarySearchScreen.route) {
                            DiarySearchScreen(navController = navController)
                        }

                        composable(Screen.DiaryDetailScreen.route,
                            arguments = listOf(navArgument(Constants.DIARY_ID_ARG) {
                                type = NavType.IntType
                            })
                        ) {
                            DiaryEntryDetailsScreen(
                                navController = navController,
                                it.arguments?.getInt(Constants.DIARY_ID_ARG)!!
                            )
                        }

                        composable(Screen.DiaryChartScreen.route) {
                            DiaryChartScreen()
                        }

                        // Notes Section
                        composable(Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(
                            Screen.NoteDetailsScreen.route,
                            arguments = listOf(navArgument(Constants.NOTE_ID_ARG) {
                                type = NavType.IntType
                            },
                                navArgument(Constants.FOLDER_ID) {
                                    type = NavType.IntType
                                }
                            ),
                        ) {
                            NoteDetailsScreen(
                                navController,
                                it.arguments?.getInt(Constants.NOTE_ID_ARG) ?: -1,
                                it.arguments?.getInt(Constants.FOLDER_ID) ?: -1
                            )
                        }

                        composable(Screen.NoteSearchScreen.route) {
                            NotesSearchScreen(navController = navController)
                        }

                        composable(
                            Screen.NoteFolderDetailsScreen.route,
                            arguments = listOf(navArgument(Constants.FOLDER_ID) {
                                type = NavType.IntType
                            })
                        ) {
                            NoteFolderDetailsScreen(
                                navController = navController,
                                it.arguments?.getInt(Constants.FOLDER_ID) ?: -1
                            )
                        }

                    }

                }
            }
        }
    }
}

