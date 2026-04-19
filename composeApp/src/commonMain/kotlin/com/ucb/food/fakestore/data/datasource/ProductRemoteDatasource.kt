package com.ucb.food.fakestore.data.datasource

import com.ucb.food.fakestore.data.dto.ProductDto

interface ProductRemoteDatasource {
    suspend fun getProducts(): List<ProductDto>
}
