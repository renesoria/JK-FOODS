package com.ucb.food.events.domain.repository

interface EventRepository {
    suspend fun logEvent(type: String)
}
