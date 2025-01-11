package com.dao0203.giku_camp_hackathon_2024v20

import org.koin.dsl.KoinAppDeclaration

fun initKoin(platformDeclaration: KoinAppDeclaration = {}) {
    org.koin.core.context.startKoin {
        platformDeclaration.invoke(this)
        modules(
//            sharedModule(),
            repositoryModule,
            viewModelModule,
        )
    }
}

fun initKoin() = initKoin {}