package com.ucb.food.region.domain.repository

import com.ucb.food.region.domain.model.RegionData
import kotlinx.coroutines.flow.Flow

interface RegionRepository {
    fun getRegions(): Flow<List<RegionData>>
    suspend fun syncRegions()
}
