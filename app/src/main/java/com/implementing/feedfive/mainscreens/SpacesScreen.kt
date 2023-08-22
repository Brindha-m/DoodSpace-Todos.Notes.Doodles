package com.implementing.feedfive.mainscreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.implementing.feedfive.R
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.navigation.components.SpaceRegularCard
import com.implementing.feedfive.navigation.components.SpaceWideCard
import com.implementing.feedfive.ui.theme.Blue
import com.implementing.feedfive.ui.theme.Green
import com.implementing.feedfive.ui.theme.Orange
import com.implementing.feedfive.ui.theme.Purple

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpacesScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Spaces",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),

            )
        }
    ) {


        LazyColumn {
            item {
                Spacer(Modifier.height(60.dp))


                Row {
                    SpaceRegularCard(
                        modifier = Modifier.weight(1f, fill = true),
                        title = stringResource(R.string.notes),
                        image = R.drawable.notes_img,
                        backgroundColor = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFF546E7A), // Slightly darker silver at the start
                                0.5f to Color(0xFFB0BEC5), // Darker silver in the middle
                                1f to Color(0xFF949799) // Lighter silver at the end
                            )
                        )

                    ) {
                        navController.navigate(Screen.NotesScreen.route)
                    }

                    SpaceRegularCard(
                        modifier = Modifier.weight(1f, fill = true),
                        title = stringResource(R.string.tasks),
                        image = R.drawable.tasks_img,
                        backgroundColor = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFF0D47A1), // Dark blue at the start
                                0.5f to Color(0xFF1976D2), // Lighter blue in the middle
                                1f to Color(0xFF2196F3) // Lightest blue at the end
                            )
                        )
                    ) {
                        navController.navigate(
                            Screen.TasksScreen.route
                        )
                    }
                }
            }
            item {
                Row {
                    SpaceRegularCard(
                        modifier = Modifier.weight(1f, fill = true),
                        title = stringResource(R.string.diary),
                        image = R.drawable.diary_img,
                        backgroundColor = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFF4CAF50), // Dark green at the start
                                0.5f to Color(0xFF66BB6A), // Lighter green in the middle
                                1f to Color(0xFF81C784) // Lightest green at the end
                            )
                        )

                    ) {
                        navController.navigate(Screen.DiaryScreen.route)
                    }
                    SpaceRegularCard(
                        modifier = Modifier.weight(1f, fill = true),
                        title = stringResource(R.string.bookmarks),
                        image = R.drawable.bookmarks_img,
                        backgroundColor = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color(0xFFF06292), // Dark pink at the start
                                0.5f to Color(0xFFEC407A), // Lighter pink in the middle
                                1f to Color(0xFFFF4081) // Lightest pink at the end
                            )
                        )

                    ) {
                        navController.navigate(Screen.BookmarksScreen.route)
                    }
                }
            }
            item {
                SpaceWideCard(
                    title = stringResource(R.string.calendar),
                    image = R.drawable.calendar_img,
                    backgroundColor = Brush.linearGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFF9575CD), // Medium purple at the start
                            0.5f to Color(0xFF7E57C2), // Slightly darker medium purple in the middle
                            1f to Color(0xFF673AB7) // Darker medium purple at the end
                        )
                    )
                ) {
                    navController.navigate(Screen.CalendarScreen.route)
                }
            }
            item { Spacer(Modifier.height(60.dp)) }
        }
    }

}


@Preview
@Composable
fun SpacesScreenPreview() {
    SpacesScreen(
        navController = rememberNavController()
    )
}

