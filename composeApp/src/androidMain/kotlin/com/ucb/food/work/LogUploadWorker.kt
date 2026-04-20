package com.ucb.food.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ucb.food.movie.domain.usecase.GetMoviesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LogUploadWorker(
   appContext: Context,
   workerParameters: WorkerParameters
) : CoroutineWorker(appContext, workerParameters), KoinComponent {

   private val getMoviesUseCase: GetMoviesUseCase by inject()

   override suspend fun doWork(): Result {
       return try {
           println("ejecutar instrucción para subir datos")
           val movies = getMoviesUseCase.invoke()
           println("datos obtenidos mediante WorkManager: ${movies.size}")
           Result.success()
       } catch (e: Exception) {
           println("Error en WorkManager: ${e.message}")
           Result.failure()
       }
   }
}
