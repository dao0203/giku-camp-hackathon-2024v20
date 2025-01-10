package com.dao0203.giku_camp_hackathon_2024v20

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform