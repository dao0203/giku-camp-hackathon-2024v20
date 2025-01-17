package com.dao0203.gikucampv20.android.feature.training

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dao0203.gikucampv20.android.R
import com.dao0203.gikucampv20.android.feature.training.component.CircularTimer
import com.dao0203.gikucampv20.android.ui.theme.MainTheme
import com.dao0203.gikucampv20.android.util.MainPreview
import com.dao0203.gikucampv20.android.util.StringRes
import com.dao0203.gikucampv20.feature.training.TrainingRestUiState
import com.dao0203.gikucampv20.feature.training.TrainingRestViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrainingRestScreen(navigateToTrainingWithCamera: () -> Unit) {
    val viewModel = koinViewModel<TrainingRestViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }
    TrainingRestContent(
        uiState = uiState,
        onClickSkip = { navigateToTrainingWithCamera() },
        onClickStart = { navigateToTrainingWithCamera() },
    )
}

@Composable
private fun TrainingRestContent(
    uiState: TrainingRestUiState,
    onClickSkip: () -> Unit,
    onClickStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) {
        Box(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularTimer(
                progress = uiState.progress,
                progressColor = MaterialTheme.colorScheme.primaryContainer,
                backGroundColor = MaterialTheme.colorScheme.surface,
                size = 300.dp,
            )
            if (uiState.showTimeText) {
                Text(
                    text = uiState.remainingRestTime,
                    style =
                        TextStyle(
                            fontSize = 100.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                )
            }
            Button(
                onClick = {
                    if (uiState.enableStartButton) {
                        onClickStart()
                    } else {
                        onClickSkip()
                    }
                },
                shape = RoundedCornerShape(10),
                modifier =
                    Modifier
                        .size(
                            width = 200.dp,
                            height = 50.dp,
                        )
                        .align(BiasAlignment(0f, 0.5f)),
                colors =
                    ButtonDefaults.buttonColors().let { colors ->
                        if (uiState.enableStartButton) {
                            colors.copy(containerColor = colors.containerColor)
                        } else {
                            colors.copy(containerColor = colors.disabledContainerColor)
                        }
                    },
            ) {
                Text(
                    text =
                        if (uiState.enableStartButton) {
                            stringResource(StringRes.start)
                        } else {
                            stringResource(R.string.skip)
                        },
                    style = TextStyle(fontSize = 20.sp),
                )
            }
        }
    }
}

@MainPreview
@Composable
private fun TrainingRestContentPreview() {
    MainTheme {
        TrainingRestContent(
            uiState =
                TrainingRestUiState(
                    remainingRestTime = "10",
                    progress = 1f,
                    showTimeText = true,
                    enableStartButton = true,
                    remainingSets = 3,
                ),
            onClickSkip = {},
            onClickStart = {},
        )
    }
}

@MainPreview
@Composable
private fun TrainingRestScreenPreview() {
    MainTheme {
        TrainingRestContent(
            uiState =
                TrainingRestUiState(
                    remainingRestTime = "10",
                    progress = 1f,
                    showTimeText = true,
                    enableStartButton = false,
                    remainingSets = 3,
                ),
            onClickSkip = {},
            onClickStart = {},
        )
    }
}
