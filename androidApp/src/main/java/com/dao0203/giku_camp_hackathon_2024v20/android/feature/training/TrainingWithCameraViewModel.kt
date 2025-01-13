package com.dao0203.giku_camp_hackathon_2024v20.android.feature.training

import android.content.Context
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dao0203.giku_camp_hackathon_2024v20.android.feature.training.component.PoseOverlayUiModel
import com.dao0203.giku_camp_hackathon_2024v20.android.util.PoseLandmarkerHelper
import com.dao0203.giku_camp_hackathon_2024v20.repository.OnGoingTrainingMenuRepository
import com.google.mediapipe.tasks.vision.core.RunningMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Stable
data class TrainingWithCameraUiState(
    val poseOverlayUiModel: PoseOverlayUiModel? = null,
    val isBackCamera: Boolean = true,
    val remainingReps: Int = 0,
)

class TrainingWithCameraViewModel : ViewModel(), KoinComponent,
    PoseLandmarkerHelper.LandmarkerListener {
    private val onGoingTrainingMenuRepository by inject<OnGoingTrainingMenuRepository>()
    private val context: Context by inject()

    private val _uiState = MutableStateFlow(TrainingWithCameraUiState())
    val uiState = _uiState.asStateFlow()

    private val poseRandmarkerHelper = PoseLandmarkerHelper(
        context = context,
        runningMode = RunningMode.LIVE_STREAM,
        landmarkerListener = this
    )

    fun initialize() {
        poseRandmarkerHelper.setup()
        viewModelScope.launch {
            onGoingTrainingMenuRepository.onGoingTrainingMenu.collect {
                _uiState.update { it.copy(remainingReps = it.remainingReps) }
            }
        }
    }

    fun updateReps(reps: Int) {
        onGoingTrainingMenuRepository.updateSets(reps)
    }

    fun detectPose(imageProxy: ImageProxy) {
        poseRandmarkerHelper.detectLiveStream(imageProxy)
    }

    fun switchCamera() {
        _uiState.update { it.copy(isBackCamera = !it.isBackCamera) }
    }

    override fun onResult(resultBundle: PoseLandmarkerHelper.ResultBundle) {
        _uiState.value = TrainingWithCameraUiState(
            poseOverlayUiModel = PoseOverlayUiModel(
                poseLandmarkerResult = resultBundle.results.first(),
                imageWidth = resultBundle.inputImageWidth,
                imageHeight = resultBundle.inputImageHeight,
                runningMode = RunningMode.LIVE_STREAM,
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        poseRandmarkerHelper.clear()
    }
}
