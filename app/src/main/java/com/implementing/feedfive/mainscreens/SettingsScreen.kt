package com.implementing.feedfive.mainscreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.implementing.feedfive.R
import com.implementing.feedfive.inappscreens.settings.SettingsBasicLinkItem
import com.implementing.feedfive.inappscreens.settings.SettingsItemCard
import com.implementing.feedfive.inappscreens.settings.viewmodel.SettingsViewModel
import com.implementing.feedfive.navigation.Screen
import com.implementing.feedfive.ui.theme.Jost
import com.implementing.feedfive.ui.theme.Rubik
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.StartUpScreenSettings
import com.implementing.feedfive.util.ThemeSettings
import com.implementing.feedfive.util.getName
import com.implementing.feedfive.util.toFontFamily
import com.implementing.feedfive.util.toInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                val theme = viewModel
                    .getSettings(
                        intPreferencesKey(Constants.SETTINGS_THEME_KEY), ThemeSettings.AUTO.value
                    ).collectAsState(
                        initial = ThemeSettings.AUTO.value
                    )
                ThemeSettingsItem(theme.value) {
                    when (theme.value) {
                        ThemeSettings.AUTO.value -> viewModel.saveSettings(
                            intPreferencesKey(Constants.SETTINGS_THEME_KEY),
                            ThemeSettings.LIGHT.value
                        )

                        ThemeSettings.LIGHT.value -> viewModel.saveSettings(
                            intPreferencesKey(Constants.SETTINGS_THEME_KEY),
                            ThemeSettings.DARK.value
                        )

                        ThemeSettings.DARK.value -> viewModel.saveSettings(
                            intPreferencesKey(Constants.SETTINGS_THEME_KEY),
                            ThemeSettings.AUTO.value
                        )
                    }
                }
            }
            item {
                val screen = viewModel
                    .getSettings(
                        intPreferencesKey(Constants.DEFAULT_START_UP_SCREEN_KEY),
                        StartUpScreenSettings.SPACES.value
                    ).collectAsState(
                        initial = StartUpScreenSettings.SPACES.value
                    )
                StartUpScreenSettingsItem(
                    screen.value,
                    {
                        viewModel.saveSettings(
                            intPreferencesKey(Constants.DEFAULT_START_UP_SCREEN_KEY),
                            StartUpScreenSettings.SPACES.value
                        )
                    },
                    {
                        viewModel.saveSettings(
                            intPreferencesKey(Constants.DEFAULT_START_UP_SCREEN_KEY),
                            StartUpScreenSettings.DASHBOARD.value
                        )
                    }
                )
            }
            item {
                val screen = viewModel
                    .getSettings(
                        intPreferencesKey(Constants.APP_FONT_KEY),
                        Rubik.toInt()
                    ).collectAsState(
                        initial = Rubik.toInt()
                    )
                AppFontSettingsItem(
                    screen.value,
                ) { font ->
                    viewModel.saveSettings(
                        intPreferencesKey(Constants.APP_FONT_KEY),
                        font
                    )
                }
            }
            item {
                val block = viewModel
                    .getSettings(
                        booleanPreferencesKey(Constants.BLOCK_SCREENSHOTS_KEY),
                        false
                    ).collectAsState(
                        initial = false
                    )
                BlockScreenshotsSettingsItem(
                    block.value
                ) {
                    viewModel.saveSettings(
                        booleanPreferencesKey(Constants.BLOCK_SCREENSHOTS_KEY),
                        it
                    )
                }
            }

            item {
                SettingsItemCard(
                    cornerRadius = 16.dp,
                    onClick = {
                        navController.navigate(Screen.ImportExportScreen.route)
                    }
                ) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_import_export),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.import_data),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(R.string.about),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 12.dp)
                )
            }

            item {
                SettingsBasicLinkItem(
                    title = R.string.app_version,
                    icon = R.drawable.ic_code,
//                    subtitle = BuildConfig.VERSION_NAME,
                    link = Constants.GITHUB_RELEASES_LINK
                )
            }
            item {
                SettingsBasicLinkItem(
                    title = R.string.project_on_github,
                    icon = R.drawable.ic_github,
                    link = Constants.PROJECT_GITHUB_LINK
                )
            }

            item {
                SettingsBasicLinkItem(
                    title = R.string.privacy_policy,
                    icon = R.drawable.ic_privacy,
                    link = Constants.PRIVACY_POLICY_LINK
                )
            }

            item {
                Text(
                    text = stringResource(R.string.product),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 12.dp)
                )
            }

            item {
                SettingsBasicLinkItem(
                    title = R.string.request_feature_report_bug,
                    icon = R.drawable.ic_feature_issue,
                    link = Constants.GITHUB_ISSUES_LINK
                )
            }

            item {
                SettingsBasicLinkItem(
                    title = R.string.project_roadmap,
                    icon = R.drawable.ic_roadmap,
                    link = Constants.PROJECT_ROADMAP_LINK
                )
            }
            item { Spacer(Modifier.height(60.dp)) }
        }

    }
}


