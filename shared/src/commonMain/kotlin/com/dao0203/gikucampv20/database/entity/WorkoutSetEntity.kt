package com.dao0203.gikucampv20.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dao0203.gikucampv20.domain.WorkoutSet
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity
data class WorkoutSetEntity(
    @PrimaryKey val id: String,
    val reps: Int,
    val weight: Double,
    val rest: Int,
) {
    companion object {
        fun fromWorkoutSet(workoutSet: WorkoutSet): WorkoutSetEntity {
            return WorkoutSetEntity(
                id = workoutSet.id,
                reps = workoutSet.reps,
                weight = workoutSet.weight,
                rest = workoutSet.rest,
            )
        }

        @OptIn(ExperimentalUuidApi::class)
        fun createFromWorkoutSet(workoutSet: WorkoutSet): WorkoutSetEntity {
            return WorkoutSetEntity(
                id = Uuid.random().toString(),
                reps = workoutSet.reps,
                weight = workoutSet.weight,
                rest = workoutSet.rest,
            )
        }
    }
}

private fun List<WorkoutSetEntity>.toWorkoutSet(): List<WorkoutSet> {
    return map { it.toWorkoutSet() }
}

private fun WorkoutSetEntity.toWorkoutSet(): WorkoutSet {
    return WorkoutSet(
        id = id,
        reps = reps,
        weight = weight,
        rest = rest,
    )
}
