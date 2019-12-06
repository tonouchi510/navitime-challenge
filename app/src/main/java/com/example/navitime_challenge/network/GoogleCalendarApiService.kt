package com.example.navitime_challenge.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


/**
 * A retrofit service to fetch a schedule from Google Calendar.
 */

interface GoogleCalendarApiService {

    @GET("calendars/{calendarId}/events")
    fun getEvents(@Header( "Authorization") accessToken: String,
                  @Path("calendarId") calendarId: String): Deferred<GoogleCalendarContainer>
}


/**
 * Main entry point for network access. Call like `GoogleCalendarApi.order.getOptimalShift()`
 */
object GoogleCalendarApi {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/calendar/v3/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val service = retrofit.create(GoogleCalendarApiService::class.java)
}
