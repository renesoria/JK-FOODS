package com.ucb.food.region.presentation.state

import com.ucb.food.region.domain.model.RegionData

data class RegionUiState(
    val regions: List<RegionData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
