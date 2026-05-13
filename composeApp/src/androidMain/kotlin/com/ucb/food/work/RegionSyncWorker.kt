package com.ucb.food.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.food.region.domain.repository.RegionRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegionSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val regionRepository: RegionRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            Log.d("RegionSyncWorker", "Iniciando sincronización de regiones en segundo plano...")
            regionRepository.syncRegions()
            Log.d("RegionSyncWorker", "Sincronización completada exitosamente.")
            Result.success()
        } catch (e: Exception) {
            Log.e("RegionSyncWorker", "Error en sincronización: ${e.message}")
            Result.retry()
        }
    }
}
