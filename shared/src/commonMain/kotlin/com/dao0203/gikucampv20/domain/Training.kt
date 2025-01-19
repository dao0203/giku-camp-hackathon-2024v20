package com.dao0203.gikucampv20.domain

import kotlinx.datetime.LocalDate

sealed interface Training {
    val id: String
    val type: TrainingType

    // TODO: 既存のTrainingMenuと入れ替える
    data class Menu(
        override val id: String,
        override val type: TrainingType,
        val sets: Int,
        val reps: Int,
        val weight: Double,
        val rest: Int,
        val createdAt: LocalDate,
    ) : Training

    data class History(
        override val id: String,
        override val type: TrainingType,
        val sets: Int,
        val workoutSet: List<WorkoutSet>,
        val createdAt: LocalDate,
    ) : Training {
        companion object
    }
}

fun List<Training.History>.byMuscleGroup(): Map<MuscleGroup, List<Training.History>> {
    return groupBy { it.type.muscleGroups.first() }
}

fun Training.History.Companion.dummies(): Map<MuscleGroup, List<Training.History>> {
    return mapOf(
        MuscleGroup.BACK to
            listOf(
                Training.History(
                    id = "dummy",
                    type = TrainingType.dummy(),
                    sets = 4,
                    workoutSet = WorkoutSet.dummies("dummy"),
                    createdAt = LocalDate(2021, 1, 1),
                ),
            ),
    )
}
