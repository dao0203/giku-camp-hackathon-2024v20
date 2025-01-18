package com.dao0203.gikucampv20.domain

import kotlinx.datetime.Instant

data class TrainingType(
    val id: String,
    val name: String,
    val description: String,
    val muscleGroups: List<MuscleGroup>,
    val createdAt: Instant,
    val targetPoseLandmarksIndices: List<PoseLandmarksIndex>? = null, // TODO: Remove initial value
) {
    companion object
}

data class PoseLandmarksIndex(
    val start: PoseLandmark,
    val end: PoseLandmark,
)

fun TrainingType.Companion.defaults() =
    listOf(
        TrainingType(
            id = "1",
            name = "ベンチプレス",
            description = "ベンチプレスは胸と上腕三頭筋の筋力と筋肉を鍛えるコンパウンドエクササイズです。",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS),
            createdAt = Instant.DISTANT_PAST,
            targetPoseLandmarksIndices =
                listOf(
                    PoseLandmarksIndex(
                        start = PoseLandmark.LEFT_WRIST,
                        end = PoseLandmark.RIGHT_WRIST,
                    ),
                ),
        ),
        TrainingType(
            id = "2",
            name = "デッドリフト",
            description = "デッドリフトは背中、脚、前腕の筋力と筋肉を鍛えるコンパウンドエクササイズです。",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.LEGS, MuscleGroup.FOREARM),
            createdAt = Instant.DISTANT_PAST,
            targetPoseLandmarksIndices =
                listOf(
                    PoseLandmarksIndex(
                        start = PoseLandmark.LEFT_WRIST,
                        end = PoseLandmark.RIGHT_WRIST,
                    ),
                ),
        ),
        TrainingType(
            id = "3",
            name = "ショルダープレス",
            description = "ショルダープレスは肩の筋力と筋肉を鍛えるアイソレーションエクササイズです。",
            muscleGroups = listOf(MuscleGroup.SHOULDER),
            targetPoseLandmarksIndices =
                listOf(
                    PoseLandmarksIndex(
                        start = PoseLandmark.LEFT_WRIST,
                        end = PoseLandmark.RIGHT_WRIST,
                    ),
                ),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "4",
            name = "バイセップカール",
            description = "バイセップカールは上腕二頭筋の筋力と筋肉を鍛えるアイソレーションエクササイズです。",
            muscleGroups = listOf(MuscleGroup.BICEPS),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "5",
            name = "トライセップエクステンション",
            description = "トライセップエクステンションは上腕三頭筋の筋力と筋肉を鍛えるアイソレーションエクササイズです。",
            muscleGroups = listOf(MuscleGroup.TRICEPS),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "6",
            name = "レッグプレス",
            description = "レッグプレスは脚の筋力と筋肉を鍛えるコンパウンドエクササイズです。",
            muscleGroups = listOf(MuscleGroup.LEGS),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "7",
            name = "クランチ",
            description = "クランチは腹筋の筋力と筋肉を鍛えるアイソレーションエクササイズです。",
            muscleGroups = listOf(MuscleGroup.ABS),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "8",
            name = "プルアップ",
            description = "プルアップは背中と前腕の筋力と筋肉を鍛えるコンパウンドエクササイズです。",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.FOREARM),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "9",
            name = "スクワット",
            description = "スクワットは脚と背中の筋力と筋肉を鍛えるコンパウンドエクササイズです。",
            muscleGroups = listOf(MuscleGroup.LEGS, MuscleGroup.BACK),
            createdAt = Instant.DISTANT_PAST,
            targetPoseLandmarksIndices =
                listOf(
                    PoseLandmarksIndex(
                        start = PoseLandmark.LEFT_HIP,
                        end = PoseLandmark.RIGHT_HIP,
                    ),
                ),
        ),
        TrainingType(
            id = "10",
            name = "ダンベルロー",
            description = "ダンベルローは背中と前腕の筋力と筋肉を鍛えるコンパウンドエクササイズです。",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.FOREARM),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "11",
            name = "ラテラルレイズ",
            description = "ラテラルレイズは肩の筋力と筋肉を鍛えるアイソレーションエクササイズです。",
            muscleGroups = listOf(MuscleGroup.SHOULDER),
            createdAt = Instant.DISTANT_PAST,
        ),
        TrainingType(
            id = "12",
            name = "ハンマーカール",
            description = "ハンマーカールは上腕二頭筋の筋力と筋肉を鍛えるアイソレーションエクササイズです。",
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
