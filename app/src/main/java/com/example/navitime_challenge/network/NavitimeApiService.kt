package com.example.navitime_challenge.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

/**
 * A retrofit service to fetch a optimal shift.
 */
interface NavitimeApiService {
    @GET("getRoute?start={\"lat\":35.706822,\"lon\":139.813956}&shop={\"lat\":35.655392,\"lon\":139.748642}&time=2018-05-01T13:00&via=[{%27name%27:%27東京都台東区秋葉原%27,%27lat%27:%2735.702069%27,%27lon%27:%27139.775327%27,%27stay-time%27:%2730%27},{%27name%27:%27代々木公園%27,%27lat%27:%2735.662141%27,%27lon%27:%27139.771023%27,%27spot%27:%2702301-1300514%27},{%27stay-time%27:%2730%27,%27name%27:%27東京%27,%27node%27:%2700006668%27}]")
    fun getOptimalShift(): Deferred<NavitimeRouteContainer>
}

/**
 * Main entry point for network access. Call like `NavitimeApi.order.getOptimalShift()`
 */
object NavitimeApi {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-northeast1-navitime-challenge.cloudfunctions.net/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val service: NavitimeApiService by lazy { retrofit.create(NavitimeApiService::class.java) }

}
