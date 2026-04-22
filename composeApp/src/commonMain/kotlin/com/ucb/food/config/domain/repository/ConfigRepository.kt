package com.ucb.food.config.domain.repository

import com.ucb.food.config.domain.model.AppConfig
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    fun getConfig(): Flow<AppConfig?>
    suspend fun syncConfig()
}
