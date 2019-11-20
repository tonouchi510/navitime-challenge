package com.example.navitime_challenge.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ScheduleDao {
    @Query("select * from databaseschedule")
    fun getVideos(): LiveData<List<DatabaseSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( videos: List<DatabaseSchedule>)
}

@Database(entities = [DatabaseSchedule::class], version = 1)
abstract class SchedulesDatabase: RoomDatabase() {
    abstract val videoDao: ScheduleDao
}

private lateinit var INSTANCE: SchedulesDatabase

fun getDatabase(context: Context): SchedulesDatabase {
    synchronized(SchedulesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                SchedulesDatabase::class.java,
                "schedules").build()
        }
    }
    return INSTANCE
}

