package com.dao0203.giku_camp_hackathon_2024v20.repository

import com.dao0203.giku_camp_hackathon_2024v20.domain.TrainingMenu
import com.dao0203.giku_camp_hackathon_2024v20.domain.default
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

interface OnGoingTrainingMenuRepository {
    val plannedTrainingMenu: Flow<TrainingMenu>
    val onGoingTrainingMenu: Flow<TrainingMenu>
    fun updateSets(sets: Int)
}

class OnGoingTrainingMenuRepositoryImpl(
    private val coroutineScope: CoroutineScope
) : OnGoingTrainingMenuRepository {
    private val _plannedTrainingMenu = MutableStateFlow(TrainingMenu.default())
    override val plannedTrainingMenu = _plannedTrainingMenu.stateIn(
        coroutineScope,
        started = SharingStarted.WhileSubscribed(1_000),
        initialValue = TrainingMenu.default()
    )
    private val _onGoingTrainingMenu = MutableStateFlow(TrainingMenu.default())
    override val onGoingTrainingMenu = _onGoingTrainingMenu.stateIn(
        coroutineScope,
        started = SharingStarted.WhileSubscribed(1_000),
        initialValue = TrainingMenu.default()
    )

    override fun updateSets(sets: Int) {
        _onGoingTrainingMenu.update { it.copy(sets = sets) }
    }
}
