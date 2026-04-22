package com.ucb.food.config.data.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "app_config")
data class ConfigEntity(
    @PrimaryKey val id: String = "singleton_config",
    val welcomeMessage: String,
    val backgroundColor: String
)

@Dao
interface ConfigDao {
    @Query("SELECT * FROM app_config WHERE id = 'singleton_config'")
    fun getConfig(): Flow<ConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: ConfigEntity)
}
