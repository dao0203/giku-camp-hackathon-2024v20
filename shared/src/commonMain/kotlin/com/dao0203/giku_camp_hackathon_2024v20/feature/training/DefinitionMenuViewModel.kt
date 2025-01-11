@file:OptIn(ExperimentalUuidApi::class)

package com.dao0203.giku_camp_hackathon_2024v20.feature.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dao0203.giku_camp_hackathon_2024v20.domain.TrainingMenu
import com.dao0203.giku_camp_hackathon_2024v20.domain.TrainingType
import com.dao0203.giku_camp_hackathon_2024v20.domain.default
import com.dao0203.giku_camp_hackathon_2024v20.domain.dummies
import com.dao0203.giku_camp_hackathon_2024v20.repository.OnGoingTrainingMenuRepository
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class DefinitionMenuUiState(
    val trainingMenu: TrainingMenu = TrainingMenu.default(),
    val trainingTypes: List<TrainingType> = TrainingType.dummies() //TODO: fetch from API or Local DB
)

@OptIn(ExperimentalUuidApi::class)
class DefinitionMenuViewModel(
) : ViewModel(), KoinComponent{
    private val onGoingTrainingMenuRepository by inject<OnGoingTrainingMenuRepository>()
    private val _uiState = MutableStateFlow(DefinitionMenuUiState())
    val uiState: StateFlow<DefinitionMenuUiState> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        DefinitionMenuUiState()
    )

    fun changeTrainingType(type: TrainingType) {
        _uiState.update { it.copy(trainingMenu = it.trainingMenu.copy(type = type)) }
    }

    fun changeSets(sets: Int) {
        _uiState.update { it.copy(trainingMenu = it.trainingMenu.copy(sets = sets)) }
    }

    fun changeReps(reps: Int) {
        _uiState.update { it.copy(trainingMenu = it.trainingMenu.copy(reps = reps)) }
    }

    fun changeWeight(weight: Double) {
        _uiState.update { it.copy(trainingMenu = it.trainingMenu.copy(weight = weight)) }
    }

    fun changeRest(rest: Int) {
        _uiState.update { it.copy(trainingMenu = it.trainingMenu.copy(rest = rest)) }
    }

    fun startTraining() {
        _uiState.update {
            it.copy(
                trainingMenu = it.trainingMenu.copy(
                    id = Uuid.random().toString(),
                    createdAt = Clock.System.now()
                )
            )
        }
        onGoingTrainingMenuRepository.updateTrainingMenu(uiState.value.trainingMenu)
    }
}