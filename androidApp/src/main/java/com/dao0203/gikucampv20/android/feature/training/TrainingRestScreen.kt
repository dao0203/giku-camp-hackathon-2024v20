package com.dao0203.gikucampv20.android.feature.training

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dao0203.gikucampv20.feature.training.TrainingRestUiState
import com.dao0203.gikucampv20.feature.training.TrainingRestViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrainingRestScreen(navigateToTrainingWithCamera: () -> Unit) {
    val viewModel = koinViewModel<TrainingRestViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TrainingRestContent(
        uiState = uiState,
    )
}

@Composable
private fun TrainingRestContent(
    uiState: TrainingRestUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.padding(it),
        ) {
        }
    }
}
