package com.implementing.feedfive.mainscreens

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.implementing.feedfive.doodle_space.DoodleScreen
import com.implementing.feedfive.doodle_space.contoller.DoodleController
import com.implementing.feedfive.inappscreens.bookmark.screens.BookmarkDetailsScreen
import com.implementing.feedfive.inappscreens.bookmark.screens.BookmarkSearchScreen
import com.implementing.feedfive.inappscreens.bookmark.screens.BookmarksScreen
import com.implementing.feedfive.inappscreens.calendar.screen.CalendarEventDetailsScreen
import com.implementing.feedfive.inappscreens.calendar.screen.CalendarScreen
import com.implementing.feedfive.inappscreens.diary.chart.DiaryChartScreen
import com.implementing.feedfive.inappscreens.diary.screens.DiaryEntryDetailsScreen
import com.implementing.feedfive.inappscreens.diary.screens.DiaryScreen
import com.implementing.feedfive.inappscreens.diary.screens.DiarySearchScreen
import com.implementing.feedfive.inappscreens.note.screens.NoteDetailsScreen
import com.implementing.feedfive.inappscreens.note.screens.NoteFolderDetailsScreen
import com.implementing.feedfive.inappscreens.note.screens.NotesScreen
import com.implementing.feedfive.inappscreens.note.screens.NotesSearchScreen
import com.implementing.feedfive.inappscreens.task.screens.TaskDetailScreen
import com.implementing.feedfive.inappscreens.task.screens.TasksScreen
import com.implementing.feedfive.inappscreens.task.screens.TasksSearchScreen
import com.implementing.feedfive.mainscreens.viewmodel.MainViewModel
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.ui.theme.FeedFiveTheme
import com.implementing.feedfive.ui.theme.Jost
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.ThemeSettings
import com.implementing.feedfive.util.toFontFamily
import com.implementing.feedfive.util.toInt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeMode = viewModel.themeMode.collectAsState(initial = ThemeSettings.AUTO.value)
            val font = viewModel.font.collectAsState(initial = Jost.toInt())
            val blockScreenshots = viewModel.blockScreenshots.collectAsState(initial = false)
            val systemUiController = rememberSystemUiController()

            val startUpScreen = Screen.SpacesScreen.route

            LaunchedEffect(blockScreenshots.value) {
                if (blockScreenshots.value) {
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE
                    )
                } else
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }

            val isDarkMode = when (themeMode.value) {
                ThemeSettings.DARK.value -> true
                ThemeSettings.LIGHT.value -> false
                else -> isSystemInDarkTheme()
            }
            SideEffect {
                systemUiController.setSystemBarsColor(
                    if (isDarkMode) Color.Black else Color.White,
                    darkIcons = !isDarkMode
                )
            }

            FeedFiveTheme(darkTheme = isDarkMode, fontFamily = font.value.toFontFamily()) {
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

                        composable(
                            Screen.DiaryDetailScreen.route,
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



                        composable(Screen.DoodleScreen.route) {
                            DoodleScreen(doodleController = DoodleController())
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

                        // Tasks Section
                        composable(Screen.TaskSearchScreen.route) {
                            TasksSearchScreen(navController = navController)
                        }

                        composable(
                            route = Screen.TasksScreen.route,
                            arguments = listOf(navArgument(Constants.ADD_TASK_ARG) {
                                type = NavType.BoolType
                                defaultValue = false
                            }),
                            deepLinks = listOf(
                                navDeepLink {
                                    uriPattern =
                                        "${Constants.TASKS_SCREEN_URI}/{${Constants.ADD_TASK_ARG}}"
                                }
                            )
                        ) {
                            TasksScreen(
                                navController = navController,
                                addTask = it.arguments?.getBoolean(Constants.ADD_TASK_ARG) ?: false
                            )

                        }

                        composable(
                            route = Screen.TaskDetailScreen.route,
                            arguments = listOf(navArgument(Constants.TASK_ID_ARG) {
                                type = NavType.IntType
                            }),
                            deepLinks = listOf(
                                navDeepLink {
                                    uriPattern =
                                        "${Constants.TASK_DETAILS_URI}/{${Constants.TASK_ID_ARG}}"
                                }
                            )
                        ) {
                            TaskDetailScreen(
                                navController = navController,
                                taskId = it.arguments?.getInt(Constants.TASK_ID_ARG)!!
                            )
                        }


                        // Calendar Section

                        composable(
                            Screen.CalendarScreen.route,
                            deepLinks = listOf(
                                navDeepLink {
                                    uriPattern = Constants.CALENDAR_SCREEN_URI
                                }
                            )
                        ) {
                            CalendarScreen(navController = navController)
                        }


                        composable(
                            Screen.CalendarEventDetailsScreen.route,
                            arguments = listOf(navArgument(Constants.CALENDAR_EVENT_ARG) {
                                type = NavType.StringType
                            }),
                            deepLinks = listOf(
                                navDeepLink {
                                    uriPattern =
                                        "${Constants.CALENDAR_DETAILS_SCREEN_URI}/{${Constants.CALENDAR_EVENT_ARG}}"
                                }
                            )
                        ) {
                            CalendarEventDetailsScreen(
                                navController = navController,
                                eventJson = it.arguments?.getString(Constants.CALENDAR_EVENT_ARG) ?: ""
                            )
                        }

                    }

                }
            }
        }
    }
}

