package com.dao0203.gikucampv20.database.entity

import androidx.room.Entity
import androidx.room.TypeConverter
import com.dao0203.gikucampv20.domain.PoseLandmarksIndex

@Entity
data class PoseLandmarksIndexEntity(
    val start: PoseLandmarkEntity,
    val end: PoseLandmarkEntity,
)

internal fun List<PoseLandmarksIndexEntity>.toPoseLandmarksIndex(): List<PoseLandmarksIndex> {
    return map { it.toPoseLandmarksIndex() }
}

internal fun PoseLandmarksIndexEntity.toPoseLandmarksIndex(): PoseLandmarksIndex {
    return PoseLandmarksIndex(
        start = start.toPoseLandmark(),
        end = end.toPoseLandmark(),
    )
}

object PoseLandmarkEntityConverter {
    @TypeConverter
    fun fromPoseLandmarkEntity(value: PoseLandmarkEntity): Int {
        return value.index
    }

    @TypeConverter
    fun toPoseLandmarkEntity(value: Int): PoseLandmarkEntity {
        return PoseLandmarkEntity.entries.first { it.index == value }
    }
}
