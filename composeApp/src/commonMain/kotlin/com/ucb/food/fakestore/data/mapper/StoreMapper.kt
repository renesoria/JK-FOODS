package com.ucb.food.fakestore.data.mapper

import com.ucb.food.fakestore.data.dto.ProductDto
import com.ucb.food.fakestore.data.dto.RatingDto
import com.ucb.food.fakestore.domain.model.RatingModel
import com.ucb.food.fakestore.domain.model.StoreModel

fun ProductDto.toModel() = StoreModel(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rating = rating.toModel()
)

fun RatingDto.toModel() = RatingModel(
    rate = rate,
    count = count
)
