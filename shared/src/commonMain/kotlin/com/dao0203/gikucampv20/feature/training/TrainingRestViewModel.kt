package com.dao0203.gikucampv20.feature.training

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dao0203.gikucampv20.repository.OnGoingTrainingMenuRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Stable
data class TrainingRestUiState(
    val remainingRestTime: String = "",
    val enableStartButton: Boolean = false,
    val showTimeText: Boolean = true,
    val progress: Float = 0f,
    val remainingSets: Int = 0,
    val showSkipAlertDialog: Boolean = false,
)

data class TrainingRestViewModelState(
    val remainingRestTime: Int = 0,
    val showTimeText: Boolean = true,
    val showSkipAlertDialog: Boolean = false,
)

class TrainingRestViewModel :
    ViewModel(),
    KoinComponent {
    private val onGoingTrainingMenuRepository by inject<OnGoingTrainingMenuRepository>()

    private val vmState = MutableStateFlow(TrainingRestViewModelState())
    val uiState =
        combine(
            vmState,
            onGoingTrainingMenuRepository.onGoingTrainingMenu,
        ) { vmState, onGoingTrainingMenu ->
            TrainingRestUiState(
                remainingRestTime = vmState.remainingRestTime.toString(),
                showTimeText = vmState.showTimeText,
                progress = vmState.remainingRestTime.toFloat() / onGoingTrainingMenu.rest,
                enableStartButton = vmState.remainingRestTime == 0,
                showSkipAlertDialog = vmState.showSkipAlertDialog,
                remainingSets = onGoingTrainingMenu.sets,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            TrainingRestUiState(),
        )

    fun initialize() {
        operationTime()
    }

    private fun operationTime() {
        viewModelScope.launch {
            val plannedTrainingMenu = onGoingTrainingMenuRepository.plannedTrainingMenu.first()

            vmState.update { it.copy(remainingRestTime = plannedTrainingMenu.rest) }

            while (true) {
                if (vmState.value.remainingRestTime == 0) break
                delay(1_000)
                vmState.update { it.copy(remainingRestTime = it.remainingRestTime - 1) }
            }

            while (true) {
                vmState.update { it.copy(showTimeText = !it.showTimeText) }
                delay(500)
            }
        }
    }

    fun changeShowSkipAlertDialog() {
        vmState.update { it.copy(showSkipAlertDialog = !it.showSkipAlertDialog) }
    }
}
