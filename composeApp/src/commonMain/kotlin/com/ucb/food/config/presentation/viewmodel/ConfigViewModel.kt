package com.ucb.food.config.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.config.domain.model.AppConfig
import com.ucb.food.config.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ConfigViewModel(
    private val repository: ConfigRepository
) : ViewModel() {

    // Observamos la configuración de Room en tiempo real
    val config: StateFlow<AppConfig?> = repository.getConfig()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onSyncClicked() {
        viewModelScope.launch {
            try {
                repository.syncConfig()
            } catch (e: Exception) {
                // Aquí podrías manejar el error, por ahora lo ignoramos por simplicidad
                e.printStackTrace()
            }
        }
    }
}
