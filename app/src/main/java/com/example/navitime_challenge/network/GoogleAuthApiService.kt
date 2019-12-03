package com.example.navitime_challenge.network

import com.google.api.client.json.Json
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

/**
 * A retrofit service to fetch a optimal shift.
 */

val header = "client_id=212813157070-cg70au51ob4fk4bvo89mt783bvvhkhkf.apps.googleusercontent.com client_secret=XyY_GCN8-kkTsZJwKjCOp0YT redirect_uri=http://localhost grant_type=authorization_code code=4/tgE6V0Q7vdweDldSf24rQZGY_IedBZSPhgzZHTQ2U8exLe7qcVVbQ7qbA2aJBQYYrby1IGddiA5RKYpebqpjAvY".toString().trim()

data class Token(var access_token: String,
                 var expires_in: Int,
                 var refresh_token: String,
                 var token_type: String)

interface GoogleAuthApiService {
    @GET("token")
    fun getAccessToken(@Header("client_id") client_id: String): Call<Token>
}

/**
 * Main entry point for network access. Call like `NavitimeApi.order.getOptimalShift()`
 */
object GoogleAuthApi {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(" https://accounts.google.com/o/oauth2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val service = retrofit.create(GoogleAuthApiService::class.java)

}