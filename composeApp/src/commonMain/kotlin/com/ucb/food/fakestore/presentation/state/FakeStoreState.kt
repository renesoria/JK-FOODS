package com.ucb.food.fakestore.presentation.state

import com.ucb.food.fakestore.domain.model.StoreModel

data class FakeStoreState(val isLoading: Boolean = false,
                          val stores: List<StoreModel> = emptyList(),
                          val error: String? = null
)