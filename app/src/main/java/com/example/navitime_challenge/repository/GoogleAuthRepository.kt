package com.example.navitime_challenge.repository

import com.example.navitime_challenge.database.GoogleAuthDatabase
import com.example.navitime_challenge.domain.GoogleAuthPayload
import com.example.navitime_challenge.network.GoogleAuth
import com.example.navitime_challenge.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


class GoogleAuthRepository(private val database: GoogleAuthDatabase) {

    suspend fun getAccessToken(payload: GoogleAuthPayload) {
        Timber.d("get access token by auth code")

        withContext(Dispatchers.IO) {
            val response = GoogleAuth.authService.getAccessToken(
                clientId = payload.clientId,
                clientSecret = payload.clientSecret,
                grantType = payload.grantType!!,
                code = payload.code!!
            ).await()

            Timber.d(response.toString())

            if (response.refreshToken != null) {
                database.authDao.insertRefreshToken(response.asDatabaseModel())
                Timber.d(database.authDao.getRefreshToken().token)
            }
        }
    }

    suspend fun refreshAccessToken(payload: GoogleAuthPayload): String {
        Timber.d("refresh access token by refresh token")
        val refreshToken = database.authDao.getRefreshToken()

        val response = GoogleAuth.authService.refreshAccessToken(
            clientId = payload.clientId,
            clientSecret = payload.clientSecret,
            grantType = "refresh_token",
            refreshToken = refreshToken.token
        ).await()

        return response.accessToken
    }
}