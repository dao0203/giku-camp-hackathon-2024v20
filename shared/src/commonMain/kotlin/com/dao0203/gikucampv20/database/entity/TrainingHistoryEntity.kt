package com.dao0203.gikucampv20.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dao0203.gikucampv20.domain.Training
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@Entity
data class TrainingHistoryEntity(
    @PrimaryKey val id: String,
    @Embedded val type: TrainingTypeEntity,
    val sets: Int,
    val createdAt: Long,
    val workoutSets: List<WorkoutSetEntity>,
) {
    companion object {
        fun fromTrainingHistory(training: Training.History): TrainingHistoryEntity {
            return TrainingHistoryEntity(
                id = training.id,
                type = training.type.toTrainingTypeEntity(),
                sets = training.sets,
                createdAt = training.createdAt.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
                workoutSets = training.workoutSet.toWorkoutSetEntity(),
            )
        }
    }
}

fun List<TrainingHistoryEntity>.toTrainingHistory(): List<Training.History> {
    return map { it.toTrainingHistory() }
}

internal fun TrainingHistoryEntity.toTrainingHistory(): Training.History {
    return Training.History(
        id = id,
        type = type.toTrainingType(),
        sets = sets,
        workoutSet = workoutSets.toWorkoutSet(),
        createdAt =
            Instant.fromEpochMilliseconds(createdAt)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    )
}
