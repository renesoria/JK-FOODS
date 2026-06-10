package com.ucb.food.restaurant.data.repository

import com.ucb.food.restaurant.data.dto.DishDto
import com.ucb.food.restaurant.data.dto.HotDealDto
import com.ucb.food.restaurant.data.dto.RestaurantDto
import com.ucb.food.restaurant.data.dto.ReviewDto
import com.ucb.food.restaurant.data.mapper.toDto
import com.ucb.food.restaurant.data.mapper.toEntity
import com.ucb.food.restaurant.data.mapper.toModel
import com.ucb.food.restaurant.domain.model.DishModel
import com.ucb.food.restaurant.domain.model.HotDealModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import com.ucb.food.restaurant.domain.model.ReviewModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository
import com.ucb.food.restaurant.data.service.NotificationService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RestaurantRepositoryImpl(
    private val restaurantDao: RestaurantDao,
    private val notificationService: NotificationService
) : RestaurantRepository {
    private val database = Firebase.database.reference()
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        syncFromRemote()
    }

    private fun syncFromRemote() {
        repositoryScope.launch {
            database.child("restaurants").valueEvents.collect { snapshot ->
                val dtos = snapshot.children.mapNotNull { it.value<RestaurantDto>() }
                restaurantDao.insertRestaurants(dtos.map { it.toEntity() })
            }
        }
    }

    override fun getRestaurants(): Flow<List<RestaurantModel>> {
        return restaurantDao.getAllRestaurants().map { list -> 
            list.map { it.toModel() }
        }
    }

    override fun getRestaurantById(id: String): Flow<RestaurantModel?> {
        return restaurantDao.getRestaurantById(id).map { it?.toModel() }
    }

    override fun getMenu(restaurantId: String): Flow<List<DishModel>> {
        repositoryScope.launch {
            database.child("menu").child(restaurantId).valueEvents.collect { snapshot ->
                val dtos = snapshot.children.mapNotNull { it.value<DishDto>() }
                restaurantDao.insertDishes(dtos.map { it.toEntity() })
            }
        }
        return restaurantDao.getMenuByRestaurant(restaurantId).map { list ->
            list.map { it.toModel() }
        }
    }

    override fun getHotDeals(): Flow<List<HotDealModel>> {
        return database.child("hot_deals").valueEvents.map { snapshot ->
            snapshot.children.mapNotNull { it.value<HotDealDto>().toModel() }
        }
    }

    override fun getReviews(branchId: String): Flow<List<ReviewModel>> {
        return database.child("reviews").child(branchId).valueEvents.map { snapshot ->
            snapshot.children.mapNotNull { child ->
                child.value<ReviewDto>().toModel()
            }
        }
    }

    override fun getUserReviews(userId: String): Flow<List<ReviewModel>> {
        return database.child("reviews").valueEvents.map { snapshot ->
            val allReviews = mutableListOf<ReviewModel>()
            snapshot.children.forEach { branchSnapshot ->
                branchSnapshot.children.forEach { reviewSnapshot ->
                    try {
                        val dto = reviewSnapshot.value<ReviewDto>()
                        if (dto.userId == userId) {
                            allReviews.add(dto.toModel())
                        }
                    } catch (e: Exception) {}
                }
            }
            allReviews
        }
    }

    override suspend fun addReview(review: ReviewModel) {
        val reviewDto = review.toDto()
        val branchId = review.branchId
        if (branchId.isBlank()) return 

        val reviewId = database.child("reviews").child(branchId).push().key ?: ""
        val finalReview = reviewDto.copy(id = reviewId, timestamp = 0L)
        database.child("reviews").child(branchId).child(reviewId).setValue(finalReview)
        
        updateRestaurantRating(review.restaurantId)
    }

    override suspend fun sendFiveStarNotification(restaurantName: String, dishName: String) {
        notificationService.sendFiveStarNotification(restaurantName, dishName)
    }

    private suspend fun updateRestaurantRating(restaurantId: String) {
        val restaurant = restaurantDao.getRestaurantById(restaurantId).first() ?: return
        val branches = restaurant.toModel().branches
        
        var totalStars = 0
        var reviewCount = 0
        
        branches.forEach { branch ->
            val snapshot = database.child("reviews").child(branch.id).valueEvents.first()
            snapshot.children.forEach { reviewSnap ->
                val rating = reviewSnap.child("rating").value<Int>()
                totalStars += rating
                reviewCount++
            }
        }
        
        if (reviewCount > 0) {
            val newRating = totalStars.toDouble() / reviewCount
            database.child("restaurants").child(restaurantId).child("overallRating").setValue(newRating)
        }
    }
}
