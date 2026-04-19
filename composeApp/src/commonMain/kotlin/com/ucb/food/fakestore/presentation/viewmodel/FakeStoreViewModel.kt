package com.ucb.food.fakestore.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.fakestore.domain.usecase.GetStoreProductsUseCase
import com.ucb.food.fakestore.presentation.state.FakeStoreEffect
import com.ucb.food.fakestore.presentation.state.FakeStoreEvent
import com.ucb.food.fakestore.presentation.state.FakeStoreState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FakeStoreViewModel(
    private val getStoreProductsUseCase: GetStoreProductsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(FakeStoreState())
    val state = _state.asStateFlow()

    private val _effect = Channel<FakeStoreEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(FakeStoreEvent.OnLoad)
    }

    fun onEvent(event: FakeStoreEvent) {
        when (event) {
            FakeStoreEvent.OnLoad -> loadStore()
            FakeStoreEvent.OnRefresh -> loadStore()
        }
    }

    private fun loadStore() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val products = getStoreProductsUseCase.invoke()
                _state.update {
                    it.copy(
                        isLoading = false,
                        stores = products
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
}
