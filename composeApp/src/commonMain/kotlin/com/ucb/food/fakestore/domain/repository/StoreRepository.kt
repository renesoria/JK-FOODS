package com.ucb.food.fakestore.domain.repository

import com.ucb.food.fakestore.domain.model.StoreModel

interface StoreRepository {
    suspend fun getProducts(): List<StoreModel>
}
