package com.dao0203.gikucampv20.android.feature.training

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
    val preparationTimeUntilAdjusting: String,
    val showPreparationTime: Boolean = preparationTimeUntilTraining.toInt() > 0,
    val rotationDegrees: Float,
    val showPreparationTimeUntilAdjusting: Boolean,
    val showGoText: Boolean,
    val showFinishCheck: Boolean,
)

sealed interface TrainingWithCameraEffect {
    data object NavigateToRest : TrainingWithCameraEffect

    data object NavigateToResult : TrainingWithCameraEffect
}

private data class TrainingWithCameraViewModelState(
    val poseOverlayUiModel: PoseOverlayUiModel? = null,
    val isBackCamera: Boolean = false,
    val isLiftedAboveMidpointAll: Boolean = false,
    val preparationTimeUntilTraining: Int = 15,
    val preparationTimeUntilAdjusting: Int = 7,
    val screenOrientation: ScreenOrientation = ScreenOrientation.PORTRAIT,
    val showPreparationTimeUntilAdjusting: Boolean = true,
    val showGoText: Boolean = false,
    val showFinishCheck: Boolean = false,
)

enum class ScreenOrientation(val degrees: Float) {
    PORTRAIT(0f),
    LANDSCAPE_LEFT(90f),
    LANDSCAPE_RIGHT(-90f),
    ;

    fun isLandScapeRight() = this == LANDSCAPE_RIGHT
}

