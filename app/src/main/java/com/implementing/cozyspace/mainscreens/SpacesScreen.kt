package com.implementing.cozyspace.mainscreens

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewManagerFactory
import com.idapgroup.snowfall.snowmelt
import com.idapgroup.snowfall.types.FlakeType
import com.implementing.cozyspace.R
import com.implementing.cozyspace.festive_animations.ChristmasRide
import com.implementing.cozyspace.festive_animations.randomColor
import com.implementing.cozyspace.mainscreens.viewmodel.MainViewModel
import com.implementing.cozyspace.navigation.Screen
import com.implementing.cozyspace.navigation.components.SpaceRegularCard
import com.implementing.cozyspace.navigation.components.SpaceRegularCardMiddle
import com.implementing.cozyspace.navigation.components.SpaceWideCard
import com.implementing.cozyspace.util.ThemeSettings
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpacesScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val themeMode = viewModel.themeMode.collectAsState(initial = ThemeSettings.DARK.value)


    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .statusBarsPadding()
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                modifier = Modifier.height(44.dp),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp)
                            .background(Color.Transparent)
                    )
                    {

                        Image(
                            painter = when (themeMode.value) {
                                ThemeSettings.DARK.value -> painterResource(id = R.drawable.dood_space_splash)
                                ThemeSettings.LIGHT.value -> painterResource(id = R.drawable.dood_space_light)
                                else -> {
                                    painterResource(id = R.drawable.dood_space_light)
                                }
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .size(135.dp)
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        }
    ) {

        AutoUpdateCheck()

        LazyColumn {

            item {
                Spacer(Modifier.height(30.dp))

                /**  This is the animation on top
                 ***/

                Spacer(Modifier.height(18.dp))
                ChristmasRide()

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

            item {
                Spacer(Modifier.height(65.dp))
            }

        }

//            Diwali(
//                property1 = variants[currentVariantIndex],
//            )

    }

//    //BG
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(15.dp)
    )
    {
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current.density
        val screenHeightPx = (configuration.screenHeightDp * density).toInt()
        // The ratio of the fireworks width to the screen width is 4:5.
        val fireworkWidth = configuration.screenWidthDp / 1.25f
        val fireworkHeight = configuration.screenHeightDp

        val colorLine = randomColor((0..9).random())
        val colorOrb = randomColor((0..9).random())


//        Firework(
//            width = fireworkWidth.dp,
//            height = fireworkHeight.dp,
//            colorLine = colorLine,
//            colorOrb = colorOrb
//        )
        Spacer(modifier = Modifier.width(50.dp))

    }


    ChristmasAnimation()

//    FireworkCenterView(viewModel = fireviewModel, startAnimation = true)

}


@Composable
fun ChristmasAnimation() {

    val rcb: List<Painter> = listOf(
        painterResource(id = R.drawable.rcbbold),
        painterResource(id = R.drawable.sixipl)
    )
    val mi: List<Painter> = listOf(
        painterResource(id = R.drawable.mihitman),
        painterResource(id = R.drawable.rr)
    )

    val srh: List<Painter> = listOf(
        painterResource(id = R.drawable.srh),
        )

    val violet: List<Painter> = listOf(
        painterResource(R.drawable.fouripl),
        painterResource(id = R.drawable.gt)
    )

    val trophy = listOf(
        painterResource(id = R.drawable.trophyipl),
        painterResource(id = R.drawable.cskwp),
    )
    /* olympics shower special
    val game1 = listOf(
        painterResource(id = R.drawable.rugby)
    )
    val game2 = listOf(
        painterResource(id = R.drawable.pingpong)
    )
    val game3 = listOf(
        painterResource(id = R.drawable.football),
        painterResource(id = R.drawable.shuttlecock),
    )
    val game4 = listOf(
        painterResource(id = R.drawable.cricket)
    )
    val game5 = listOf(
        painterResource(id = R.drawable.bowarrow),
    )
    */



    Box(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize()
            .background(Color.Transparent, shape = RoundedCornerShape(2.dp))
            .snowmelt(
                colors = listOf(Color(0xFFFA1F28)),
//                colors = listOf(Color(0xFFF64850)),
                type = FlakeType.Custom(rcb),
                density = 0.002 // from 0.0 to 1.0,
            )
            .snowmelt(
                colors = listOf(Color(0xFFD9C400)),
                type = FlakeType.Custom(trophy),
                density = 0.003,
            )
            .snowmelt(
                colors = listOf(Color(0xFF29B6F6)),
                type = FlakeType.Custom(mi),
                density = 0.002,
            )
            .snowmelt(
                colors = listOf(Color(0xFFFB8C00)),
                type = FlakeType.Custom(srh),
                density = 0.002,
            )

    )
}



@Composable
fun AutoUpdateCheck() {

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val coroutineScope = rememberCoroutineScope()
    val appUpdateManager = remember { AppUpdateManagerFactory.create(context) }

    // Auto-trigger In-App Review
//        LaunchedEffect("review") {
//            val reviewManager = ReviewManagerFactory.create(context)
//            val request = reviewManager.requestReviewFlow()
//            request.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    coroutineScope.launch {
//                        reviewManager.launchReviewFlow(context, task.result)
//                    }
//                }
//            }
//        }

    LaunchedEffect("review") {
        val reviewManager = ReviewManagerFactory.create(context)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Review flow requested successfully")
                coroutineScope.launch {
                    reviewManager.launchReviewFlow(context, task.result)
                    Log.d("Review", "Review dialog launched (won’t show locally)")
                }
            } else {
                Log.d("Review", "Review request failed: ${task.exception}")
            }
        }
    }

    // Auto-trigger In-App Update
    LaunchedEffect("update") {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                coroutineScope.launch {
                    appUpdateManager.startUpdateFlow(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result == RESULT_OK) {
                            // Update started
                        }
                    }
                }
            }
        }
    }
}

