package com.dao0203.gikucampv20.android.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun Modifier.rotateWithAnimation(
    degrees: Float,
): Modifier {
    val animatedRotation by animateFloatAsState(
        targetValue = degrees,
        animationSpec =
            tween(
                durationMillis = 300,
                easing = { it },
            ),
    )
    return this.then(
        Modifier.graphicsLayer(
            rotationZ = animatedRotation,
        ),
    )
}
