package com.ucb.food.region.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.region.domain.repository.RegionRepository
import com.ucb.food.region.presentation.state.RegionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RegionViewModel(
    private val regionRepository: RegionRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<RegionUiState> = combine(
        regionRepository.getRegions(),
        _isLoading,
        _error
    ) { regions, isLoading, error ->
        RegionUiState(
            regions = regions,
            isLoading = isLoading,
            error = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RegionUiState(isLoading = true)
    )

    fun syncRegions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                regionRepository.syncRegions()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
