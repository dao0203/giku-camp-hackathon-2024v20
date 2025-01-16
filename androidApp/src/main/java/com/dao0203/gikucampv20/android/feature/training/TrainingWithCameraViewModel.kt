package com.dao0203.gikucampv20.android.feature.training

import android.content.Context
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dao0203.gikucampv20.android.feature.training.component.Coordination
import com.dao0203.gikucampv20.android.feature.training.component.LineSegment
import com.dao0203.gikucampv20.android.feature.training.component.PoseOverlayUiModel
import com.dao0203.gikucampv20.android.util.PoseLandmarkerHelper
import com.dao0203.gikucampv20.domain.PoseLandmarksIndex
import com.dao0203.gikucampv20.domain.TrainingMenu
import com.dao0203.gikucampv20.domain.default
import com.dao0203.gikucampv20.repository.OnGoingTrainingMenuRepository
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
    val preparationTimeUntilTraining: String,
    val showPreparationTime: Boolean = preparationTimeUntilTraining.toInt() > 0,
    val showGoText: Boolean,
)

sealed interface TrainingWithCameraEffect {
    data object NavigateToRest : TrainingWithCameraEffect

    data object NavigateToResult : TrainingWithCameraEffect
}

private data class TrainingWithCameraViewModelState(
    val poseOverlayUiModel: PoseOverlayUiModel? = null,
    val isBackCamera: Boolean = true,
    val preparationTimeUntilTraining: Int = 10,
    val showGoText: Boolean = false,
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
            createUiState(
                vmState,
                onGoingTrainingMenu,
            )
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
        decreasePreparationTime()
        collectReps()
        defineLandmarkIndexesForTraining()
    }

    private fun decreasePreparationTime() {
        viewModelScope.launch {
            while (true) {
                delay(1_000)
                val currentState = vmState.value.preparationTimeUntilTraining
                vmState.update { it.copy(preparationTimeUntilTraining = currentState - 1) }
                if (vmState.value.preparationTimeUntilTraining == 0) {
                    break
                }
            }
            vmState.update { it.copy(showGoText = true) }
            delay(1_000)
            vmState.update { it.copy(showGoText = false) }
        }
    }

    private fun collectReps() {
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

    private fun defineLandmarkIndexesForTraining() {
        viewModelScope.launch {
            vmState.collect { collect ->
                if (collect.preparationTimeUntilTraining == 0) {
                    vmState.update { it.copyOnPreparationTimeUntilTrainingZero() }
                    cancel()
                }
            }
        }
    }

    private fun TrainingWithCameraViewModelState.copyOnPreparationTimeUntilTrainingZero(): TrainingWithCameraViewModelState {
        return this.copy(
            poseOverlayUiModel =
                this.poseOverlayUiModel?.copy(
                    trainingLineSegments =
                        this.poseOverlayUiModel
                            .poseLandmarksIndexesForAdjusting.mapToLandmarkIndex(
                                this.poseOverlayUiModel.poseLandmarkerResult,
                            ),
                    showLandmarkIndexesForAdjusting = false,
                ),
        )
    }

    private fun List<PoseLandmarksIndex>.mapToLandmarkIndex(poseLandmarkerResult: PoseLandmarkerResult): List<LineSegment> {
        return this.map {
            val landmark = poseLandmarkerResult.landmarks()[0]
            val startX = landmark[it.start.index].x()
            val startY = landmark[it.start.index].y()
            val endX = landmark[it.end.index].x()
            val endY = landmark[it.end.index].y()
            LineSegment(
                start = Coordination(x = startX, y = startY),
                end = Coordination(x = endX, y = endY),
            )
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
        viewModelScope.launch {
            val adjusting =
                if (vmState.value.poseOverlayUiModel == null) {
                    onGoingTrainingMenuRepository.onGoingTrainingMenu.first().type?.targetPoseLandmarksIndices
                } else {
                    vmState.value.poseOverlayUiModel?.poseLandmarksIndexesForAdjusting
                }
            vmState.update {
                it.copy(
                    poseOverlayUiModel =
                        PoseOverlayUiModel(
                            poseLandmarkerResult = resultBundle.results.first(),
                            imageWidth = resultBundle.inputImageWidth,
                            imageHeight = resultBundle.inputImageHeight,
                            runningMode = RunningMode.LIVE_STREAM,
                            trainingLineSegments =
                                it.poseOverlayUiModel?.trainingLineSegments
                                    ?: emptyList(),
                            poseLandmarksIndexesForAdjusting =
                                adjusting ?: emptyList(),
                            showLandmarkIndexesForAdjusting =
                                it.poseOverlayUiModel?.showLandmarkIndexesForAdjusting
                                    ?: true,
                        ),
                )
            }
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
        poseOverlayUiModel =
            vmState.poseOverlayUiModel?.copy(
                poseLandmarksIndexesForAdjusting =
                    onGoingTrainingMenu.type?.targetPoseLandmarksIndices
                        ?: emptyList(),
                trainingLineSegments = vmState.poseOverlayUiModel.trainingLineSegments,
                showLandmarkIndexesForAdjusting = vmState.poseOverlayUiModel.showLandmarkIndexesForAdjusting,
            ),
        isBackCamera = vmState.isBackCamera,
        remainingReps = onGoingTrainingMenu.reps,
        preparationTimeUntilTraining = vmState.preparationTimeUntilTraining.toString(),
        showGoText = vmState.showGoText,
    )
}
