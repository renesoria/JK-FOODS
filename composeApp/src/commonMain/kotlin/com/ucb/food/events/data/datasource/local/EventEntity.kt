package com.ucb.food.events.data.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert

@Entity(tableName = "event_logs")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val eventType: String // "OPEN" o "CLOSE"
)

@Dao
interface EventDao {
    @Insert
    suspend fun insertEvent(event: EventEntity)
}
