import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val eventType: String // "OPEN" o "CLOSE"
)
