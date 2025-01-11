package com.dao0203.giku_camp_hackathon_2024v20

import org.koin.dsl.KoinAppDeclaration

fun startKoin(platformDeclaration: KoinAppDeclaration? = null) {
    org.koin.core.context.startKoin {
        platformDeclaration?.invoke(this)
        modules(
            sharedModule(),
        )
    }
}