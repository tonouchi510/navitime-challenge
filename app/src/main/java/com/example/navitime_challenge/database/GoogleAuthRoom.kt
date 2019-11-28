package com.example.navitime_challenge.database

import android.content.Context
import androidx.room.*

@Dao
interface GoogleAuthDao {

    @Query("select * from auth")
    fun getRefreshToken(): DatabaseAuth

    @Insert
    fun insertRefreshToken(auth: DatabaseAuth)

    @Update
    fun updateRefreshToken(auth: DatabaseAuth)

    @Query("delete from auth")
    fun clear()
}


@Database(entities = [DatabaseAuth::class], version = 1)
abstract class GoogleAuthDatabase: RoomDatabase() {
    abstract val authDao: GoogleAuthDao
}

private lateinit var INSTANCE: GoogleAuthDatabase

fun getGoogleAuthDatabase(context: Context): GoogleAuthDatabase {
    synchronized(RoutesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    GoogleAuthDatabase::class.java,
                    "auth").build()
        }
    }
    return INSTANCE
}
