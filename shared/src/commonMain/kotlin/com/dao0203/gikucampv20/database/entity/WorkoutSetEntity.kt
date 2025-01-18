package com.dao0203.gikucampv20.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.dao0203.gikucampv20.domain.WorkoutSet
import kotlinx.serialization.json.Json

@Entity
data class WorkoutSetEntity(
    @PrimaryKey val workoutSetId: String,
    val historyId: String,
    val reps: Int,
    val weight: Double,
    val rest: Int?,
)

object WorkoutSetEntityListConverter {
    @TypeConverter
    fun fromWorkoutSetEntityList(value: List<WorkoutSetEntity>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toWorkoutSetEntityList(value: String): List<WorkoutSetEntity> {
        return Json.decodeFromString(value)
    }
}

internal fun List<WorkoutSetEntity>.toWorkoutSet(): List<WorkoutSet> {
    return map { it.toWorkoutSet() }
}

private fun WorkoutSetEntity.toWorkoutSet(): WorkoutSet {
    return WorkoutSet(
        id = workoutSetId,
        reps = reps,
        weight = weight,
        rest = rest,
    )
}
