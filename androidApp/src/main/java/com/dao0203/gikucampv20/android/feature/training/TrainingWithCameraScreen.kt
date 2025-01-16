package com.dao0203.gikucampv20.android.feature.training

import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dao0203.gikucampv20.android.BuildConfig
import com.dao0203.gikucampv20.android.feature.training.component.CameraPreview
import com.dao0203.gikucampv20.android.feature.training.component.PoseOverlay
import com.dao0203.gikucampv20.android.feature.training.component.TrainingInfoCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrainingWithCameraScreen(
    navigateToRest: () -> Unit,
    navigateToResult: () -> Unit,
) {
    val viewModel = koinViewModel<TrainingWithCameraViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.initialize()
        viewModel.effect.collect {
            when (it) {
                is TrainingWithCameraEffect.NavigateToRest -> navigateToRest()
                is TrainingWithCameraEffect.NavigateToResult -> navigateToResult()
            }
        }
    }
    TrainingWithCameraContent(
        uiState = uiState,
        onAnalyzeImage = viewModel::detectPose,
        onSwitchCamera = viewModel::switchCamera,
        onPassLine = viewModel::updateReps,
        onClickCard = { },
    )
}

@Composable
private fun TrainingWithCameraContent(
    uiState: TrainingWithCameraUiState,
    onAnalyzeImage: (image: ImageProxy) -> Unit,
    onSwitchCamera: () -> Unit,
    onPassLine: () -> Unit,
    onClickCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            Row {
                FloatingActionButton(
                    onClick = onSwitchCamera,
                ) {
                    // TODO: change icon
                    Icon(Icons.Default.Star, contentDescription = null)
                }
                if (BuildConfig.DEBUG) {
                    FloatingActionButton(
                        onClick = onPassLine,
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                    }
                }
            }
        },
    ) {
        Box(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxSize(),
        ) {
            CameraPreview(
                onAnalyzeImage = onAnalyzeImage,
                isBackCamera = uiState.isBackCamera,
            )
            uiState.poseOverlayUiModel?.let { poseOverlayUiModel ->
                PoseOverlay(
                    uiModel = poseOverlayUiModel,
                )
            }
            TrainingInfoCard(
                remainingReps = uiState.remainingReps,
                onClick = onClickCard,
                modifier =
                    Modifier
                        .padding(16.dp),
            )
        }
    }
}
