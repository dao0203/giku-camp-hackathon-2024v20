package com.dao0203.gikucampv20.domain

import kotlinx.datetime.Instant

data class TrainingType(
    val id: String,
    val name: String,
    val description: String,
    val muscleGroups: List<MuscleGroup>,
    val createdAt: Instant,
    val targetLandmarkIndexes: List<LandmarkIndex>? = null, // TODO: Remove initial value
) {
    companion object
}

data class LandmarkIndex(
    val start: PoseLandmark,
    val end: PoseLandmark,
)

fun TrainingType.Companion.dummies() =
    listOf(
        TrainingType(
            id = "1",
            name = "Bench Press",
            description = "Bench Press is a compound exercise that builds strength and muscle in the chest and triceps.",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS),
            createdAt = Instant.DISTANT_PAST,
            targetLandmarkIndexes =
                listOf(
                    LandmarkIndex(
                        start = PoseLandmark.LEFT_WRIST,
                        end = PoseLandmark.RIGHT_WRIST,
                    ),
                ),
        ),
        TrainingType(
            id = "2",
            name = "Deadlift",
            description = "Deadlift is a compound exercise that builds strength and muscle in the back, legs, and forearms.",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.LEGS, MuscleGroup.FOREARM),
            createdAt = Instant.DISTANT_PAST,
            targetLandmarkIndexes =
                listOf(
                    LandmarkIndex(
                        start = PoseLandmark.LEFT_WRIST,
                        end = PoseLandmark.RIGHT_WRIST,
                    ),
                ),
        ),
        TrainingType(
            id = "3",
            name = "Shoulder Press",
            description = "Shoulder Press is an isolation exercise that builds strength and muscle in the shoulders.",
            muscleGroups = listOf(MuscleGroup.SHOULDER),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "4",
            name = "Bicep Curl",
            description = "Bicep Curl is an isolation exercise that builds strength and muscle in the biceps.",
            muscleGroups = listOf(MuscleGroup.BICEPS),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "5",
            name = "Tricep Extension",
            description = "Tricep Extension is an isolation exercise that builds strength and muscle in the triceps.",
            muscleGroups = listOf(MuscleGroup.TRICEPS),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "6",
            name = "Leg Press",
            description = "Leg Press is a compound exercise that builds strength and muscle in the legs.",
            muscleGroups = listOf(MuscleGroup.LEGS),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "7",
            name = "Crunch",
            description = "Crunch is an isolation exercise that builds strength and muscle in the abs.",
            muscleGroups = listOf(MuscleGroup.ABS),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "8",
            name = "Pull-up",
            description = "Pull-up is a compound exercise that builds strength and muscle in the back and forearms.",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.FOREARM),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "9",
            name = "Squat",
            description = "Squat is a compound exercise that builds strength and muscle in the legs and back.",
            muscleGroups = listOf(MuscleGroup.LEGS, MuscleGroup.BACK),
            createdAt = Instant.DISTANT_PAST,
            targetLandmarkIndexes =
                listOf(
                    LandmarkIndex(
                        start = PoseLandmark.LEFT_HIP,
                        end = PoseLandmark.RIGHT_HIP,
                    ),
                ),
        ),
        TrainingType(
            id = "10",
            name = "Dumbbell Row",
            description = "Dumbbell Row is a compound exercise that builds strength and muscle in the back and forearms.",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.FOREARM),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "11",
            name = "Lateral Raise",
            description = "Lateral Raise is an isolation exercise that builds strength and muscle in the shoulders.",
            muscleGroups = listOf(MuscleGroup.SHOULDER),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "12",
            name = "Hammer Curl",
            description = "Hammer Curl is an isolation exercise that builds strength and muscle in the biceps.",
            muscleGroups = listOf(MuscleGroup.BICEPS),
            createdAt = Instant.DISTANT_PAST,
        ),
    )

fun TrainingType.Companion.groupByMuscleGroup(trainingTypes: List<TrainingType>): Map<MuscleGroup, List<TrainingType>> {
    val map = mutableMapOf<MuscleGroup, MutableList<TrainingType>>()
    trainingTypes.forEach { trainingType ->
        trainingType.muscleGroups.forEach { muscleGroup ->
            if (map[muscleGroup] == null) {
                map[muscleGroup] = mutableListOf()
            }
            map[muscleGroup]!!.add(trainingType)
        }
    }
    return map
}
