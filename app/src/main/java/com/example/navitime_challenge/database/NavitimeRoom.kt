package com.example.navitime_challenge.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RouteDao {

    @Query("select * from routes")
    fun getRoutes(): LiveData<List<DatabaseRoute>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(routes: List<DatabaseRoute>)

    @Query("delete from routes")
    fun clear()
}


@Database(entities = [DatabaseRoute::class], version = 1)
abstract class RoutesDatabase: RoomDatabase() {
    abstract val routeDao: RouteDao
}

private lateinit var INSTANCE: RoutesDatabase

fun getDatabase(context: Context): RoutesDatabase {
    synchronized(RoutesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    RoutesDatabase::class.java,
                    "routes").build()
        }
    }
    return INSTANCE
}
