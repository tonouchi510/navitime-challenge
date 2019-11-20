package com.example.navitime_challenge.repository

import com.example.navitime_challenge.database.SchedulesDatabase
import com.example.navitime_challenge.network.GoogleCalendarApi
import com.example.navitime_challenge.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class SchedulesRepository(private val database: SchedulesDatabase) {
    suspend fun refreshSchedules() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh schedules is called");
            val playlist = GoogleCalendarApi.service.getCalendarId().await()
            database.ScheduleDao.insertAll(playlist.asDatabaseModel())
        }
    }
}