package com.ucb.food.portafolio.data.datasource

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await


actual class FirebaseManager actual constructor(){
    private val database = FirebaseDatabase.getInstance().reference


    actual suspend fun saveData(path: String, value: String) {
        try {
            database.child(path).setValue(value).await()
            println("Firebase Android: Guardado exitoso en $path")
        } catch (e: Exception) {
            println("Firebase Android: Error - ${e.message}")
        }
    }

    actual suspend fun getData(path: String): String? {
        return try {
            val snapshot = database.child(path).get().await()
            snapshot.value?.toString()
        } catch (e: Exception) {
            println("Firebase Android: Error al obtener datos - ${e.message}")
            null
        }
    }
}
