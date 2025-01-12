package com.dao0203.giku_camp_hackathon_2024v20.android.feature.training.component

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun CameraPreview(
    onAnalyzeImage: (imageProxy: ImageProxy) -> Unit,
    isBackCamera: Boolean,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    DisposableEffect(isBackCamera) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraProvider = cameraProviderFuture.get()
        val executor = Executors.newSingleThreadExecutor()

        val preview = createPreview(previewView.surfaceProvider)

        val cameraSelector = if (isBackCamera) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }
        val imageAnalysis = createImageAnalysis(executor, onAnalyzeImage)
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            cameraProvider.unbindAll()
            executor.shutdown()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier,
    )
}

private fun createPreview(
    surfaceProvider: SurfaceProvider
): Preview {
    val preview = Preview
        .Builder()
        .build()
        .also {
            it.surfaceProvider = surfaceProvider
        }
    return preview
}

private fun createImageAnalysis(
    executor: Executor,
    onAnalyzeImage: (imageProxy: ImageProxy) -> Unit
): ImageAnalysis {
    val imageAnalysis = ImageAnalysis
        .Builder()
        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
        .build()
        .also {
            it.setAnalyzer(executor) { imageProxy ->
                // Analyze the image
                onAnalyzeImage(imageProxy)
                imageProxy.close()
            }
        }
    return imageAnalysis
}
