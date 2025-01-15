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
    fun updateReps(reps: Int)
    fun updatePlannedTrainingMenu(trainingMenu: TrainingMenu)
    fun updateOnGoingTrainingMenu(trainingMenu: TrainingMenu)
}

class OnGoingTrainingMenuRepositoryImpl(
) : OnGoingTrainingMenuRepository {
    private val _plannedTrainingMenu = MutableStateFlow(TrainingMenu.default())
    override val plannedTrainingMenu = _plannedTrainingMenu.asStateFlow()
    private val _onGoingTrainingMenu = MutableStateFlow(TrainingMenu.default())
    override val onGoingTrainingMenu = _onGoingTrainingMenu.asStateFlow()

    override fun updateReps(reps: Int) {
        _onGoingTrainingMenu.update { it.copy(reps = reps) }
    }

    override fun updatePlannedTrainingMenu(trainingMenu: TrainingMenu) {
        _plannedTrainingMenu.update { trainingMenu }
    }

    override fun updateOnGoingTrainingMenu(trainingMenu: TrainingMenu) {
        _onGoingTrainingMenu.update { trainingMenu }
    }
}
