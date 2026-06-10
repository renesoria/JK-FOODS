import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppEventDao {
    @Insert
    suspend fun insert(event: AppEventEntity)

    @Query("SELECT * FROM AppEventEntity ORDER BY timestamp DESC")
    suspend fun getAllEvents(): List<AppEventEntity>
}
