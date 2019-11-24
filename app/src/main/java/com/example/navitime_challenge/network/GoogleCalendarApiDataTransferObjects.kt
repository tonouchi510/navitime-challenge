package com.example.navitime_challenge.network

//import com.example.navitime_challenge.domain.Route
import android.telephony.gsm.SmsMessage
import com.example.navitime_challenge.database.DatabaseSchedule
import com.example.navitime_challenge.domain.Schedule
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class GoogleCalendarContainer(val schedules: List<NetworkSchedule>)


@JsonClass(generateAdapter = true)
data class NetworkSchedule(
    val summary: String
    //val items: List<Calendar.CalendarList>
    /*
    val kind: String,
    val etag: String?,
    val summary: String,
    val description: String,
    val updated: DateTime,
    val timeZone: String,
    val accessRole: String,
    val defaultReminders: List<String>,
    val nextPageToken: String,
    val nextSyncToken: String,
    val items: Calendar.id
     */
)



/**
 * Convert Network results to database objects
 */
fun GoogleCalendarContainer.asDatabaseModel(): List<DatabaseSchedule> {
    return schedules.map {
        DatabaseSchedule(
            summary = it.summary
        )
    }
}