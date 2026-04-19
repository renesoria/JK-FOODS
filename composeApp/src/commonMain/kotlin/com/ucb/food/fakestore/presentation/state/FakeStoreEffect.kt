package com.ucb.food.fakestore.presentation.state

sealed interface FakeStoreEffect {
    data class ShowError(val message: String) : FakeStoreEffect
}