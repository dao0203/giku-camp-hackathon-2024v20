package com.dao0203.gikucampv20

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