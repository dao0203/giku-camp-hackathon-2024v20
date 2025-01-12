package com.dao0203.giku_camp_hackathon_2024v20.android.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult

class PoseLandmarkerHelper(
    private val context: Context,
    private val landmarkerListener: LandmarkerListener? = null
) {
    private var poseLandmarker: PoseLandmarker? = null
    fun clear() {
        poseLandmarker?.close()
    }

    fun setup() {
        val baseOptions = BaseOptions.builder()
            .setDelegate(Delegate.CPU)
            .setModelAssetPath("task/pose_landmark_full.task")
            .build()

        val optionsBuilder = PoseLandmarker.PoseLandmarkerOptions.builder()
            .setBaseOptions(baseOptions)
            .setResultListener(this::returnLivestreamResult)
            .build()

        poseLandmarker = PoseLandmarker.createFromOptions(context, optionsBuilder)
    }

    fun detectLiveStream(
        imageProxy: ImageProxy,
    ) {
        val bitmapBuffer = Bitmap.createBitmap(
            imageProxy.width,
            imageProxy.height,
            Bitmap.Config.ARGB_8888
        )

        imageProxy.use { image ->
            bitmapBuffer.copyPixelsFromBuffer(image.planes[0].buffer)
        }
        imageProxy.close()

        val matrix = Matrix()
            .apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
            }

        val rotatedBitmap = Bitmap.createBitmap(
            bitmapBuffer,
            0,
            0,
            bitmapBuffer.width,
            bitmapBuffer.height,
            matrix,
            true
        )

        val mpImage = BitmapImageBuilder(rotatedBitmap).build()
        val frameTime = imageProxy.imageInfo.timestamp
        poseLandmarker?.detectAsync(mpImage, frameTime)
    }

    private fun returnLivestreamResult(
        result: PoseLandmarkerResult,
        input: MPImage
    ) {
        val finishTimeMs = SystemClock.uptimeMillis()
        val inferenceTime = finishTimeMs - result.timestampMs()

        landmarkerListener?.onResult(
            ResultBundle(
                listOf(result),
                inferenceTime,
                input.height,
                input.width
            )
        )
    }

    data class ResultBundle(
        val results: List<PoseLandmarkerResult>,
        val inferenceTime: Long,
        val inputImageHeight: Int,
        val inputImageWidth: Int,
    )

    interface LandmarkerListener {
        fun onResult(resultBundle: ResultBundle)
    }
}
