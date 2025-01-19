package com.dao0203.gikucampv20.domain

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class WorkoutSet(
    val id: String,
    val historyId: String,
    val reps: Int,
    val weight: Double,
    val rest: Int?,
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun createDefault(
            historyId: String,
            reps: Int,
            weight: Double,
            rest: Int? = null,
        ) = WorkoutSet(
            id = Uuid.random().toString(),
            historyId = historyId,
            reps = reps,
            weight = weight,
            rest = rest,
        )
    }
}

fun WorkoutSet.Companion.dummies(historyId: String) =
    listOf(
        createDefault(historyId, 10, 50.0, 60),
        createDefault(historyId, 10, 50.0, 60),
        createDefault(historyId, 10, 50.0, 60),
        createDefault(historyId, 10, 50.0, 60),
    )
