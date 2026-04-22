import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ucb.food.config.data.datasource.local.ConfigDao
import com.ucb.food.config.data.datasource.local.ConfigEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [TodoEntity::class, ConfigEntity::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): TodoDao
    abstract fun getConfigDao(): ConfigDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
}
