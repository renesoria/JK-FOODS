package com.ucb.food.country.presentation.state

import com.ucb.food.country.model.CountryModel

data class CountryState(val isLoading: Boolean = false,
                        val countries: List<CountryModel> = emptyList(),
                        val error: String? = null
)