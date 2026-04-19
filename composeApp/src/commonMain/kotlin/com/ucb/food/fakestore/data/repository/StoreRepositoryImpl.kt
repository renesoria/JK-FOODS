package com.ucb.food.fakestore.data.repository

import com.ucb.food.fakestore.data.datasource.ProductRemoteDatasource
import com.ucb.food.fakestore.data.mapper.toModel
import com.ucb.food.fakestore.domain.model.StoreModel
import com.ucb.food.fakestore.domain.repository.StoreRepository

class StoreRepositoryImpl(
    val remote: ProductRemoteDatasource
) : StoreRepository {
    override suspend fun getProducts(): List<StoreModel> {
        return remote.getProducts().map { it.toModel() }
    }
}
