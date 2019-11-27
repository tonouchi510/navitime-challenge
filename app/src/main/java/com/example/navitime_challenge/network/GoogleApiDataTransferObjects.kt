package com.example.navitime_challenge.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoogleAuthContainer(val authResult: NetworkAuth)


@JsonClass(generateAdapter = true)
data class NetworkAuth(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
    val scope: String,
    val tokenType: String,
    val idToken: String)

fun GoogleAuthContainer.asDomainModel(): String {
    return authResult.accessToken
}
