package com.implementing.feedfive.mainscreens

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.implementing.feedfive.R
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

    val navigationBarItems = remember {
        NavigationBarItems.values()
    }

        // by default it will start with the spaces in bottom nav - value -> 1
    var selectedIndex by remember {
        mutableStateOf(1)
    }


    Scaffold(
        modifier = Modifier.padding(7.dp),
        bottomBar = {
//            MainBottomBar(navController = navController, items = bottomNavItems )

            AnimatedNavigationBar(
                modifier = Modifier.height(60.dp),
                selectedIndex = selectedIndex,
                cornerRadius = shapeCornerRadius(cornerRadius = 30.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = MaterialTheme.colorScheme.onSecondary,
                ballColor = MaterialTheme.colorScheme.onTertiary
            ) {
                navigationBarItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable { selectedIndex = item.ordinal },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(22.dp),
                            painter = painterResource(id = item.icon),
                            contentDescription = "Bottom Bar",
                            tint = if (selectedIndex == item.ordinal) MaterialTheme.colorScheme.inversePrimary
                            else MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    ) {
        // Display different content based on the selectedIndex
        when (navigationBarItems[selectedIndex]) {
            NavigationBarItems.Dashboard -> DashboardScreen(navController = mainNavController)
            NavigationBarItems.Settings -> SettingsScreen(navController = mainNavController)
            NavigationBarItems.Spaces -> SpacesScreen(navController = mainNavController)
            else -> {}
        }
    }
}

enum class NavigationBarItems(val title: Int, val icon: Int, val iconSelected: Int, val route: String){

    Dashboard(R.string.dashboard, R.drawable.ic_home, R.drawable.ic_home_filled, Screen.DashboardScreen.route),
    Spaces(R.string.spaces, R.drawable.ic_spaces, R.drawable.ic_spaces_filled, Screen.SpacesScreen.route),
    Settings(R.string.settings, R.drawable.ic_settings, R.drawable.ic_settings_filled, Screen.SettingsScreen.route)

}
