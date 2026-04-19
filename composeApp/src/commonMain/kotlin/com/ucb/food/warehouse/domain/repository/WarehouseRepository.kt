package com.ucb.food.warehouse.domain.repository

interface WarehouseRepository {
    suspend fun verifyStock(productId: String): Boolean
    suspend fun reduceProduct(productId: String)
}