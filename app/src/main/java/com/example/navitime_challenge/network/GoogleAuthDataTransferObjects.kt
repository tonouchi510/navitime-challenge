package com.example.navitime_challenge.network

import com.example.navitime_challenge.database.DatabaseAuth
import com.example.navitime_challenge.domain.Token
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoogleAuthContainer(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "expires_in") val expiresIn: Int,
    @Json(name = "refresh_token") val refreshToken: String = "",
    @Json(name = "scope") val scope: String?,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "id_token") val idToken: String?)


fun GoogleAuthContainer.asDomainModel(): Token {
    return Token(
        accessToken = accessToken,
        refreshToken = refreshToken)
}

fun GoogleAuthContainer.asDatabaseModel(): DatabaseAuth {
    return DatabaseAuth(
        id = "111111111",
        token = refreshToken
    )
}
