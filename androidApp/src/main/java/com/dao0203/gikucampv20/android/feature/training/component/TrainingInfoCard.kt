package com.dao0203.gikucampv20.android.feature.training.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dao0203.gikucampv20.android.ui.theme.MainTheme
import com.dao0203.gikucampv20.android.util.DrawableRes
import com.dao0203.gikucampv20.android.util.MainPreview
import com.dao0203.gikucampv20.android.util.StringRes

@Composable
fun TrainingInfoCard(
    remainingReps: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .size(
                width = 120.dp,
                height = 120.dp,
            ),
        elevation = CardDefaults.elevatedCardElevation(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            Text(
                text = stringResource(StringRes.remaining),
                style = MaterialTheme.typography.bodyMedium,
            )
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.BottomEnd)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Bottom),
                ) {
                    val baseModifier = Modifier.alignByBaseline()
                    Text(
                        text = remainingReps.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        modifier = baseModifier
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = stringResource(StringRes.reps),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = baseModifier
                    )
                }
                Icon(painterResource(DrawableRes.more_horiz), contentDescription = null)
            }
        }
    }
}

@MainPreview
@Composable
private fun TrainingInfoCardPreview() {
    MainTheme {
        TrainingInfoCard(
            remainingReps = 10,
            onClick = {},
        )
    }
}
