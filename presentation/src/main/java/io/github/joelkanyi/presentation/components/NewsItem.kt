package io.github.joelkanyi.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.joelkanyi.presentation.model.NewsUiModel
import io.github.joelkanyi.presentation.utils.toRelativeTime

@Composable
fun NewsItem(
    news: NewsUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            NewsImage(
                imageUrl = news.imageUrl,
                contentDescription = news.title,
                modifier =
                    Modifier
                        .height(100.dp)
                        .fillMaxWidth(.32f),
            )

            Column(
                modifier =
                    Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = news.publishedAt.toRelativeTime(),
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Text(
                        text = news.source,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
