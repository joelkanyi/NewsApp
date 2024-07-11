package io.github.joelkanyi.designsystem.components

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        modifier = modifier,
        containerColor = containerColor,
        onDismissRequest = {
            scope.launch {
                bottomSheetState.hide()
                onDismissRequest()
            }
        },
        sheetState = bottomSheetState,
        shape = shape,
    ) {
        content()
    }
}
