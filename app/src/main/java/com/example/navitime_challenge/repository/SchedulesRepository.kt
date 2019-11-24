package com.example.navitime_challenge.repository

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.navitime_challenge.database.DatabaseSchedule
import com.example.navitime_challenge.database.SchedulesDatabase
import com.example.navitime_challenge.database.asDomainModel
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.domain.Schedule
import com.example.navitime_challenge.network.GoogleCalendarApi
import com.example.navitime_challenge.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class SchedulesRepository(private val database: SchedulesDatabase) {

    private val TAG = "ScheduleRepository"

    val schedules: LiveData<List<Schedule>> =
        Transformations.map(database.ScheduleDao.getSchedules()) {
            it.asDomainModel()
        }

    suspend fun refreshSchedules() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh schedules is called");
            val getCalendarId = GoogleCalendarApi.service.getCalendarId().await()
            Timber.d("CalendarId:"+getCalendarId)
            database.ScheduleDao.insertAll(getCalendarId.asDatabaseModel())
        }
    }

    suspend fun getSchedule(): LiveData<List<Schedule>> {
        withContext(Dispatchers.IO) {
            Timber.d("get schedules is called")
            val schedules = database.ScheduleDao.getSchedules()
        }
        return schedules
    }
}
