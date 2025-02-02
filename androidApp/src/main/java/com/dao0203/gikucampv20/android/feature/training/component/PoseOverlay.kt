package com.dao0203.gikucampv20.android.feature.training.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.dao0203.gikucampv20.domain.PoseLandmarksIndex
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.max
import kotlin.math.min

@Stable
data class PoseOverlayUiModel(
    val poseLandmarkerResult: PoseLandmarkerResult,
    val trainingMidPointLines: MidpointLine?,
    val poseLandmarksIndexesForAdjusting: List<PoseLandmarksIndex>,
    val showLandmarkIndexesForAdjusting: Boolean,
    val isLiftedAboveLine: Boolean,
    val imageHeight: Int,
    val imageWidth: Int,
    val runningMode: RunningMode,
) {
    fun getScaleFactor(
        overlayWidth: Int,
        overlayHeight: Int,
    ): Float =
        if (runningMode == RunningMode.VIDEO || runningMode == RunningMode.IMAGE) {
            min(
                overlayWidth * 1f / imageWidth,
                overlayHeight * 1f / imageHeight,
            )
        } else {
            max(
                overlayWidth * 1f / imageWidth,
                overlayHeight * 1f / imageHeight,
            )
        }
}

@Stable
data class MidpointLine(
    val midpoint: Coordination,
    val direction: LineDirection,
)

@Stable
data class Coordination(
    val x: Float,
    val y: Float,
)

@Stable
enum class LineDirection {
    HORIZONTAL,
    VERTICAL,
}

@Composable
fun PoseOverlay(
    uiModel: PoseOverlayUiModel,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        val scaleFactor = uiModel.getScaleFactor(size.width.toInt(), size.height.toInt())
        for (landmark in uiModel.poseLandmarkerResult.landmarks()) {
            for (normalizedLandmark in landmark) {
                drawCircle(
                    center =
                        Offset(
                            normalizedLandmark.x() * uiModel.imageWidth * scaleFactor,
                            normalizedLandmark.y() * uiModel.imageHeight * scaleFactor,
                        ),
                    radius = 4f,
                    color = Color(0xFFE57373),
                )
            }
            PoseLandmarker.POSE_LANDMARKS.forEach {
                with(uiModel) {
                    if (poseLandmarkerResult.landmarks().isEmpty()) return@forEach
                    val start =
                        Offset(
                            poseLandmarkerResult.landmarks()[0][it.start()].x() * imageWidth * scaleFactor,
                            poseLandmarkerResult.landmarks()[0][it.start()].y() * imageHeight * scaleFactor,
                        )
                    val end =
                        Offset(
                            poseLandmarkerResult.landmarks()[0][it.end()].x() * imageWidth * scaleFactor,
                            poseLandmarkerResult.landmarks()[0][it.end()].y() * imageHeight * scaleFactor,
                        )
                    drawLine(
                        start = start,
                        end = end,
                        strokeWidth = 12f,
                        color = Color(0xFF007F8B),
                    )
                }
            }
            if (uiModel.showLandmarkIndexesForAdjusting) {
                uiModel.poseLandmarksIndexesForAdjusting.forEach {
                    val start =
                        Offset(
                            uiModel.poseLandmarkerResult.landmarks()[0][it.start.index].x() * uiModel.imageWidth * scaleFactor,
                            uiModel.poseLandmarkerResult.landmarks()[0][it.start.index].y() * uiModel.imageHeight * scaleFactor,
                        )
                    val end =
                        Offset(
                            uiModel.poseLandmarkerResult.landmarks()[0][it.end.index].x() * uiModel.imageWidth * scaleFactor,
                            uiModel.poseLandmarkerResult.landmarks()[0][it.end.index].y() * uiModel.imageHeight * scaleFactor,
                        )
                    drawLine(
                        start = start,
                        end = end,
                        strokeWidth = 12f,
                        color = Color(0xFFE57373),
                    )
                }
            }
            uiModel.trainingMidPointLines?.let {
                val start: Offset
                val end: Offset
                when (it.direction) {
                    LineDirection.HORIZONTAL -> {
                        start =
                            Offset(
                                0f,
                                it.midpoint.y * uiModel.imageHeight * scaleFactor,
                            )
                        end =
                            Offset(
                                size.width,
                                it.midpoint.y * uiModel.imageHeight * scaleFactor,
                            )
                    }
                    LineDirection.VERTICAL -> {
                        start =
                            Offset(
                                it.midpoint.x * uiModel.imageWidth * scaleFactor,
                                0f,
                            )
                        end =
                            Offset(
                                it.midpoint.x * uiModel.imageWidth * scaleFactor,
                                size.height,
                            )
                    }
                }
                drawLine(
                    start = start,
                    end = end,
                    strokeWidth = 12f,
                    color =
                        if (uiModel.isLiftedAboveLine) {
                            Color(0xFF4CAF50)
                        } else {
                            Color(0xFFE57373)
                        },
                )
            }
        }
    }
}
