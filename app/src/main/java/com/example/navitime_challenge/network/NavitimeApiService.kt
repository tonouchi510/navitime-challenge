package com.example.navitime_challenge.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

// example: "getRoute?start={\"lat\":35.706822,\"lon\":139.813956}&shop={\"lat\":35.655392,\"lon\":139.748642}&starttime=2018-05-01T13:00&endtime=2018-05-01T15:55&via=[{\"name\":\"東京都台東区秋葉原\",\"lat\":\"35.702069\",\"lon\":\"139.775327\",\"stay-time\":\"30\"},{\"name\":\"代々木公園\",\"lat\":\"35.662141\",\"lon\":\"139.771023\",\"spot\":\"02301-1300514\"},{\"stay-time\":\"30\",\"name\":\"東京\",\"node\":\"00006668\"}]"

/**
 * A retrofit service to fetch a optimal shift.
 */
interface NavitimeApiService {
    @GET("getRoute")
    fun getOptimalShift(
        @Query("start") start: String,
        @Query("shop") shop: String,
        @Query("starttime") starttime: String,
        @Query("endtime") endtime: String,
        @Query("via") via: String
    ): Deferred<NavitimeRouteContainer>
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

    val service = retrofit.create(NavitimeApiService::class.java)

}
