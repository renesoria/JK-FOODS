package com.ucb.food.fakestore.data.mapper

import com.ucb.food.fakestore.data.dto.ProductDto
import com.ucb.food.fakestore.data.dto.RatingDto
import com.ucb.food.fakestore.domain.model.RatingModel
import com.ucb.food.fakestore.domain.model.StoreModel
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.category_electronics
import kotlinproject.composeapp.generated.resources.category_jewelery
import kotlinproject.composeapp.generated.resources.category_men_clothing
import kotlinproject.composeapp.generated.resources.category_women_clothing
import org.jetbrains.compose.resources.getString

suspend fun ProductDto.toModel() = StoreModel(
    id = id,
    title = title,
    price = price,
    description = description,
    category = translateCategory(category),
    image = image,
    rating = rating.toModel()
)

suspend fun translateCategory(category: String): String {
    return when (category.lowercase()) {
        "electronics" -> getString(Res.string.category_electronics)
        "jewelery" -> getString(Res.string.category_jewelery)
        "men's clothing" -> getString(Res.string.category_men_clothing)
        "women's clothing" -> getString(Res.string.category_women_clothing)
        else -> category
    }
}

fun RatingDto.toModel() = RatingModel(
    rate = rate,
    count = count
)
