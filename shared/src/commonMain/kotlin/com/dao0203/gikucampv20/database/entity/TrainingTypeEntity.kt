package com.dao0203.gikucampv20.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.dao0203.gikucampv20.domain.TrainingType
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json

@Entity
data class TrainingTypeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val muscleGroups: List<MuscleGroupEntity>,
    val createdAt: Long,
    val targetPoseLandmarksIndices: List<PoseLandmarksIndexEntity>? = null,
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

internal fun TrainingTypeEntity.toTrainingType(): TrainingType {
    return TrainingType(
        id = id,
        name = name,
        description = description,
        muscleGroups = muscleGroups.toMuscleGroup(),
        createdAt =
            Instant.fromEpochMilliseconds(createdAt)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date,
        targetPoseLandmarksIndices = targetPoseLandmarksIndices?.toPoseLandmarksIndex(),
    )
}