class TrainingWithCameraViewModel :
    ViewModel(),
    KoinComponent,
    PoseLandmarkerHelper.LandmarkerListener,
    SensorEventListener {
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
        decreasePreparationTimeUntilAdjusting()
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
            cancel()
        }
    }

    private fun decreasePreparationTimeUntilAdjusting() {
        viewModelScope.launch {
            while (true) {
                delay(1_000)
                if (vmState.value.preparationTimeUntilAdjusting == 0) {
                    vmState.update { it.copy(showPreparationTimeUntilAdjusting = false) }
                    cancel()
                    break
                }
                val currentState = vmState.value.preparationTimeUntilAdjusting
                vmState.update { it.copy(preparationTimeUntilAdjusting = currentState - 1) }
            }
        }
    }

    private fun collectReps() {
        viewModelScope.launch {
            onGoingTrainingMenuRepository.onGoingTrainingMenu
                .distinctUntilChanged()
                .collect {
                    if (it.reps != 0) return@collect
                    if (it.sets == 1) {
                        vmState.update { vm -> vm.copy(showFinishCheck = true) }
                        delay(1_500) // for showing finish check
                        _effect.emit(TrainingWithCameraEffect.NavigateToResult)
                        onGoingTrainingMenuRepository.addTrainingHistory()
                        onGoingTrainingMenuRepository.updateOnGoingTrainingMenu(TrainingMenu.default())
                        onGoingTrainingMenuRepository.updatePlannedTrainingMenu(TrainingMenu.default())
                        cancel()
                    }
                    vmState.update { vm -> vm.copy(showFinishCheck = true) }
                    delay(1_500) // for showing finish check
                    _effect.emit(TrainingWithCameraEffect.NavigateToRest)
                    onGoingTrainingMenuRepository.addWorkoutSetDefault()
                    onGoingTrainingMenuRepository.resetReps()
                    vmState.update { TrainingWithCameraViewModelState() }
                    cancel()
                }
        }
    }

    private fun collectCoordination() {
        viewModelScope.launch {
            vmState.collect { collect ->
                if (collect.preparationTimeUntilTraining != 0) return@collect
                if (uiState.value.remainingReps == 0) return@collect
                if (collect.poseOverlayUiModel?.poseLandmarksIndexesForAdjusting?.isNotEmpty() == true) {
                    if (collect.poseOverlayUiModel.poseLandmarkerResult.landmarks()
                            ?.isNotEmpty() != true
                    ) {
                        return@collect
                    }
                    val landmark = collect.poseOverlayUiModel.poseLandmarkerResult.landmarks()[0]
                    val midPoint = collect.poseOverlayUiModel.trainingMidPointLines?.midpoint
                    val isLiftedAboveMidpoint =
                        collect.poseOverlayUiModel.poseLandmarksIndexesForAdjusting.map {
                            val landmarkOffset = it
                            val orientation = collect.screenOrientation
                            if (collect.poseOverlayUiModel.trainingMidPointLines != null) {
                                val isLiftedAboveLine =
                                    if (collect
                                            .poseOverlayUiModel
                                            .trainingMidPointLines
                                            .direction == LineDirection.HORIZONTAL
                                    ) {
                                        Pair(
                                            landmark[landmarkOffset.start.index].y() < (
                                                midPoint?.y
                                                    ?: 0f
                                            ),
                                            landmark[landmarkOffset.end.index].y() < (
                                                midPoint?.y
                                                    ?: 0f
                                            ),
                                        )
                                    } else {
                                        val isOrientationLandscapeRight =
                                            orientation.isLandScapeRight()
                                        val isStartLefted =
                                            if (isOrientationLandscapeRight) {
                                                landmark[landmarkOffset.start.index].x() < (
                                                    midPoint?.x
                                                        ?: 0f
                                                )
                                            } else {
                                                landmark[landmarkOffset.start.index].x() > (
                                                    midPoint?.x
                                                        ?: 0f
                                                )
                                            }
                                        val isEndLifted =
                                            if (isOrientationLandscapeRight) {
                                                landmark[landmarkOffset.end.index].x() < (
                                                    midPoint?.x
                                                        ?: 0f
                                                )
                                            } else {
                                                landmark[landmarkOffset.end.index].x() > (
                                                    midPoint?.x
                                                        ?: 0f
                                                )
                                            }
                                        Pair(isStartLefted, isEndLifted)
                                    }
                                isLiftedAboveLine
                            } else {
                                Pair(false, false)
                            }
                        }
                    // 片方でも上に上がっていたら全部下げない限り，次はカウントしない
                    val isLiftedAboveLine = isLiftedAboveMidpoint.first()

                    if (isLiftedAboveLine.first) {
                        // 一回，上に上がっていたら，falseにならない限り，次はカウントしない
                        if (collect.isLiftedAboveMidpointAll) return@collect
                        // 両方上に上がっていたら，全て上に上がっていると判定
                        if (isLiftedAboveLine.second) {
                            vmState.update { it.copy(isLiftedAboveMidpointAll = true) }
                            updateReps()
                        }
                    } else {
                        // 一回，下に下がっていたら，trueにならない限り，次はカウントしない
                        if (!collect.isLiftedAboveMidpointAll) return@collect
                        // 両方下に下がっていたら，全て下に下がっていると判定
                        if (!isLiftedAboveLine.second) {
                            vmState.update { it.copy(isLiftedAboveMidpointAll = false) }
                        }
                    }
                }
            }
        }
    }

    private fun defineLandmarkIndexesForTraining() {
        viewModelScope.launch {
            vmState.collect { collect ->
                if (collect.preparationTimeUntilTraining == 7) {
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
            if (poseLandmarkerResult.landmarks().isEmpty()) {
                return MidpointLine(
                    midpoint = Coordination(0f, 0f),
                    direction = LineDirection.HORIZONTAL,
                )
            }
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
                            isLiftedAboveLine = it.isLiftedAboveMidpointAll,
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
                isLiftedAboveLine = vmState.isLiftedAboveMidpointAll,
            ),
        isBackCamera = vmState.isBackCamera,
        remainingReps = onGoingTrainingMenu.reps,
        preparationTimeUntilTraining = vmState.preparationTimeUntilTraining.toString(),
        preparationTimeUntilAdjusting = vmState.preparationTimeUntilAdjusting.toString(),
        showPreparationTimeUntilAdjusting = vmState.showPreparationTimeUntilAdjusting,
        showGoText = vmState.showGoText,
        rotationDegrees = vmState.screenOrientation.degrees,
        showFinishCheck = vmState.showFinishCheck,
    )

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]

                val orientation =
                    when {
                        abs(y) > abs(x) -> ScreenOrientation.PORTRAIT
                        x > 0 -> ScreenOrientation.LANDSCAPE_LEFT
                        else -> ScreenOrientation.LANDSCAPE_RIGHT
                    }
                vmState.update { it.copy(screenOrientation = orientation) }
                println("orientation: $orientation")
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accurancy: Int) {
        // no-op
    }
}
