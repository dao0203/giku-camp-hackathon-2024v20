package com.dao0203.gikucampv20.database.entity

import androidx.room.Entity
import androidx.room.TypeConverter
import com.dao0203.gikucampv20.domain.MuscleGroup
import kotlinx.serialization.json.Json

@Entity
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

object MuscleGroupEntityConverter {
    @TypeConverter
    fun fromMuscleGroupEntity(value: MuscleGroupEntity): Int {
        return value.id
    }

    @TypeConverter
    fun toMuscleGroupEntity(value: Int): MuscleGroupEntity {
        return MuscleGroupEntity.entries.first { it.id == value }
    }
}

internal fun List<MuscleGroupEntity>.toMuscleGroup(): List<MuscleGroup> {
    return map { it.toMuscleGroup() }
}

internal fun MuscleGroupEntity.toMuscleGroup(): MuscleGroup {
    return MuscleGroup.valueOf(name)
}
