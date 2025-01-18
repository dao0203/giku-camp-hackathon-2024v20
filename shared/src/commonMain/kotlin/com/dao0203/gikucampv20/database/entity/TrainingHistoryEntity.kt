package com.dao0203.gikucampv20.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dao0203.gikucampv20.domain.Training
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Entity(tableName = "training_history")
data class TrainingHistoryEntity(
    @PrimaryKey val id: String,
    val type: TrainingTypeEntity,
    val sets: Int,
    val workoutSet: List<WorkoutSetEntity>,
    val createdAt: Long,
)

private fun TrainingHistoryEntity.toTrainingHistory(): Training.History {
    return Training.History(
        id = id,
        type = type.toTrainingType(),
        sets = sets,
        workoutSet = workoutSet.toWorkoutSet(),
        createdAt =
            Instant.fromEpochMilliseconds(createdAt)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    )
}
