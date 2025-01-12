package com.dao0203.giku_camp_hackathon_2024v20.android.feature.training.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.max
import kotlin.math.min

@Stable
data class PoseOverlayUiModel(
    val poseLandmarkerResult: PoseLandmarkerResult,
    val imageHeight: Int,
    val imageWidth: Int,
    val runningMode: RunningMode,
) {
    fun getScaleFactor(
        overlayWidth: Int,
        overlayHeight: Int
    ): Float =
        if (runningMode == RunningMode.VIDEO || runningMode == RunningMode.IMAGE) {
            min(
                overlayWidth * 1f / imageWidth,
                overlayHeight * 1f / imageHeight
            )
        } else {
            max(
                overlayWidth * 1f / imageWidth,
                overlayHeight * 1f / imageHeight
            )
        }

}

@Composable
fun PoseOverlay(
    uiModel: PoseOverlayUiModel,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val scaleFactor = uiModel.getScaleFactor(size.width.toInt(), size.height.toInt())
        for (landmark in uiModel.poseLandmarkerResult.landmarks()) {
            for (normalizedLandmark in landmark) {
                drawCircle(
                    center = Offset(
                        normalizedLandmark.x() * uiModel.imageWidth * scaleFactor,
                        normalizedLandmark.y() * uiModel.imageHeight * scaleFactor
                    ),
                    radius = 4f,
                    color = Color(0xFFE57373)
                )
            }
            PoseLandmarker.POSE_LANDMARKS.forEach {
                with(uiModel) {
                    if (poseLandmarkerResult.landmarks().isEmpty()) return@forEach
                    val start = Offset(
                        poseLandmarkerResult.landmarks()[0][it.start()].x() * imageWidth * scaleFactor,
                        poseLandmarkerResult.landmarks()[0][it.start()].y() * imageHeight * scaleFactor
                    )
                    val end = Offset(
                        poseLandmarkerResult.landmarks()[0][it.end()].x() * imageWidth * scaleFactor,
                        poseLandmarkerResult.landmarks()[0][it.end()].y() * imageHeight * scaleFactor
                    )
                    drawLine(
                        start = start,
                        end = end,
                        strokeWidth = 12f,
                        color = Color(0xFF007F8B)
                    )
                }
            }
        }

    }
}
