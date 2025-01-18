package com.dao0203.gikucampv20.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dao0203.gikucampv20.domain.Training
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Entity
data class TrainingHistoryEntity(
    @PrimaryKey val id: String,
    @Embedded val type: TrainingTypeEntity,
    val sets: Int,
    val createdAt: Long,
    val workoutSets: List<WorkoutSetEntity>,
)

private fun TrainingHistoryEntity.toTrainingHistory(): Training.History {
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
