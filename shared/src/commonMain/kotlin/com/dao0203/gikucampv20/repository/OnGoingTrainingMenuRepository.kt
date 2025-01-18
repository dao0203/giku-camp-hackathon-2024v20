package com.dao0203.gikucampv20.repository

import com.dao0203.gikucampv20.domain.TrainingMenu
import com.dao0203.gikucampv20.domain.default
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface OnGoingTrainingMenuRepository {
    val plannedTrainingMenu: Flow<TrainingMenu>
    val onGoingTrainingMenu: Flow<TrainingMenu>

    fun decreaseReps()

    fun resetReps()

    fun decreaseSets()

    fun updatePlannedTrainingMenu(trainingMenu: TrainingMenu)

    fun updateOnGoingTrainingMenu(trainingMenu: TrainingMenu)
}

class OnGoingTrainingMenuRepositoryImpl : OnGoingTrainingMenuRepository {
    private val _plannedTrainingMenu = MutableStateFlow(TrainingMenu.default())
    override val plannedTrainingMenu = _plannedTrainingMenu.asStateFlow()
    private val _onGoingTrainingMenu = MutableStateFlow(TrainingMenu.default())
    override val onGoingTrainingMenu = _onGoingTrainingMenu.asStateFlow()

    override fun decreaseReps() {
        _onGoingTrainingMenu.update { it.copy(reps = it.reps - 1) }
    }

    override fun resetReps() {
        _onGoingTrainingMenu.update { it.copy(reps = _plannedTrainingMenu.value.reps) }
    }

    override fun decreaseSets() {
        _onGoingTrainingMenu.update { it.copy(sets = it.sets - 1) }
    }

    override fun updatePlannedTrainingMenu(trainingMenu: TrainingMenu) {
        _plannedTrainingMenu.update { trainingMenu }
    }

    override fun updateOnGoingTrainingMenu(trainingMenu: TrainingMenu) {
        _onGoingTrainingMenu.update { trainingMenu }
    }
}
