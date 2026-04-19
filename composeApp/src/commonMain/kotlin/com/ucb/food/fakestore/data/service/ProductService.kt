package com.ucb.food.fakestore.data.service

import com.ucb.food.fakestore.data.datasource.ProductRemoteDatasource
import com.ucb.food.fakestore.data.dto.ProductDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ProductService : ProductRemoteDatasource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    override suspend fun getProducts(): List<ProductDto> {
        return client.get("https://fakestoreapi.com/products").body()
    }
}
