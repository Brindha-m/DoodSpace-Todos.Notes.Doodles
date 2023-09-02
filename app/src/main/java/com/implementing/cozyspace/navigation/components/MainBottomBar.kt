package com.implementing.cozyspace.navigation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.implementing.cozyspace.navigation.BottomNavItem


@Composable
fun MainBottomBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
) {
//    NavigationBar(containerColor = Color.Transparent) {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//
//        val currentDestination = navBackStackEntry?.destination
//
//        items.forEach {
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        if (currentDestination?.route == it.route)
//                            painterResource(it.iconSelected)
//                        else
//                            painterResource(it.icon),
//                        contentDescription = stringResource(it.title),
//                    )
//                },
//                selected = currentDestination?.route == it.route,
//                onClick = {
//                    navController.navigate(it.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                },
//                alwaysShowLabel = false,
//
//                )
//        }
//    }
}