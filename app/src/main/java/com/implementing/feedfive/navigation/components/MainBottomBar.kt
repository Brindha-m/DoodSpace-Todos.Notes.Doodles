package com.implementing.feedfive.navigation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.implementing.feedfive.navigation.BottomNavItem


@Composable
fun MainBottomBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
) {
    NavigationBar(containerColor = Color.Transparent) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentDestination = navBackStackEntry?.destination


        items.forEach {
            NavigationBarItem(
                icon = {
                    Icon(
                    if (currentDestination?.route == it.route)
                        painterResource(it.iconSelected)
                    else
                        painterResource(it.icon),
                    contentDescription = stringResource(it.title),
                ) },
                selected = currentDestination?.route == it.route,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false,

            )
        }
    }
}