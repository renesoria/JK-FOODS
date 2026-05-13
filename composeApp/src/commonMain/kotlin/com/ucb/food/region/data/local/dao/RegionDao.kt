package com.ucb.food.region.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ucb.food.region.data.local.entity.RegionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegions(regions: List<RegionEntity>)

    @Query("SELECT * FROM RegionEntity")
    fun getAllRegions(): Flow<List<RegionEntity>>

    @Query("DELETE FROM RegionEntity")
    suspend fun deleteAllRegions()
}
