package com.dao0203.giku_camp_hackathon_2024v20.android

import android.app.Application
import com.dao0203.giku_camp_hackathon_2024v20.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(sharedModule())
        }
    }
}