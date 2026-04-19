package com.ucb.food.fakestore.domain.usecase

import com.ucb.food.fakestore.domain.model.StoreModel
import com.ucb.food.fakestore.domain.repository.StoreRepository

class GetStoreProductsUseCase(private val repository: StoreRepository) {
    suspend fun invoke(): List<StoreModel> = repository.getProducts()
}
