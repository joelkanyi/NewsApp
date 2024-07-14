package io.github.joelkanyi.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import io.github.joelkanyi.designsystem.theme.NewsAppTheme

@Composable
fun SettingsScreen(
    navController: NavController,
) {
    SettingsScreenContent()
}

@Composable
fun SettingsScreenContent(
    modifier: Modifier = Modifier,
) {
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    NewsAppTheme {
        SettingsScreenContent()
    }
}
