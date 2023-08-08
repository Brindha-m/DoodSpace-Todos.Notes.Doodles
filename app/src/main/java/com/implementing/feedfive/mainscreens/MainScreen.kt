package com.implementing.feedfive.mainscreens

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.implementing.feedfive.navigation.BottomNavItem
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.navigation.components.MainBottomBar
import com.implementing.feedfive.navigation.components.NavigationGraph

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    startUpScreen: String,
    mainNavController: NavHostController
) {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem.Dashboard, BottomNavItem.Spaces, BottomNavItem.Settings
    )
    Scaffold(
        bottomBar = {
            MainBottomBar(navController = navController, items = bottomNavItems )
        }
    ) {
        NavigationGraph(
            navController = navController,
            mainNavController = mainNavController,
            startUpScreen = startUpScreen)

    }
}