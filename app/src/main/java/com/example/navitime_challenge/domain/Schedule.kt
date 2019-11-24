package com.example.navitime_challenge.domain

import com.google.api.services.calendar.Calendar
import org.joda.time.DateTime


data class Schedule(
    val summary: String
    //val calendarId: String
    //val items: List<Calendar.CalendarList>
    /*
    val eventId: String,
    val title: String,
    val datetime: DateTime,
    val location: String
     */
)