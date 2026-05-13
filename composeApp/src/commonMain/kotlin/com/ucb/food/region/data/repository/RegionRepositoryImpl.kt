package com.ucb.food.region.data.repository

import com.ucb.food.firebase.getRemoteConfigString
import com.ucb.food.portafolio.data.datasource.FirebaseManager
import com.ucb.food.region.data.local.dao.RegionDao
import com.ucb.food.region.data.local.entity.RegionEntity
import com.ucb.food.region.domain.model.RegionData
import com.ucb.food.region.domain.repository.RegionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegionRepositoryImpl(
    private val regionDao: RegionDao,
    private val firebaseManager: FirebaseManager
) : RegionRepository {

    override fun getRegions(): Flow<List<RegionData>> {
        return regionDao.getAllRegions().map { entities ->
            entities.map { RegionData(it.id, it.name, it.description) }
        }
    }

    override suspend fun syncRegions() {
        // 1. Obtener regiones activas desde Remote Config
        val activeRegionsJson = getRemoteConfigString("active_regions")
        if (activeRegionsJson.isEmpty() || activeRegionsJson == "[]") return

        // Por simplicidad, asumimos que es una lista separada por comas "region1,region2"
        val activeRegions = activeRegionsJson.split(",").map { it.trim() }

        val regionEntities = mutableListOf<RegionEntity>()

        // 2. Descargar datos de Realtime Database para cada región
        activeRegions.forEach { regionId ->
            val regionName = firebaseManager.getData("regions/$regionId/name") ?: "Unknown"
            val regionDesc = firebaseManager.getData("regions/$regionId/description") ?: ""
            regionEntities.add(RegionEntity(regionId, regionName, regionDesc))
        }

        // 3. Guardar en Room
        if (regionEntities.isNotEmpty()) {
            regionDao.deleteAllRegions()
            regionDao.insertRegions(regionEntities)
        }
    }
}
