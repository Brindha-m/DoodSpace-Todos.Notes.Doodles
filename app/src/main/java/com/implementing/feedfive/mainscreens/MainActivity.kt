package com.implementing.feedfive.mainscreens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.implementing.feedfive.navigation.Screen
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

                        composable(Screen.BookmarksScreen.route) {
//                            BookmarksScreen(navController = navController)
                        }

                        composable(
                            Screen.BookmarkDetailScreen.route,
                            arguments = listOf(navArgument(Constants.BOOKMARK_ID_ARG) {
                                type = NavType.IntType
                            })
                            ) {
//                            BookmarkDetailScreen(
//                                navController = navController,
//                                it.arguments?.getInt(Constants.BOOKMARK_ID_ARG)!!
//                            )
                        }

                        composable(Screen.BookmarkSearchScreen.route) {
//                            BookmarkSearchScreen(navController = navController)
                        }

                    }


                }
            }
        }
    }
}

