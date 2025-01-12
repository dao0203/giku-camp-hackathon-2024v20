package com.dao0203.giku_camp_hackathon_2024v20.android.feature.training

import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dao0203.giku_camp_hackathon_2024v20.android.feature.training.component.CameraPreview
import com.dao0203.giku_camp_hackathon_2024v20.android.feature.training.component.PoseOverlay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrainingWithCameraScreen() {
    val viewModel = koinViewModel<TrainingWithCameraViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }
    TrainingWithCameraContent(
        uiState = uiState,
        onAnalyzeImage = viewModel::detectPose,
        onSwitchCamera = viewModel::switchCamera
    )
}

@Composable
private fun TrainingWithCameraContent(
    uiState: TrainingWithCameraUiState,
    onAnalyzeImage: (image: ImageProxy) -> Unit,
    onSwitchCamera: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSwitchCamera
            ) {
                // TODO: change icon
                Icon(Icons.Default.Star, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            Box(

            ) {
                CameraPreview(
                    onAnalyzeImage = onAnalyzeImage,
                    isBackCamera = uiState.isBackCamera
                )
                uiState.poseOverlayUiModel?.let { poseOverlayUiModel ->
                    PoseOverlay(
                        uiModel = poseOverlayUiModel
                    )
                }
            }

        }
    }
}
