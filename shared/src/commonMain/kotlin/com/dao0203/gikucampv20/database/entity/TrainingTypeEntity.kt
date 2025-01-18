package com.dao0203.gikucampv20.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverter
import com.dao0203.gikucampv20.domain.TrainingType
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json

@Entity
data class TrainingTypeEntity(
    val trainingTypeId: String,
    val name: String,
    val description: String,
    val muscleGroups: List<MuscleGroupEntity>,
    val targetPoseLandmarksIndices: List<PoseLandmarksIndexEntity>?,
    @ColumnInfo(name = "training_type_created_at")
    val createdAt: Long,
)

object TrainingTypeEntityListConverter {
    @TypeConverter
    fun fromTrainingTypeEntityList(value: List<TrainingTypeEntity>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toTrainingTypeEntityList(value: String): List<TrainingTypeEntity> {
        return Json.decodeFromString(value)
    }
}

internal fun TrainingType.toTrainingTypeEntity(): TrainingTypeEntity {
    return TrainingTypeEntity(
        trainingTypeId = id,
        name = name,
        description = description,
        muscleGroups = muscleGroups.toMuscleGroupEntity(),
        targetPoseLandmarksIndices = targetPoseLandmarksIndices?.toPoseLandmarksIndexEntity(),
        createdAt = createdAt.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
    )
}

internal fun TrainingTypeEntity.toTrainingType(): TrainingType {
    return TrainingType(
        id = trainingTypeId,
        name = name,
        description = description,
        muscleGroups = muscleGroups.toMuscleGroup(),
        createdAt =
            Instant.fromEpochMilliseconds(createdAt)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date,
        targetPoseLandmarksIndices = targetPoseLandmarksIndices?.toPoseLandmarksIndex(),
    )
}
