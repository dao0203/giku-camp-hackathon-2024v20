package com.dao0203.gikucampv20.database.entity

import androidx.room.TypeConverter
import com.dao0203.gikucampv20.domain.MuscleGroup
import kotlinx.serialization.json.Json

enum class MuscleGroupEntity(val id: Int) {
    OTHER(0),
    CHEST(1),
    BACK(2),
    FOREARM(3),
    SHOULDER(4),
    TRICEPS(5),
    BICEPS(6),
    LEGS(7),
    ABS(8),
}

object MuscleGroupEntityListConverter {
    @TypeConverter
    fun fromMuscleGroupEntityList(value: List<MuscleGroupEntity>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toMuscleGroupEntityList(value: String): List<MuscleGroupEntity> {
        return Json.decodeFromString(value)
    }
}

internal fun List<MuscleGroup>.toMuscleGroupEntity(): List<MuscleGroupEntity> {
    return map { it.toMuscleGroupEntity() }
}

private fun MuscleGroup.toMuscleGroupEntity(): MuscleGroupEntity {
    return when (this) {
        MuscleGroup.OTHER -> MuscleGroupEntity.OTHER
        MuscleGroup.CHEST -> MuscleGroupEntity.CHEST
        MuscleGroup.BACK -> MuscleGroupEntity.BACK
        MuscleGroup.FOREARM -> MuscleGroupEntity.FOREARM
        MuscleGroup.SHOULDER -> MuscleGroupEntity.SHOULDER
        MuscleGroup.TRICEPS -> MuscleGroupEntity.TRICEPS
        MuscleGroup.BICEPS -> MuscleGroupEntity.BICEPS
        MuscleGroup.LEGS -> MuscleGroupEntity.LEGS
        MuscleGroup.ABS -> MuscleGroupEntity.ABS
    }
}

internal fun List<MuscleGroupEntity>.toMuscleGroup(): List<MuscleGroup> {
    return map { it.toMuscleGroup() }
}

internal fun MuscleGroupEntity.toMuscleGroup(): MuscleGroup {
    return MuscleGroup.valueOf(name)
}
