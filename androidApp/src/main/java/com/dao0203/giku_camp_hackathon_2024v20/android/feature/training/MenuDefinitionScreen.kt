package com.dao0203.giku_camp_hackathon_2024v20.android.feature.training

import androidx.compose.runtime.Composable
import com.dao0203.giku_camp_hackathon_2024v20.feature.training.DefinitionMenuViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DefinitionMenuScreen(
    onStartTraining: () -> Unit,
) {
    val viewModel = koinViewModel<DefinitionMenuViewModel>()
}
