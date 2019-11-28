package com.example.navitime_challenge.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.navitime_challenge.database.getGoogleAuthDatabase
import com.example.navitime_challenge.domain.GoogleAuthPayload
import com.example.navitime_challenge.repository.GoogleAuthRepository
import timber.log.Timber


class RefreshTokenWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params)  {

    companion object {
        const val WORK_NAME = "com.example.navitime_challenge.work.RefreshTokenWorker"
    }

    override suspend fun doWork(): Result {
        Timber.d("Start RefreshTokenWorker")

        val repository = GoogleAuthRepository(getGoogleAuthDatabase(applicationContext))
        val clientID = inputData.getString("clientID")

        val p = GoogleAuthPayload(
            clientId = clientID!!,
            clientSecret = "YAtyWz_fZlJsV34WZC6L9DMh",
            grantType = "refresh_token",
            code = null
        )
        val accessToken = repository.refreshAccessToken(p)
        Timber.d(accessToken)

        return Result.success()
    }

}