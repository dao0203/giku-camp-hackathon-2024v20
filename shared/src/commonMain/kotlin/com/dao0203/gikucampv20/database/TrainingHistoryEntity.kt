package com.dao0203.gikucampv20.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.dao0203.gikucampv20.domain.MuscleGroup
import com.dao0203.gikucampv20.domain.PoseLandmark
import com.dao0203.gikucampv20.domain.PoseLandmarksIndex
import com.dao0203.gikucampv20.domain.Training
import com.dao0203.gikucampv20.domain.TrainingType
import com.dao0203.gikucampv20.domain.WorkoutSet
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TrainingHistoryEntity(
    val id: String,
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

@Entity
data class TrainingTypeEntity(
    val id: String,
    val name: String,
    val description: String,
    val muscleGroups: List<MuscleGroupEntity>,
    val createdAt: Long,
    val targetPoseLandmarksIndices: List<PoseLandmarksIndexEntity>? = null,
)

private fun TrainingTypeEntity.toTrainingType(): TrainingType {
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

@Entity
enum class MuscleGroupEntity(private val id: Int) {
    OTHER(0),
    CHEST(1),
    BACK(2),
    FOREARM(3),
    SHOULDER(4),
    TRICEPS(5),
    BICEPS(6),
    LEGS(7),
    ABS(8),
    ;

    @TypeConverter
    fun fromMuscleGroupEntity(value: MuscleGroupEntity): Int {
        return value.id
    }

    @TypeConverter
    fun toMuscleGroupEntity(value: Int): MuscleGroupEntity {
        return entries.first { it.id == value }
    }
}

private fun List<MuscleGroupEntity>.toMuscleGroup(): List<MuscleGroup> {
    return map { it.toMuscleGroup() }
}

private fun MuscleGroupEntity.toMuscleGroup(): MuscleGroup {
    return MuscleGroup.valueOf(name)
}

@Entity
data class PoseLandmarksIndexEntity(
    val start: PoseLandmarkEntity,
    val end: PoseLandmarkEntity,
)

private fun List<PoseLandmarksIndexEntity>.toPoseLandmarksIndex(): List<PoseLandmarksIndex> {
    return map { it.toPoseLandmarksIndex() }
}

private fun PoseLandmarksIndexEntity.toPoseLandmarksIndex(): PoseLandmarksIndex {
    return PoseLandmarksIndex(
        start = start.toPoseLandmark(),
        end = end.toPoseLandmark(),
    )
}

@Entity
enum class PoseLandmarkEntity(
    private val index: Int,
) {
    NOSE(0),
    LEFT_EYE_INNER(1),
    LEFT_EYE(2),
    LEFT_EYE_OUTER(3),
    RIGHT_EYE_INNER(4),
    RIGHT_EYE(5),
    RIGHT_EYE_OUTER(6),
    LEFT_EAR(7),
    RIGHT_EAR(8),
    MOUTH_LEFT(9),
    MOUTH_RIGHT(10),
    LEFT_SHOULDER(11),
    RIGHT_SHOULDER(12),
    LEFT_ELBOW(13),
    RIGHT_ELBOW(14),
    LEFT_WRIST(15),
    RIGHT_WRIST(16),
    LEFT_PINKY(17),
    RIGHT_PINKY(18),
    LEFT_INDEX(19),
    RIGHT_INDEX(20),
    LEFT_THUMB(21),
    RIGHT_THUMB(22),
    LEFT_HIP(23),
    RIGHT_HIP(24),
    LEFT_KNEE(25),
    RIGHT_KNEE(26),
    LEFT_ANKLE(27),
    RIGHT_ANKLE(28),
    LEFT_HEEL(29),
    RIGHT_HEEL(30),
    LEFT_FOOT_INDEX(31),
    RIGHT_FOOT_INDEX(32),
    ;

    @TypeConverter
    fun fromPoseLandmarkEntity(value: PoseLandmarkEntity): Int {
        return value.index
    }

    @TypeConverter
    fun toPoseLandmarkEntity(value: Int): PoseLandmarkEntity {
        return entries.first { it.index == value }
    }
}

private fun PoseLandmarkEntity.toPoseLandmark(): PoseLandmark {
    return PoseLandmark.valueOf(name)
}

@Entity
data class WorkoutSetEntity(
    @PrimaryKey val id: String,
    val reps: Int,
    val weight: Double,
    val rest: Int,
) {
    companion object {
        fun fromWorkoutSet(workoutSet: WorkoutSet): WorkoutSetEntity {
            return WorkoutSetEntity(
                id = workoutSet.id,
                reps = workoutSet.reps,
                weight = workoutSet.weight,
                rest = workoutSet.rest,
            )
        }

        @OptIn(ExperimentalUuidApi::class)
        fun createFromWorkoutSet(workoutSet: WorkoutSet): WorkoutSetEntity {
            return WorkoutSetEntity(
                id = Uuid.random().toString(),
                reps = workoutSet.reps,
                weight = workoutSet.weight,
                rest = workoutSet.rest,
            )
        }
    }
}

private fun List<WorkoutSetEntity>.toWorkoutSet(): List<WorkoutSet> {
    return map { it.toWorkoutSet() }
}

private fun WorkoutSetEntity.toWorkoutSet(): WorkoutSet {
    return WorkoutSet(
        id = id,
        reps = reps,
        weight = weight,
        rest = rest,
    )
}
