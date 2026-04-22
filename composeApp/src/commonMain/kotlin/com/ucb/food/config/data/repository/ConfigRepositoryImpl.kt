package com.ucb.food.config.data.repository

import com.ucb.food.config.data.datasource.local.ConfigDao
import com.ucb.food.config.data.datasource.local.ConfigEntity
import com.ucb.food.config.domain.model.AppConfig
import com.ucb.food.config.domain.repository.ConfigRepository
import com.ucb.food.firebase.getRemoteConfigString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConfigRepositoryImpl(
    private val configDao: ConfigDao
) : ConfigRepository {

    override fun getConfig(): Flow<AppConfig?> {
        return configDao.getConfig().map { entity ->
            entity?.let {
                AppConfig(
                    welcomeMessage = it.welcomeMessage,
                    backgroundColor = it.backgroundColor
                )
            }
        }
    }

    override suspend fun syncConfig() {
        // Obtenemos los valores desde Firebase Remote Config
        // Nota: Estos nombres de llaves ("welcome_message", "bg_color") 
        // deben coincidir con lo que pongas en la consola de Firebase.
        val message = getRemoteConfigString("welcome_message")
        val color = getRemoteConfigString("bg_color")

        // Guardamos en la caché local (Room)
        val newConfig = ConfigEntity(
            welcomeMessage = message.ifEmpty { "Bienvenido (Local)" },
            backgroundColor = color.ifEmpty { "#FFFFFF" }
        )
        configDao.saveConfig(newConfig)
    }
}
