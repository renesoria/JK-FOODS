package com.ucb.food.events.data.repository

import com.ucb.food.events.data.datasource.local.EventDao
import com.ucb.food.events.data.datasource.local.EventEntity
import com.ucb.food.events.domain.repository.EventRepository
import com.ucb.food.portafolio.data.datasource.FirebaseManager
import kotlinx.datetime.Clock

class EventRepositoryImpl(
    private val eventDao: EventDao,
    private val firebaseManager: FirebaseManager
) : EventRepository {

    override suspend fun logEvent(type: String) {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        
        // 1. Guardar en Room (Local)
        val entity = EventEntity(
            timestamp = timestamp,
            eventType = type
        )
        eventDao.insertEvent(entity)

        // 2. Intentar subir a Firebase Realtime Database
        try {
            // Creamos un JSON simple o string para Firebase
            // Nodo: event_logs/1672531200000_OPEN
            val path = "event_logs/${timestamp}_$type"
            val value = "{\"timestamp\": $timestamp, \"type\": \"$type\"}"
            
            firebaseManager.saveData(path, value)
        } catch (e: Exception) {
            // Si falla (ej. sin internet), no pasa nada, ya está en Room.
            e.printStackTrace()
        }
    }
}
