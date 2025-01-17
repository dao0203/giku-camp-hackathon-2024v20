package com.dao0203.gikucampv20.android.feature.training.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

@Composable
fun CircularTimer(
    progress: Float,
    progressColor: Color,
    size: Dp,
    backGroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier.size(size),
    ) {
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = -360 * progress,
            useCenter = true,
            style =
                Stroke(
                    width = 40f,
                    cap = StrokeCap.Round,
                ),
        )
        drawCircle(
            color = backGroundColor,
        )
    }
}
