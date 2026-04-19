package com.ucb.food.fakestore.presentation.state

sealed interface FakeStoreEvent {
    data object OnLoad : FakeStoreEvent
    data object OnRefresh : FakeStoreEvent
}