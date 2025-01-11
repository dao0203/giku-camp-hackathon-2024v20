package com.dao0203.giku_camp_hackathon_2024v20

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val platformModule = module {
    singleOf(::Platform)
}

expect class Platform() {
    val name: String
}
