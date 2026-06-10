import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveConfig(config: ConfigEntity)

    @Query("SELECT * FROM ConfigEntity WHERE `key` = :key LIMIT 1")
    suspend fun getConfig(key: String): ConfigEntity?

    @Query("SELECT * FROM ConfigEntity")
    fun getAllConfigsFlow(): Flow<List<ConfigEntity>>
}
