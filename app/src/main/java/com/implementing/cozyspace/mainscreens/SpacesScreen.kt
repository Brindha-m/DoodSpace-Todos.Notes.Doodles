package com.implementing.cozyspace.mainscreens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.cozyspace.R
import com.implementing.cozyspace.festive_animations.fireworks.FireworkCenterView
import com.implementing.cozyspace.mainscreens.viewmodel.MainViewModel
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.navigation.components.SpaceRegularCard
import com.implementing.cozyspace.navigation.components.SpaceRegularCardMiddle
import com.implementing.cozyspace.navigation.components.SpaceWideCard
import com.implementing.cozyspace.util.ThemeSettings

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpacesScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val themeMode = viewModel.themeMode.collectAsState(initial = ThemeSettings.DARK.value)

    Scaffold(
        topBar = {
            TopAppBar( modifier = Modifier.height(44.dp),
                title = {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                    )
                    {

                        Image(
                            painter = when (themeMode.value) {
                                ThemeSettings.DARK.value -> painterResource(id = R.drawable.dood_space_splash)
                                ThemeSettings.LIGHT.value -> painterResource(id = R.drawable.dood_space_light)
                                else -> {painterResource(id = R.drawable.dood_space_light)}
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .size(105.dp)
                                .align(Alignment.CenterEnd)
                        )

                        Image(
                            painter = painterResource(id = R.drawable.cozy_bg_light),
                            contentDescription = null,
                            modifier = Modifier
                                .size(118.dp)
                                .align(Alignment.CenterStart)
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),)
        }
    ) {


        LazyColumn {

            item {
                Spacer(Modifier.height(50.dp))
                Column {
                    SpaceWideCard(
                        title = "Doodle Board",
                        image = R.drawable.img_9,
                        backgroundColor = Brush.linearGradient(
                            colorStops = arrayOf(
                                0.48f to Color(0xFF1D1E1F), // More transparent white in the middle
                                0.95f to Color(0xFFC9BDBD),  // Light gray (sky)
                            )
                        )
                    ) {
                        navController.navigate(Screen.DoodleScreen.route)
                    }
                }

            }

            item {
                Row {
                    SpaceRegularCard(
                        modifier = Modifier.weight(1f, fill = true),
                        title = stringResource(R.string.notes),
                        image = R.drawable.note_img,
                        backgroundColor = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFFE49F4A), // Light grey at the start
                                0.5f to Color(0xFFDBA86E), // Darker grey in the middle
                                1f to Color(0xFF93929D) // Lighter shade at the end
                            )
                        )

                    ) {
                        navController.navigate(Screen.NotesScreen.route)
                    }

                    SpaceRegularCard(
                        modifier = Modifier.weight(1f, fill = true),
                        title = stringResource(R.string.diary),
                        image = R.drawable.diary_img,
                        backgroundColor = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFF56C2EB), // Light grey at the start
                                0.5f to Color(0xFF5B93B4), // Darker grey in the middle
                                1f to Color(0xFF93929D) // Dark grey at the end
                            )
                        )

                    ) {
                        navController.navigate(Screen.DiaryScreen.route)
                    }

                }
            }

            item {
                Row {

                    SpaceRegularCard(
                        modifier = Modifier.weight(1f, fill = true),
                        title = stringResource(R.string.tasks),
                        image = R.drawable.tasks_img,
                        backgroundColor = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFF221F3E), // Light grey at the start
                                0.5f to Color(0xFF775A6D), // Darker grey in the middle
                                1f to Color(0xFF93929D) // Dark grey at the end
                            )
                        )
                    ) {
                        navController.navigate(
                            Screen.TasksScreen.route
                        )
                    }

                    SpaceRegularCard(
                        modifier = Modifier.weight(1f, fill = true),
                        title = stringResource(R.string.bookmarks),
                        image = R.drawable.bookmarks_img,
                        backgroundColor = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.2f to Color(0xDA161515), // Dark pink at the start
                                0.5f to Color(0xCE252424), // Lighter pink in the middle
                                1f to Color(0xEB404041) // Dark grey at the end
                            )
                        )

                    ) {
                        navController.navigate(Screen.BookmarksScreen.route)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    SpaceRegularCardMiddle(
                        title = "Calendar",
                        image = R.drawable.calendar_img,
                        backgroundColor = Brush.linearGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFFC9BDBD),  // Light gray (sky)
                                0.4f to Color(0xFF035881), // More transparent white in the middle
                                0.8f to Color(0xFF455A3C),
                                1.2f to Color(0xFFCD853F),
                            )
                        )
                    ) {
                        navController.navigate(Screen.CalendarScreen.route)
                    }
                }
            }

            item { Spacer(Modifier.height(60.dp)) }
        }
    }

    FireworkCenterView()

}





/*
Gradient Green

0f to Color(0xFF141D1B), // Transparent white at the start
0.5f to Color(0xFF3A6B5E), // More transparent white in the middle
1f to Color(0xFF59AB93)  // Fully transparent white at the end

Earth Gradient

0f to Color(0xFFC9BDBD),  // Light gray (sky)
0.4f to Color(0xFF035881), // More transparent white in the middle
0.9f to Color(0xFF455A3C),
1.3f to Color(0xFFCD853F),
 */