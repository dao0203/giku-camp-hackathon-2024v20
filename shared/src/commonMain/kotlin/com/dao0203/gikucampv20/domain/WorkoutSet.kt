package com.dao0203.gikucampv20.domain

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class WorkoutSet(
    val id: String,
    val reps: Int,
    val weight: Double,
    val rest: Int?,
) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun createDefault(
            reps: Int,
            weight: Double,
            rest: Int? = null,
        ) = WorkoutSet(
            id = Uuid.random().toString(),
            reps = reps,
            weight = weight,
            rest = rest,
        )
    }
}
