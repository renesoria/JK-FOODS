import androidx.room.*
import com.ucb.food.restaurant.data.repository.DishEntity
import com.ucb.food.restaurant.data.repository.RestaurantDao
import com.ucb.food.restaurant.data.repository.RestaurantEntity
import com.ucb.food.login.data.repository.UserDao
import com.ucb.food.login.data.repository.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [TodoEntity::class, AppEventEntity::class, ConfigEntity::class, UserEntity::class, RestaurantEntity::class, DishEntity::class], version = 6)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): TodoDao
    abstract fun getEventDao(): AppEventDao
    abstract fun getConfigDao(): ConfigDao
    abstract fun getUserDao(): UserDao
    abstract fun getRestaurantDao(): RestaurantDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(androidx.sqlite.driver.bundled.BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
