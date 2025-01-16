package com.dao0203.gikucampv20.android.feature.training

import android.content.Context
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dao0203.gikucampv20.android.feature.training.component.Coordination
import com.dao0203.gikucampv20.android.feature.training.component.LineDirection
import com.dao0203.gikucampv20.android.feature.training.component.MidpointLine
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
import kotlin.math.abs

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
    val isLiftedAboveMidpoint: Boolean = false,
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
        collectCoordination()
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

    private fun collectCoordination() {
        viewModelScope.launch {
            vmState.collect { collect ->
                if (collect.poseOverlayUiModel?.poseLandmarksIndexesForAdjusting?.isNotEmpty() == true) {
                    if (collect.poseOverlayUiModel.poseLandmarkerResult.landmarks()?.isNotEmpty() != true) return@collect
                    val landmark = collect.poseOverlayUiModel.poseLandmarkerResult.landmarks()[0]
                    val midPoint = collect.poseOverlayUiModel.trainingMidPointLines?.midpoint
                    val isLiftedAboveMidpoint =
                        collect.poseOverlayUiModel.trainingMidPointLines?.let {
                            val landMarkOffset = collect.poseOverlayUiModel.poseLandmarksIndexesForAdjusting.first()
                            if (it.direction == LineDirection.HORIZONTAL) {
                                landmark[landMarkOffset.start.index].y() < (midPoint?.y ?: 0f) &&
                                    landmark[landMarkOffset.end.index].y() < (midPoint?.y ?: 0f)
                            } else {
                                landmark[landMarkOffset.start.index].x() < (midPoint?.x ?: 0f) &&
                                    landmark[landMarkOffset.end.index].x() < (midPoint?.x ?: 0f)
                            }
                        } ?: false
                    vmState.update { it.copy(isLiftedAboveMidpoint = isLiftedAboveMidpoint) }
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
                    trainingMidPointLines =
                        this.poseOverlayUiModel
                            .poseLandmarksIndexesForAdjusting.mapToLandmarkIndex(
                                this.poseOverlayUiModel.poseLandmarkerResult,
                            ),
                    showLandmarkIndexesForAdjusting = false,
                ),
        )
    }

    private fun List<PoseLandmarksIndex>.mapToLandmarkIndex(poseLandmarkerResult: PoseLandmarkerResult): MidpointLine {
        return this.first().let {
            val landmark = poseLandmarkerResult.landmarks()[0]
            val startX = landmark[it.start.index].x()
            val startY = landmark[it.start.index].y()
            val endX = landmark[it.end.index].x()
            val endY = landmark[it.end.index].y()
            val midX = (startX + endX) / 2
            val midY = (startY + endY) / 2
            val dx = endX - startX
            val dy = endY - startY
            MidpointLine(
                midpoint = Coordination(midX, midY),
                direction = if (abs(dx) > abs(dy)) LineDirection.HORIZONTAL else LineDirection.VERTICAL,
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
                            trainingMidPointLines =
                                it.poseOverlayUiModel?.trainingMidPointLines,
                            poseLandmarksIndexesForAdjusting =
                                adjusting ?: emptyList(),
                            showLandmarkIndexesForAdjusting =
                                it.poseOverlayUiModel?.showLandmarkIndexesForAdjusting
                                    ?: true,
                            isLiftedAboveLine = it.isLiftedAboveMidpoint,
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
                trainingMidPointLines = vmState.poseOverlayUiModel.trainingMidPointLines,
                showLandmarkIndexesForAdjusting = vmState.poseOverlayUiModel.showLandmarkIndexesForAdjusting,
                isLiftedAboveLine = vmState.isLiftedAboveMidpoint,
            ),
        isBackCamera = vmState.isBackCamera,
        remainingReps = onGoingTrainingMenu.reps,
        preparationTimeUntilTraining = vmState.preparationTimeUntilTraining.toString(),
        showGoText = vmState.showGoText,
    )
}
