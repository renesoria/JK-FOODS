package com.ucb.food.core.data.repository

import com.ucb.food.core.domain.repository.ThemeRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeRepositoryImpl(
    private val settings: Settings
) : ThemeRepository {

    companion object {
        private const val KEY_IS_DARK_MODE = "is_dark_mode"
    }

    private val _isDarkMode = MutableStateFlow(settings.getBoolean(KEY_IS_DARK_MODE, false))
    override val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    override suspend fun toggleTheme() {
        val newValue = !_isDarkMode.value
        setDarkMode(newValue)
    }

    override suspend fun setDarkMode(enabled: Boolean) {
        settings[KEY_IS_DARK_MODE] = enabled
        _isDarkMode.value = enabled
    }
}
