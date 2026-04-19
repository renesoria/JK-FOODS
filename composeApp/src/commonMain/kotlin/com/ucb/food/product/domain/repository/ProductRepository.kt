package com.ucb.food.product.domain.repository

import com.ucb.food.product.domain.model.Product

interface ProductRepository {
    suspend fun findById(id: String): Product
}