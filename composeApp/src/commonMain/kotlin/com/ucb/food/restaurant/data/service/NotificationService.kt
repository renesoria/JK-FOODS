package com.ucb.food.restaurant.data.service

import com.ucb.food.restaurant.data.dto.PublicNotificationDto
import com.ucb.food.core.presentation.NotificationHandler
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class NotificationService(
    private val notificationHandler: NotificationHandler
) {
    private val database = Firebase.database.reference().child("public_notifications")
    private val auth = Firebase.auth
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private var lastProcessedId: String? = null
    private var isFirstLoad = true

    /**
     * Publica una nueva notificación en el tablón de anuncios de Firebase.
     * Se activa cuando alguien califica con 5 estrellas en el UseCase.
     */
    suspend fun sendFiveStarNotification(restaurantName: String, dishName: String) {
        try {
            val currentUserId = auth.currentUser?.uid ?: "anonymous"
            val notificationId = database.push().key ?: return
            val notification = PublicNotificationDto(
                id = notificationId,
                title = "¡Excelente recomendación! ⭐⭐⭐⭐⭐",
                message = "El plato '$dishName' en '$restaurantName' recibió 5 estrellas. ¡Tienes que probarlo!",
                senderId = currentUserId,
                timestamp = 0L 
            )
            database.child(notificationId).setValue(notification)
        } catch (e: Exception) {
            println("Error al publicar en el tablón: ${e.message}")
        }
    }

    /**
     * Activa el escucha global. 
     * Se llama desde AndroidApp.kt al iniciar la aplicación.
     */
    fun startListening() {
        serviceScope.launch {
            database.valueEvents.collect { snapshot ->
                // Obtenemos la última notificación agregada al nodo
                val lastNotification = try {
                    snapshot.children.lastOrNull()?.value<PublicNotificationDto>()
                } catch (e: Exception) {
                    null
                }

                // Evitamos disparar notificaciones viejas al conectar por primera vez
                if (isFirstLoad) {
                    lastProcessedId = lastNotification?.id
                    isFirstLoad = false
                    return@collect
                }

                val currentUserId = auth.currentUser?.uid ?: ""
                
                // Disparamos la notificación local si:
                // 1. Es nueva (ID diferente al procesado).
                // 2. No la enviamos nosotros mismos.
                if (lastNotification != null && 
                    lastNotification.id != lastProcessedId && 
                    lastNotification.senderId != currentUserId) {
                    
                    lastProcessedId = lastNotification.id
                    notificationHandler.showNotification(
                        title = lastNotification.title,
                        message = lastNotification.message
                    )
                }
            }
        }
    }
}
