package com.dao0203.gikucampv20.android.feature.training

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dao0203.gikucampv20.feature.training.TrainingResultViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrainingResultScreen(navigateToMenuDefinition: () -> Unit) {
    val viewModel = koinViewModel<TrainingResultViewModel>()
    TrainingResultContent()
}

@Composable
private fun TrainingResultContent(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.padding(it),
        ) {
        }
    }
}
