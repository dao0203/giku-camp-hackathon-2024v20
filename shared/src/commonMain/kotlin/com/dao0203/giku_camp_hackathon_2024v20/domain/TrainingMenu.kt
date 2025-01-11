package com.dao0203.giku_camp_hackathon_2024v20.domain

import kotlinx.datetime.Instant

data class TrainingMenu(
    val id: Int,
    val type: String,
    val sets: Int,
    val reps: Int,
    val weight: Double,
    val rest: Int,
    val createdAt: Instant,
)
