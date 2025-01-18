package com.dao0203.gikucampv20.android.feature.training.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dao0203.gikucampv20.android.R
import com.dao0203.gikucampv20.android.ui.theme.MainTheme
import com.dao0203.gikucampv20.android.util.MainPreviews

@Composable
fun SkipAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                )
            }
        },
        icon = {
            Icon(Icons.Default.Warning, contentDescription = null)
        },
        title = {
            Text(
                text = stringResource(R.string.skip_alert_title),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = modifier,
    )
}

@MainPreviews
@Composable
private fun SkipAlertDialogPreview() {
    MainTheme {
        SkipAlertDialog(
            onDismissRequest = {},
            onConfirm = {},
        )
    }
}
