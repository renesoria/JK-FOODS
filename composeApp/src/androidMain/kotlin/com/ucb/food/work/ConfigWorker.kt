package com.ucb.food.work

import AppDatabase
import ConfigEntity
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ConfigWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val db: AppDatabase by inject()

    override suspend fun doWork(): Result {
        return try {
            val remoteConfig = Firebase.remoteConfig


            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
            remoteConfig.setConfigSettingsAsync(configSettings).await()


            remoteConfig.fetchAndActivate().await()
            

            val textoBienvenida = remoteConfig.getString("texto_bienvenida")
            Log.d("ConfigWorker", "Valor descargado y guardando en Room: $textoBienvenida")


            db.getConfigDao().saveConfig(
                ConfigEntity(key = "texto_bienvenida", value = textoBienvenida)
            )
            
            Result.success()
        } catch (e: Exception) {
            Log.e("ConfigWorker", "Error sincronizando: ${e.message}")
            Result.retry()
        }
    }
}
