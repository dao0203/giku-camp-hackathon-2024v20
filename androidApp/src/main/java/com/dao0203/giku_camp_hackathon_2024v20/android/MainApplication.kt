package com.dao0203.giku_camp_hackathon_2024v20.android

import android.app.Application
import com.dao0203.giku_camp_hackathon_2024v20.android.feature.training.TrainingWithCameraViewModel
import com.dao0203.giku_camp_hackathon_2024v20.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::TrainingWithCameraViewModel)
}

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}