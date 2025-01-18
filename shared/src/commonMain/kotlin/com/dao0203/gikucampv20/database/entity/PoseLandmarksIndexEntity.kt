package com.dao0203.gikucampv20.database.entity

import androidx.room.Entity
import androidx.room.TypeConverter
import com.dao0203.gikucampv20.domain.PoseLandmarksIndex
import kotlinx.serialization.json.Json

@Entity
data class PoseLandmarksIndexEntity(
    val start: PoseLandmarkEntity,
    val end: PoseLandmarkEntity,
)

object PoseLandmarksIndexEntityListConverter {
    @TypeConverter
    fun fromPoseLandmarksIndexEntityList(value: List<PoseLandmarksIndexEntity>?): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toPoseLandmarksIndexEntityList(value: String): List<PoseLandmarksIndexEntity>? {
        return Json.decodeFromString(value)
    }
}

internal fun List<PoseLandmarksIndex>.toPoseLandmarksIndexEntity(): List<PoseLandmarksIndexEntity> {
    return map { it.toPoseLandmarksIndexEntity() }
}

internal fun PoseLandmarksIndex.toPoseLandmarksIndexEntity(): PoseLandmarksIndexEntity {
    return PoseLandmarksIndexEntity(
        start = start.toPoseLandmarkEntity(),
        end = end.toPoseLandmarkEntity(),
    )
}

internal fun List<PoseLandmarksIndexEntity>.toPoseLandmarksIndex(): List<PoseLandmarksIndex> {
    return map { it.toPoseLandmarksIndex() }
}

internal fun PoseLandmarksIndexEntity.toPoseLandmarksIndex(): PoseLandmarksIndex {
    return PoseLandmarksIndex(
        start = start.toPoseLandmark(),
        end = end.toPoseLandmark(),
    )
}