@Composable
fun ThemeSettingsItem(theme: Int = 0, onClick: () -> Unit = {}) {
    SettingsItemCard(
        onClick = onClick,
        cornerRadius = 18.dp
    ) {
        Text(
            text = stringResource(R.string.app_theme),
            style = MaterialTheme.typography.bodyMedium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = when (theme) {
                    ThemeSettings.LIGHT.value -> stringResource(R.string.light_theme)
                    ThemeSettings.DARK.value -> stringResource(R.string.dark_theme)
                    else -> stringResource(R.string.auto_theme)
                },
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = when (theme) {
                    ThemeSettings.LIGHT.value -> painterResource(id = R.drawable.ic_sun)
                    ThemeSettings.DARK.value -> painterResource(id = R.drawable.ic_dark)
                    else -> painterResource(id = R.drawable.ic_auto)
                },
                contentDescription = theme.toString()
            )
        }
    }
}


@Composable
fun StartUpScreenSettingsItem(
    screen: Int,
    onSpacesClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    SettingsItemCard(
        cornerRadius = 16.dp,
        onClick = {
            expanded = true
        },
    ) {
        Text(
            text = stringResource(R.string.start_up_screen),
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (screen) {
                        StartUpScreenSettings.SPACES.value -> stringResource(R.string.spaces)
                        StartUpScreenSettings.DASHBOARD.value -> stringResource(R.string.dashboard)
                        else -> stringResource(R.string.spaces)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                content = {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = R.string.spaces),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onSpacesClick()
                            expanded = false
                        })

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = R.string.dashboard),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onDashboardClick()
                            expanded = false
                        })
                }
            )
        }
    }
}

@Composable
fun AppFontSettingsItem(
    selectedFont: Int,
    onFontChange: (Int) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val fonts = listOf(
        FontFamily.Default,
        Rubik,
        Jost,
        FontFamily.SansSerif
    )
    SettingsItemCard(
        cornerRadius = 16.dp,
        onClick = {
            expanded = true
        },
    ) {
        Text(
            text = stringResource(R.string.app_font),
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    selectedFont.toFontFamily().getName(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                fonts.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.getName(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onFontChange(it.toInt())
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun BlockScreenshotsSettingsItem(
    block: Boolean,
    onBlockClick: (Boolean) -> Unit = {}
) {
    SettingsItemCard(
        cornerRadius = 16.dp,
        onClick = {
            onBlockClick(!block)
        },
        vPadding = 10.dp
    ) {
        Text(
            text = stringResource(R.string.block_screenshots),
            style = MaterialTheme.typography.bodyMedium
        )
        Switch(checked = block, onCheckedChange = {
            onBlockClick(it)
        })
    }
}
