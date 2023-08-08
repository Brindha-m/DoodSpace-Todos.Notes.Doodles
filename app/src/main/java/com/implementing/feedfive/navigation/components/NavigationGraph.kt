package com.implementing.feedfive.navigation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.implementing.feedfive.mainscreens.DashboardScreen
import com.implementing.feedfive.mainscreens.SettingsScreen
import com.implementing.feedfive.mainscreens.SpacesScreen
import com.implementing.feedfive.navigation.Screen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    mainNavController: NavHostController,
    startUpScreen: String
) {
    NavHost(navController = navController, startDestination = startUpScreen){

        composable(Screen.DashboardScreen.route){
            DashboardScreen(mainNavController)
        }
        composable(Screen.SpacesScreen.route){
            SpacesScreen(mainNavController)
        }
        composable(Screen.SettingsScreen.route){
            SettingsScreen(mainNavController)
        }
    }
}