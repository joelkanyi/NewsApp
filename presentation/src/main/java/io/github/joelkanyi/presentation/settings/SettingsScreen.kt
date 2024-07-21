/*
 * Copyright 2024 Joel Kanyi.

 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.joelkanyi.presentation.settings

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.joelkanyi.designsystem.R
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.designsystem.theme.Theme
import io.github.joelkanyi.presentation.model.AppTheme
import io.github.joelkanyi.presentation.model.SettingsOption
import io.github.joelkanyi.presentation.navigation.Destinations
import io.github.joelkanyi.presentation.utils.getAppVersionCode
import io.github.joelkanyi.presentation.utils.getAppVersionName
import io.github.joelkanyi.presentation.utils.getLanguageName
import io.github.joelkanyi.presentation.utils.getThemeName

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    var shouldShowThemesDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val theme by viewModel.theme.collectAsState()
    val language by viewModel.language.collectAsState()
    val context = LocalContext.current

    SettingsScreenContent(
        modifier = modifier,
        settingsOptions = context.settingsOptions(
            selectedTheme = theme,
            selectedLanguage = language,
            appVersion = "${context.getAppVersionName()} (${context.getAppVersionCode()})"
        ),
        themeOptions = context.themes(),
        shouldShowThemesDialog = shouldShowThemesDialog,
        selectedTheme = theme,
        onDismissThemesDialog = {
            shouldShowThemesDialog = false
        },
        updateTheme = {
            viewModel.updateTheme(it)
        },
        onClickOption = {
            when (it) {
                context.getString(io.github.joelkanyi.presentation.R.string.theme) -> {
                    shouldShowThemesDialog = true
                }

                context.getString(io.github.joelkanyi.presentation.R.string.language) -> {
                    navController.navigate(Destinations.Language)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    shouldShowThemesDialog: Boolean,
    selectedTheme: Int,
    onDismissThemesDialog: () -> Unit,
    updateTheme: (Int) -> Unit,
    onClickOption: (String) -> Unit,
    settingsOptions: List<SettingsOption>,
    themeOptions: List<AppTheme>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .testTag(stringResource(io.github.joelkanyi.presentation.R.string.settings_screen))
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(io.github.joelkanyi.presentation.R.string.settings),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            items(settingsOptions) { option ->
                SettingsOptionItem(
                    settingsOption = option,
                    onClick = {
                        onClickOption(option.title)
                    }
                )
            }
        }
    }

    if (shouldShowThemesDialog) {
        ThemesDialog(
            themeOptions = themeOptions,
            selectedTheme = selectedTheme,
            onDismiss = onDismissThemesDialog,
            onSelectTheme = {
                onDismissThemesDialog()
                updateTheme(it)
            }
        )
    }
}

@Composable
fun SettingsOptionItem(
    settingsOption: SettingsOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        leadingContent = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = settingsOption.icon),
                contentDescription = settingsOption.title
            )
        },
        headlineContent = {
            Text(
                text = settingsOption.title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            Text(
                text = settingsOption.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}

@Composable
fun ThemesDialog(
    themeOptions: List<AppTheme>,
    onDismiss: () -> Unit,
    onSelectTheme: (Int) -> Unit,
    selectedTheme: Int,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(0),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(io.github.joelkanyi.presentation.R.string.select_theme),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(themeOptions) { theme ->
                    ThemeItem(
                        themeName = theme.themeName,
                        themeValue = theme.themeValue,
                        icon = theme.icon,
                        onSelectTheme = onSelectTheme,
                        isSelected = theme.themeValue == selectedTheme
                    )
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun ThemeItem(
    themeName: String,
    themeValue: Int,
    icon: Int,
    onSelectTheme: (Int) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onSelectTheme(themeValue)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(.75f),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = icon),
                contentDescription = null
            )
            Text(
                modifier = Modifier,
                text = themeName,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (isSelected) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = Icons.Default.Check,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    NewsAppTheme {
        val context = LocalContext.current
        SettingsScreenContent(
            themeOptions = context.themes(),
            settingsOptions = context.settingsOptions(
                selectedTheme = Theme.FOLLOW_SYSTEM.themeValue,
                selectedLanguage = 0,
                appVersion = "1.0.0"
            ),
            shouldShowThemesDialog = false,
            selectedTheme = Theme.FOLLOW_SYSTEM.themeValue,
            onDismissThemesDialog = {},
            updateTheme = {},
            onClickOption = {}
        )
    }
}

fun Context.settingsOptions(
    selectedTheme: Int,
    selectedLanguage: Int,
    appVersion: String,
) = listOf(
    SettingsOption(
        title = getString(io.github.joelkanyi.presentation.R.string.theme),
        description = selectedTheme.getThemeName(context = this),
        icon = R.drawable.ic_theme,
    ),
    SettingsOption(
        title = getString(io.github.joelkanyi.presentation.R.string.language),
        description = selectedLanguage.getLanguageName(context = this),
        icon = R.drawable.ic_language,
    ),
    SettingsOption(
        title = getString(io.github.joelkanyi.presentation.R.string.app_version),
        description = appVersion,
        icon = R.drawable.ic_info,
    )
)

fun Context.themes() = listOf(
    AppTheme(
        themeName = getString(io.github.joelkanyi.presentation.R.string.use_system_settings),
        themeValue = Theme.FOLLOW_SYSTEM.themeValue,
        icon = R.drawable.settings_suggest
    ),
    AppTheme(
        themeName = getString(io.github.joelkanyi.presentation.R.string.light_mode),
        themeValue = Theme.LIGHT_THEME.themeValue,
        icon = R.drawable.light_mode
    ),
    AppTheme(
        themeName = getString(io.github.joelkanyi.presentation.R.string.dark_mode),
        themeValue = Theme.DARK_THEME.themeValue,
        icon = R.drawable.dark_mode
    ),
    AppTheme(
        themeName = getString(io.github.joelkanyi.presentation.R.string.material_you),
        themeValue = Theme.MATERIAL_YOU.themeValue,
        icon = R.drawable.wallpaper
    )
)
