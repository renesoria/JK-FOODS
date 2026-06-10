package com.ucb.food.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.core.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val themeRepository: ThemeRepository
) : ViewModel() {
    val isDarkMode: StateFlow<Boolean> = themeRepository.isDarkMode

    fun toggleTheme() {
        viewModelScope.launch {
            themeRepository.toggleTheme()
        }
    }
}
