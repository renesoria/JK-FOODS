package com.ucb.food

import android.app.Application
import com.ucb.food.di.getModules
import com.ucb.food.work.LogScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AndroidApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApp)
            modules(getModules())
        }

        // Programar el WorkManager al iniciar la app
        LogScheduler(this).schedulePeriodicaUpload()
    }
}
