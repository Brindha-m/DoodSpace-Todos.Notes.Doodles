package com.implementing.cozyspace.mainscreens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.implementing.cozyspace.doodle_space.DoodleScreen
import com.implementing.cozyspace.doodle_space.contoller.DoodleController
import com.implementing.cozyspace.inappscreens.bookmark.screens.BookmarkDetailsScreen
import com.implementing.cozyspace.inappscreens.bookmark.screens.BookmarkSearchScreen
import com.implementing.cozyspace.inappscreens.bookmark.screens.BookmarksScreen
import com.implementing.cozyspace.inappscreens.calendar.screen.CalendarEventDetailsScreen
import com.implementing.cozyspace.inappscreens.calendar.screen.CalendarScreen
import com.implementing.cozyspace.inappscreens.diary.chart.DiaryChartScreen
import com.implementing.cozyspace.inappscreens.diary.screens.DiaryEntryDetailsScreen
import com.implementing.cozyspace.inappscreens.diary.screens.DiaryScreen
import com.implementing.cozyspace.inappscreens.diary.screens.DiarySearchScreen
import com.implementing.cozyspace.inappscreens.note.screens.NoteDetailsScreen
import com.implementing.cozyspace.inappscreens.note.screens.NoteFolderDetailsScreen
import com.implementing.cozyspace.inappscreens.note.screens.NotesScreen
import com.implementing.cozyspace.inappscreens.note.screens.NotesSearchScreen
import com.implementing.cozyspace.inappscreens.settings.ImportExportScreen
import com.implementing.cozyspace.inappscreens.task.screens.TaskDetailScreen
import com.implementing.cozyspace.inappscreens.task.screens.TasksScreen
import com.implementing.cozyspace.inappscreens.task.screens.TasksSearchScreen
import com.implementing.cozyspace.mainscreens.viewmodel.MainViewModel
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.ui.theme.Avenir
import com.implementing.cozyspace.ui.theme.FeedFiveTheme
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.ThemeSettings
import com.implementing.cozyspace.util.toFontFamily
import com.implementing.cozyspace.util.toInt
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    /* For In app update */
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.FLEXIBLE


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // in app updates
//        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
//        if (updateType == AppUpdateType.FLEXIBLE) {
//            appUpdateManager.registerListener(installStateUpdatedListener)
//        }

//        checkForAppUpdates()

        setContent {
            val themeMode = viewModel.themeMode.collectAsState(initial = ThemeSettings.DARK.value)
            val font = viewModel.font.collectAsState(initial = Avenir.toInt())
            val blockScreenshots = viewModel.blockScreenshots.collectAsState(initial = false)
            val systemUiController = rememberSystemUiController()

            val startUpScreen = Screen.SpacesScreen.route

            LaunchedEffect(true) {
                if (!isNotificationPermissionGranted() && !isAlarmManagerPermissionGranted())
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS,
                            Manifest.permission.SCHEDULE_EXACT_ALARM
                        ),
                        0
                    )

            }

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
                        startDestination = Screen.Splash.route,
                        navController = navController
                    ) {

                        composable(Screen.Splash.route) {
                            AnimatedSplashScreen(navController = navController)
                        }

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

                        // Doodle Screen

                        composable(Screen.DoodleScreen.route) {
                            DoodleScreen(doodleController = DoodleController())
                        }

                        // Sketch Guess Screen

//                        composable(Screen.SketchGuess.route) {
//                            MainNavigation()
//                        }


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
                                eventJson = it.arguments?.getString(Constants.CALENDAR_EVENT_ARG)
                                    ?: ""
                            )
                        }

                        composable(Screen.ImportExportScreen.route) {
                            ImportExportScreen(navController = navController)
                        }

                    }

                }
            }
        }
    }


    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Toast.makeText(
                applicationContext,
                "Download successful✅. Restarting in 5 seconds",
                Toast.LENGTH_LONG
            ).show()
            lifecycleScope.launch {
                delay(3.seconds)
                appUpdateManager.completeUpdate()
            }
        }
    }



    private fun checkForAppUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }
            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    123
                )
            }
        }
    }

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (resultCode != RESULT_OK) {
                println("Something Went Wrong..")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (updateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateType,
                        this,
                        123
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }


     */

//    override fun onDestroy() {
//        super.onDestroy()
//        if (updateType == AppUpdateType.FLEXIBLE) {
//            appUpdateManager.unregisterListener(installStateUpdatedListener)
//        }
//    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.S
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isAlarmManagerPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SCHEDULE_EXACT_ALARM
        ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.S
    }


}

