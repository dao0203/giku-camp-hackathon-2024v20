package com.dao0203.gikucampv20.android.feature.training

import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dao0203.gikucampv20.android.BuildConfig
import com.dao0203.gikucampv20.android.R
import com.dao0203.gikucampv20.android.feature.training.component.CameraPreview
import com.dao0203.gikucampv20.android.feature.training.component.PoseOverlay
import com.dao0203.gikucampv20.android.feature.training.component.TrainingInfoCard
import com.dao0203.gikucampv20.android.ui.theme.MainTheme
import com.dao0203.gikucampv20.android.ui.theme.surfaceLight
import com.dao0203.gikucampv20.android.util.MainPreview
import com.dao0203.gikucampv20.android.util.rotateWithAnimation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrainingWithCameraScreen(
    navigateToRest: () -> Unit,
    navigateToResult: () -> Unit,
) {
    val viewModel = koinViewModel<TrainingWithCameraViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(SensorManager::class.java) }
    val checkComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.primary_color_check))

    DisposableEffect(Unit) {
        val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager?.registerListener(
            viewModel,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL,
        )
        onDispose {
            sensorManager?.unregisterListener(viewModel, accelerometer)
        }
    }

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
        checkComposition = checkComposition,
        onAnalyzeImage = viewModel::detectPose,
        onSwitchCamera = viewModel::switchCamera,
        onPassLine = viewModel::updateReps,
        onClickCard = { },
    )
}

@Composable
private fun TrainingWithCameraContent(
    uiState: TrainingWithCameraUiState,
    checkComposition: LottieComposition?,
    onAnalyzeImage: (image: ImageProxy) -> Unit,
    onSwitchCamera: () -> Unit,
    onPassLine: () -> Unit,
    onClickCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
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
                        .rotateWithAnimation(uiState.rotationDegrees)
                        .padding(WindowInsets.systemBars.asPaddingValues())
                        .padding(20.dp),
            )
            if (uiState.showPreparationTime) {
                Column(
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .rotateWithAnimation(uiState.rotationDegrees),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (uiState.showPreparationTimeUntilAdjusting) {
                        Text(
                            text =
                                stringResource(
                                    R.string.operate_red_bar_align_for_easy_counting,
                                    uiState.preparationTimeUntilAdjusting,
                                ),
                            textAlign = TextAlign.Center,
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    color = surfaceLight,
                                ),
                        )
                    }
                    Text(
                        text = uiState.preparationTimeUntilTraining,
                        style =
                            MaterialTheme.typography.displayLarge.copy(
                                fontSize = 100.sp,
                                color = surfaceLight,
                            ),
                        modifier =
                            Modifier
                                .alpha(0.8f),
                    )
                }
            }
            if (uiState.showGoText) {
                Text(
                    text = stringResource(R.string.go),
                    style =
                        MaterialTheme.typography.displayLarge.copy(
                            fontSize = 100.sp,
                        ),
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .rotateWithAnimation(uiState.rotationDegrees),
                )
            }
            LottieAnimation(
                composition = checkComposition,
                iterations = LottieConstants.IterateForever,
                clipSpec = LottieClipSpec.Progress(0f, 1f),
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .rotate(uiState.rotationDegrees),
            )
        }
    }
}

@MainPreview
@Composable
private fun TrainingWithCameraContentPreview() {
    MainTheme {
        TrainingWithCameraContent(
            uiState =
                TrainingWithCameraUiState(
                    isBackCamera = true,
                    remainingReps = 10,
                    rotationDegrees = 0f,
                    showPreparationTime = true,
                    showPreparationTimeUntilAdjusting = true,
                    preparationTimeUntilAdjusting = "3",
                    preparationTimeUntilTraining = "2",
                    showGoText = false,
                    poseOverlayUiModel = null,
                ),
            checkComposition = null,
            onAnalyzeImage = {},
            onSwitchCamera = {},
            onPassLine = {},
            onClickCard = {},
        )
    }
}
