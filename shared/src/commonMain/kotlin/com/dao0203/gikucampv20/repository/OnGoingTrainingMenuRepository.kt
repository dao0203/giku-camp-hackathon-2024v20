package com.dao0203.gikucampv20.repository

import com.dao0203.gikucampv20.domain.TrainingMenu
import com.dao0203.gikucampv20.domain.WorkoutSet
import com.dao0203.gikucampv20.domain.default
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface OnGoingTrainingMenuRepository {
    val plannedTrainingMenu: Flow<TrainingMenu>
    val onGoingTrainingMenu: Flow<TrainingMenu>
    val workoutSets: Flow<List<WorkoutSet>>

    fun decreaseReps()

    fun resetReps()

    fun decreaseSets()

    fun addWorkoutSetDefault()

    fun updateWorkoutSet(workoutSet: WorkoutSet)

    fun getCurrentWorkoutSet(): WorkoutSet

    fun updatePlannedTrainingMenu(trainingMenu: TrainingMenu)

    fun updateOnGoingTrainingMenu(trainingMenu: TrainingMenu)
}

class OnGoingTrainingMenuRepositoryImpl : OnGoingTrainingMenuRepository {
    private val _plannedTrainingMenu = MutableStateFlow(TrainingMenu.default())
    override val plannedTrainingMenu = _plannedTrainingMenu.asStateFlow()
    private val _onGoingTrainingMenu = MutableStateFlow(TrainingMenu.default())
    override val onGoingTrainingMenu = _onGoingTrainingMenu.asStateFlow()
    private val _workoutSets = MutableStateFlow(listOf<WorkoutSet>())
    override val workoutSets = _workoutSets.asStateFlow()

    override fun decreaseReps() {
        _onGoingTrainingMenu.update { it.copy(reps = it.reps - 1) }
    }

    override fun resetReps() {
        _onGoingTrainingMenu.update { it.copy(reps = _plannedTrainingMenu.value.reps) }
    }

    override fun decreaseSets() {
        _onGoingTrainingMenu.update { it.copy(sets = it.sets - 1) }
    }

    override fun getCurrentWorkoutSet(): WorkoutSet {
        val currentSet = _onGoingTrainingMenu.value.sets
        return _workoutSets.value[currentSet - 1]
    }

    override fun addWorkoutSetDefault() {
        val workoutSet =
            WorkoutSet.createDefault(
                reps = _plannedTrainingMenu.value.reps,
                weight = _plannedTrainingMenu.value.weight,
            )
        _workoutSets.update { it + workoutSet }
    }

    override fun updateWorkoutSet(workoutSet: WorkoutSet) {
        _workoutSets.update { workoutSets ->
            workoutSets.map { if (it.id == workoutSet.id) workoutSet else it }
        }
    }

    override fun updatePlannedTrainingMenu(trainingMenu: TrainingMenu) {
        _plannedTrainingMenu.update { trainingMenu }
    }

    override fun updateOnGoingTrainingMenu(trainingMenu: TrainingMenu) {
        _onGoingTrainingMenu.update { trainingMenu }
    }
}
