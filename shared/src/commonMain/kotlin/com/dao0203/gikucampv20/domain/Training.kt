package com.dao0203.gikucampv20.domain

import kotlinx.datetime.LocalDate

sealed interface Training {
    val id: String
    val type: TrainingType

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
    ) : Training
}

data class WorkoutSet(
    val reps: Int,
    val weight: Double,
    val rest: Int,
)
