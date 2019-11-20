package com.example.navitime_challenge.database


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.navitime_challenge.domain.Schedule
import com.google.api.services.calendar.Calendar
import org.joda.time.DateTime

/**
 * DatabaseSchedule represents a schedule entity in the database.
 */
@Entity
data class DatabaseSchedule constructor(
    @PrimaryKey
    val items: List<Calendar.CalendarList>

    /*
    val eventId: String,
    val updated: String,
    val title: String,
    val datetime: DateTime,
    val location: String
     */
)

fun List<DatabaseSchedule>.asDomainModel(): List<Schedule> {
    return map {
        Schedule(
            items = it.items
            /*
            eventId = it.eventId,
            title = it.title,
            datetime = it.datetime,
            location = it.location
             */
        )
    }
}
