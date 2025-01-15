@file:OptIn(ExperimentalUuidApi::class)

package com.dao0203.gikucampv20.feature.training

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dao0203.gikucampv20.domain.MuscleGroup
import com.dao0203.gikucampv20.domain.TrainingMenu
import com.dao0203.gikucampv20.domain.TrainingType
import com.dao0203.gikucampv20.domain.dummies
import com.dao0203.gikucampv20.domain.groupByMuscleGroup
import com.dao0203.gikucampv20.repository.OnGoingTrainingMenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Stable
data class MuscleGroupsUiModel(
    val trainingTypesByGroup: Map<MuscleGroup, List<TrainingType>>,
)

@Stable
data class DefinitionMenuUiState(
    val selectedTrainingType: TrainingType? = null,
    val sets: String = "",
    val reps: String = "",
    val weight: String = "",
    val rest: String = "",
    val muscleGroupsUiModel: MuscleGroupsUiModel = MuscleGroupsUiModel(
        TrainingType.groupByMuscleGroup(TrainingType.dummies())
    )
) {
    val showStartingTrainingButton: Boolean
        get() = selectedTrainingType != null &&
                sets.isNotBlank() &&
                reps.isNotBlank() &&
                weight.isNotBlank() &&
                rest.isNotBlank()
}

@OptIn(ExperimentalUuidApi::class)
class MenuDefinitionViewModel : ViewModel(), KoinComponent {
    private val onGoingTrainingMenuRepository by inject<OnGoingTrainingMenuRepository>()
    private val _uiState = MutableStateFlow(DefinitionMenuUiState())
    val uiState: StateFlow<DefinitionMenuUiState> = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            DefinitionMenuUiState()
        )

    fun changeTrainingType(type: TrainingType) {
        _uiState.update { it.copy(selectedTrainingType = type) }
    }

    fun changeSets(sets: String) {
        _uiState.update { it.copy(sets = sets) }
    }

    fun changeReps(reps: String) {
        _uiState.update { it.copy(reps = reps) }
    }

    fun changeWeight(weight: String) {
        _uiState.update { it.copy(weight = weight) }
    }

    fun changeRest(rest: String) {
        _uiState.update { it.copy(rest = rest) }
    }

    fun startTraining() {
        val trainingMenu = TrainingMenu(
            id = Uuid.random().toString(),
            type = uiState.value.selectedTrainingType,
            sets = uiState.value.sets.toInt(),
            reps = uiState.value.reps.toInt(),
            weight = uiState.value.weight.toDouble(),
            rest = uiState.value.rest.toInt(),
            createdAt = Clock.System.now(),
        )
        onGoingTrainingMenuRepository.updatePlannedTrainingMenu(trainingMenu)
        onGoingTrainingMenuRepository.updateOnGoingTrainingMenu(trainingMenu)
    }
}
