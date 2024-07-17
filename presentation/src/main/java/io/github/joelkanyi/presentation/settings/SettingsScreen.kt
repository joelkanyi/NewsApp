package io.github.joelkanyi.presentation.settings

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.joelkanyi.designsystem.R
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.designsystem.theme.Theme
import io.github.joelkanyi.presentation.model.AppTheme
import io.github.joelkanyi.presentation.model.SettingsOption
import io.github.joelkanyi.presentation.utils.getThemeName

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    var shouldShowThemesDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val theme by viewModel.theme.collectAsState()

    SettingsScreenContent(
        settingsOptions = settingsOptions(theme),
        themeOptions = themes,
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
                "Theme" -> shouldShowThemesDialog = true
                "Language" -> {
                    navController.navigate("language")
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
) {
    Scaffold(
        modifier = Modifier
            .testTag("settings_screen")
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
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
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(0),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Themes",
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
) {
    Row(
        modifier = Modifier
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
fun PreviewSettingsScreen() {
    NewsAppTheme {
        SettingsScreenContent(
            themeOptions = themes,
            settingsOptions = settingsOptions(Theme.FOLLOW_SYSTEM.themeValue),
            shouldShowThemesDialog = false,
            selectedTheme = Theme.FOLLOW_SYSTEM.themeValue,
            onDismissThemesDialog = {},
            updateTheme = {},
            onClickOption = {}
        )
    }
}

fun settingsOptions(
    selectedTheme: Int,
) = listOf(
    SettingsOption(
        title = "Theme",
        description = selectedTheme.getThemeName(),
        icon = R.drawable.ic_theme,
    ),
    SettingsOption(
        title = "Language",
        description = "English",
        icon = R.drawable.ic_language,
    ),
    SettingsOption(
        title = "App Version",
        description = "0.0.1-debug",
        icon = R.drawable.ic_info,
    )
)

val themes = listOf(
    AppTheme(
        themeName = "Use System Settings",
        themeValue = Theme.FOLLOW_SYSTEM.themeValue,
        icon = R.drawable.settings_suggest
    ),
    AppTheme(
        themeName = "Light Mode",
        themeValue = Theme.LIGHT_THEME.themeValue,
        icon = R.drawable.light_mode
    ),
    AppTheme(
        themeName = "Dark Mode",
        themeValue = Theme.DARK_THEME.themeValue,
        icon = R.drawable.dark_mode
    ),
    AppTheme(
        themeName = "Material You",
        themeValue = Theme.MATERIAL_YOU.themeValue,
        icon = R.drawable.wallpaper
    )
)
