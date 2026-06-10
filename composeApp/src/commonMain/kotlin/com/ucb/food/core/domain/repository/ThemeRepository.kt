package com.ucb.food.core.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {
    val isDarkMode: StateFlow<Boolean>
    suspend fun toggleTheme()
    suspend fun setDarkMode(enabled: Boolean)
}
