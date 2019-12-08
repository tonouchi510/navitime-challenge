package com.example.navitime_challenge.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GoogleCalendarContainer(
    @Json(name = "items") val items: List<Event>,
    @Json(name = "updated") val updated: String?,
    @Json(name = "timeZone") val timeZone: String,
    @Json(name = "nextPageToken") val nextPageToken: String?,
    @Json(name = "nextSyncToken") val nextSyncToken: String)


data class Event(
    @Json(name = "start") val start: Time,
    @Json(name = "end") val end: Time
)


data class Time(
    @Json(name = "date") val date: String?,
    @Json(name = "dateTime") val dateTime: String?
)
