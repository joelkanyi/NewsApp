package io.github.joelkanyi.presentation.language

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.joelkanyi.designsystem.theme.NewsAppTheme
import io.github.joelkanyi.presentation.R
import io.github.joelkanyi.presentation.language.LanguageUtils.getLanguageConfiguration
import io.github.joelkanyi.presentation.language.LanguageUtils.getLanguageDesc
import io.github.joelkanyi.presentation.language.LanguageUtils.getLanguageNumber
import io.github.joelkanyi.presentation.language.LanguageUtils.languageMap
import io.github.joelkanyi.presentation.language.LanguageUtils.setLanguage
import io.github.joelkanyi.presentation.utils.LANGUAGE_SYSTEM_DEFAULT

@Composable
fun LanguageScreen(
    navController: NavController,
    viewModel: LanguageViewModel = hiltViewModel(),
) {
    val language by viewModel.language.collectAsState()

    LanguageScreenContent(
        onBackPressed = { navController.popBackStack() },
        languageMap = languageMap,
        selectedLanguage = language.getLanguageNumber(),
        onLanguageSelected = {
            viewModel.setLanguage(it)
            setLanguage(getLanguageConfiguration(it))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageScreenContent(
    onBackPressed: () -> Unit = {},
    languageMap: Map<Int, String>,
    selectedLanguage: Int,
    onLanguageSelected: (Int) -> Unit = {},
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.language),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.testTag(stringResource(R.string.back_icon)),
                        onClick = onBackPressed
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            item {
                LanguageItem(
                    text = stringResource(R.string.follow_system),
                    selected = selectedLanguage == LANGUAGE_SYSTEM_DEFAULT,
                ) { onLanguageSelected(LANGUAGE_SYSTEM_DEFAULT) }
            }

            items(languageMap.size) {
                val languageData = languageMap.entries.elementAt(it)
                LanguageItem(
                    text = getLanguageDesc(languageData.key),
                    selected = selectedLanguage == languageData.key,
                ) { onLanguageSelected(languageData.key) }
            }
        }
    }
}

@Composable
fun LanguageItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier =
        Modifier.selectable(
            selected = selected,
            onClick = onClick,
        ),
    ) {
        Row(
            modifier =
            modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
private fun LanguagePagePreview() {
    var language by remember {
        mutableIntStateOf(1)
    }
    val map =
        buildMap {
            repeat(2) {
                put(it + 1, "")
            }
        }
    NewsAppTheme {
        LanguageScreenContent(
            languageMap = map,
            selectedLanguage = language,
            onLanguageSelected = { language = it },
        )
    }
}
