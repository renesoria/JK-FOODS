package com.ucb.food.restaurant.data.mapper

import com.ucb.food.restaurant.data.dto.BranchDto
import com.ucb.food.restaurant.data.dto.DishDto
import com.ucb.food.restaurant.data.dto.RestaurantDto
import com.ucb.food.restaurant.data.dto.ReviewDto
import com.ucb.food.restaurant.data.repository.DishEntity
import com.ucb.food.restaurant.data.repository.RestaurantEntity
import com.ucb.food.restaurant.domain.model.BranchModel
import com.ucb.food.restaurant.domain.model.DishModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import com.ucb.food.restaurant.domain.model.ReviewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun RestaurantDto.toModel() = RestaurantModel(
    id = id,
    name = name,
    logoUrl = logoUrl,
    overallRating = overallRating,
    description = description,
    branches = branches.map { it.toModel() }
)

fun RestaurantDto.toEntity() = RestaurantEntity(
    id = id,
    name = name,
    logoUrl = logoUrl,
    overallRating = overallRating,
    description = description,
    branchesJson = Json.encodeToString(branches)
)

fun RestaurantEntity.toModel() = RestaurantModel(
    id = id,
    name = name,
    logoUrl = logoUrl,
    overallRating = overallRating,
    description = description,
    branches = try {
        Json.decodeFromString<List<BranchDto>>(branchesJson).map { it.toModel() }
    } catch (e: Exception) {
        emptyList()
    }
)

fun BranchDto.toModel() = BranchModel(
    id = id,
    address = address,
    latitude = latitude,
    longitude = longitude,
    phone = phone
)

fun DishDto.toModel() = DishModel(
    id = id,
    restaurantId = restaurantId,
    name = name,
    price = price,
    category = category,
    imageUrl = imageUrl
)

fun DishDto.toEntity() = DishEntity(
    id = id,
    restaurantId = restaurantId,
    name = name,
    price = price,
    category = category,
    imageUrl = imageUrl
)

fun DishEntity.toModel() = DishModel(
    id = id,
    restaurantId = restaurantId,
    name = name,
    price = price,
    category = category,
    imageUrl = imageUrl
)

fun ReviewDto.toModel() = ReviewModel(
    id = id,
    userId = userId,
    userName = userName,
    userProfilePicture = userProfilePicture,
    restaurantId = restaurantId,
    branchId = branchId,
    rating = rating,
    comment = comment,
    photoUrl = photoUrl,
    consumedDishes = consumedDishes,
    hasParking = hasParking,
    hasWifi = hasWifi,
    acceptsDigitalPayment = acceptsDigitalPayment,
    isPetFriendly = isPetFriendly,
    hasKidsArea = hasKidsArea,
    isFastService = isFastService,
    isSlowService = isSlowService,
    isClean = isClean,
    isCozy = isCozy,
    isLoud = isLoud,
    isFairPrice = isFairPrice,
    isExpensive = isExpensive,
    timestamp = timestamp
)

fun ReviewModel.toDto() = ReviewDto(
    id = id,
    userId = userId,
    userName = userName,
    userProfilePicture = userProfilePicture,
    restaurantId = restaurantId,
    branchId = branchId,
    rating = rating,
    comment = comment,
    photoUrl = photoUrl,
    consumedDishes = consumedDishes,
    hasParking = hasParking,
    hasWifi = hasWifi,
    acceptsDigitalPayment = acceptsDigitalPayment,
    isPetFriendly = isPetFriendly,
    hasKidsArea = hasKidsArea,
    isFastService = isFastService,
    isSlowService = isSlowService,
    isClean = isClean,
    isCozy = isCozy,
    isLoud = isLoud,
    isFairPrice = isFairPrice,
    isExpensive = isExpensive,
    timestamp = timestamp
)
