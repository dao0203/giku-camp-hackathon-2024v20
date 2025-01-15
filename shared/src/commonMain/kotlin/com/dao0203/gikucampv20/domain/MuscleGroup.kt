package com.dao0203.gikucampv20.domain

enum class MuscleGroup(val id: Int) {
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

    companion object {
        fun fromId(id: Int): MuscleGroup? {
            return entries.find { it.id == id }
        }
    }
}
