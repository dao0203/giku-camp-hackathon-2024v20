package com.dao0203.giku_camp_hackathon_2024v20.android

import android.app.Application
import com.dao0203.giku_camp_hackathon_2024v20.startKoin
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
        }
    }
}