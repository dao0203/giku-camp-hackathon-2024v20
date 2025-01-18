package com.dao0203.gikucampv20

import androidx.room.Room
import com.dao0203.gikucampv20.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val databaseModule =
    module {
        single<AppDatabase> {
            val context = androidContext().applicationContext
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "gikucampv20",
            )
                .build()
        }
    }
