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
    val onGoingRestTime: String = "",
    val showTimeText: Boolean = true,
    val remainingRestTime: Int = 0,
    val remainingSets: Int = 0,
)

data class TrainingRestViewModelState(
    val onGoingRestTime: Int = 0,
    val showTimeText: Boolean = true,
    val remainingRestTime: Int = 0,
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
                onGoingRestTime = vmState.onGoingRestTime.toString(),
                showTimeText = vmState.showTimeText,
                remainingRestTime = vmState.remainingRestTime,
                remainingSets = onGoingTrainingMenu.sets,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            TrainingRestUiState(),
        )

    fun initialize() {
        viewModelScope.launch {
            val plannedTrainingMenu = onGoingTrainingMenuRepository.plannedTrainingMenu.first()

            vmState.update {
                it.copy(
                    onGoingRestTime = plannedTrainingMenu.rest,
                    remainingRestTime = plannedTrainingMenu.rest,
                )
            }

            while (true) {
                if (vmState.value.onGoingRestTime == 0) break
                vmState.update {
                    it.copy(
                        onGoingRestTime = it.onGoingRestTime - 1,
                    )
                }
                delay(1_000)
            }

            while (true) {
                vmState.update {
                    it.copy(
                        showTimeText = !it.showTimeText,
                    )
                }
                delay(500)
            }
        }
    }
}
