package com.ucb.food.restaurant.data.repository

import com.ucb.food.restaurant.data.dto.DishDto
import com.ucb.food.restaurant.data.dto.RestaurantDto
import com.ucb.food.restaurant.data.dto.ReviewDto
import com.ucb.food.restaurant.data.mapper.toDto
import com.ucb.food.restaurant.data.mapper.toModel
import com.ucb.food.restaurant.domain.model.DishModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import com.ucb.food.restaurant.domain.model.ReviewModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RestaurantRepositoryImpl : RestaurantRepository {
    private val database = Firebase.database.reference()

    override fun getRestaurants(): Flow<List<RestaurantModel>> {
        return database.child("restaurants").valueEvents.map { snapshot ->
            snapshot.children.mapNotNull { child ->
                child.value<RestaurantDto>().toModel()
            }
        }
    }

    override fun getRestaurantDetails(id: String): Flow<RestaurantModel?> {
        return database.child("restaurants").child(id).valueEvents.map { snapshot ->
            if (snapshot.exists) snapshot.value<RestaurantDto>().toModel() else null
        }
    }

    override fun getMenu(restaurantId: String): Flow<List<DishModel>> {
        return database.child("menu").child(restaurantId).valueEvents.map { snapshot ->
            snapshot.children.mapNotNull { child ->
                child.value<DishDto>().toModel()
            }
        }
    }

    override fun getReviews(branchId: String): Flow<List<ReviewModel>> {
        return database.child("reviews").child(branchId).valueEvents.map { snapshot ->
            snapshot.children.mapNotNull { child ->
                child.value<ReviewDto>().toModel()
            }
        }
    }

    override suspend fun addReview(review: ReviewModel) {
        val reviewDto = review.toDto()
        val branchId = review.branchId
        val reviewId = database.child("reviews").child(branchId).push().key ?: ""
        val finalReview = reviewDto.copy(id = reviewId, timestamp = DateTimeUtils.now())
        database.child("reviews").child(branchId).child(reviewId).setValue(finalReview)
    }
}

// Utility to get timestamp
object DateTimeUtils {
    fun now(): Long = dev.gitlive.firebase.database.ServerValue.TIMESTAMP as? Long ?: 0L // Note: This might need adjustment based on how ServerValue works in KMP
}
