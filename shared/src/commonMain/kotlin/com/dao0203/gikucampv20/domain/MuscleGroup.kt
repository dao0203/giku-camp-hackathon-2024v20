package com.dao0203.gikucampv20.domain

enum class MuscleGroup(val id: Int, val description: String) {
    OTHER(0, "その他"),
    CHEST(1, "胸"),
    BACK(2, "背中"),
    FOREARM(3, "前腕"),
    SHOULDER(4, "肩"),
    TRICEPS(5, "上腕三頭筋"),
    BICEPS(6, "上腕二頭筋"),
    LEGS(7, "脚"),
    ABS(8, "腹筋"),
}
