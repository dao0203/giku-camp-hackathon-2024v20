package com.dao0203.gikucampv20.android.feature.training

import android.content.Context
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dao0203.gikucampv20.android.feature.training.component.PoseOverlayUiModel
import com.dao0203.gikucampv20.android.util.PoseLandmarkerHelper
import com.dao0203.gikucampv20.domain.TrainingMenu
import com.dao0203.gikucampv20.domain.default
import com.dao0203.gikucampv20.repository.OnGoingTrainingMenuRepository
import com.google.mediapipe.tasks.vision.core.RunningMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Stable
data class TrainingWithCameraUiState(
    val poseOverlayUiModel: PoseOverlayUiModel?,
    val isBackCamera: Boolean,
    val remainingReps: Int,
    val preparationTime: Int,
)

sealed interface TrainingWithCameraEffect {
    data object NavigateToRest : TrainingWithCameraEffect

    data object NavigateToResult : TrainingWithCameraEffect
}

private data class TrainingWithCameraViewModelState(
    val poseOverlayUiModel: PoseOverlayUiModel? = null,
    val isBackCamera: Boolean = true,
    val preparationTime: Int = 10,
)

class TrainingWithCameraViewModel :
    ViewModel(),
    KoinComponent,
    PoseLandmarkerHelper.LandmarkerListener {
    private val onGoingTrainingMenuRepository by inject<OnGoingTrainingMenuRepository>()
    private val context: Context by inject()

    private val vmState = MutableStateFlow(TrainingWithCameraViewModelState())

    val uiState =
        combine(
            vmState,
            onGoingTrainingMenuRepository.onGoingTrainingMenu,
        ) { vmState, onGoingTrainingMenu ->
            createUiState(vmState, onGoingTrainingMenu)
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            createUiState(vmState.value, TrainingMenu.default()),
        )

    private val _effect = MutableSharedFlow<TrainingWithCameraEffect>()
    val effect = _effect.asSharedFlow()

    private val poseRandmarkerHelper =
        PoseLandmarkerHelper(
            context = context,
            runningMode = RunningMode.LIVE_STREAM,
            landmarkerListener = this,
        )

    fun initialize() {
        poseRandmarkerHelper.setup()
        viewModelScope.launch {
            onGoingTrainingMenuRepository.onGoingTrainingMenu.collect {
                if (it.reps == 0) {
                    _effect.emit(TrainingWithCameraEffect.NavigateToRest)
                    onGoingTrainingMenuRepository.resetReps()
                    onGoingTrainingMenuRepository.decreaseSets()
                }
            }
        }
    }

    fun updateReps() {
        onGoingTrainingMenuRepository.decreaseReps()
    }

    fun detectPose(imageProxy: ImageProxy) {
        poseRandmarkerHelper.detectLiveStream(imageProxy)
    }

    fun switchCamera() {
        vmState.update { it.copy(isBackCamera = !it.isBackCamera) }
    }

    override fun onResult(resultBundle: PoseLandmarkerHelper.ResultBundle) {
        vmState.update {
            it.copy(
                poseOverlayUiModel =
                    PoseOverlayUiModel(
                        poseLandmarkerResult = resultBundle.results.first(),
                        imageWidth = resultBundle.inputImageWidth,
                        imageHeight = resultBundle.inputImageHeight,
                        runningMode = RunningMode.LIVE_STREAM,
                    ),
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        poseRandmarkerHelper.clear()
    }

    private fun createUiState(
        vmState: TrainingWithCameraViewModelState,
        onGoingTrainingMenu: TrainingMenu,
    ) = TrainingWithCameraUiState(
        poseOverlayUiModel = vmState.poseOverlayUiModel,
        isBackCamera = vmState.isBackCamera,
        remainingReps = onGoingTrainingMenu.reps,
        preparationTime = vmState.preparationTime,
    )
}
