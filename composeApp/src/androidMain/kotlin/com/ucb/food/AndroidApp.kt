package com.ucb.food

import android.app.Application
import com.ucb.food.di.getModules
import com.ucb.food.di.platformModule
import com.ucb.food.restaurant.data.service.NotificationService
import com.ucb.food.work.LogScheduler
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AndroidApp: Application() {

    // Inyectamos el servicio de notificaciones
    private val notificationService: NotificationService by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApp)
            // Pasamos el platformModule que contiene el AndroidNotificationHandler
            modules(getModules(listOf(platformModule)))
        }

        // ¡ENCENDEMOS EL VIGILANTE!
        // Ahora la app estará atenta a la base de datos para mostrar notificaciones
        notificationService.startListening()

        // Programar el WorkManager al iniciar la app
        LogScheduler(this).schedulePeriodicaUpload()
    }
}
