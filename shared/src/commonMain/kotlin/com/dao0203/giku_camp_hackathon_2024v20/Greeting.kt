package com.dao0203.giku_camp_hackathon_2024v20

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}