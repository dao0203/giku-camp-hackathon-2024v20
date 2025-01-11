package com.dao0203.giku_camp_hackathon_2024v20.domain

import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TrainingMenu(
    val id: String,
    val type: TrainingType?,
    val sets: Int,
    val reps: Int,
    val weight: Double,
    val rest: Int,
    val createdAt: Instant,
) {
    companion object
}

@OptIn(ExperimentalUuidApi::class)
fun TrainingMenu.Companion.default() = TrainingMenu(
    id = Uuid.random().toString(),
    type = null,
    sets = 0,
    reps = 0,
    weight = 0.0,
    rest = 0,
    createdAt = Instant.DISTANT_PAST,
)
