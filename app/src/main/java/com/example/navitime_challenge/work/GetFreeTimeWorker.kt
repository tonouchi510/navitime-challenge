package com.example.navitime_challenge.work

import android.content.Context
import androidx.work.*
import com.example.navitime_challenge.R
import com.example.navitime_challenge.database.getGoogleAuthDatabase
import com.example.navitime_challenge.domain.GoogleAuthPayload
import com.example.navitime_challenge.network.GoogleCalendarApi
import com.example.navitime_challenge.repository.GoogleAuthRepository
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class GetFreeTimeWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params)  {

    companion object {
        const val WORK_NAME = "com.example.navitime_challenge.work.GetFreeTimeWorker"
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
        //val accessToken = repository.refreshAccessToken(p)
        val accessToken = "Bearer ya29.ImCzBxvcCulvcIhT41UtGQxqt-AK1Qg2Vt1VKlhBPKYQ5NJDrm_o1GkefoXzkUVK-_5_BD_gh6A2FX03i3JPg8gL6rYdWGCyW8rTfEOCVqilIlLVQUWsrgtT9SfAPHR0ZnQ"
        Timber.d(accessToken)

        //val response = GoogleCalendarApi.service.getEvents(accessToken, "navitime.challenge.user@gmail.com").await()
        //Timber.d(response.toString())

        // formatter
        val f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val f2 = DateTimeFormatter.ofPattern("HH:mm")

        var date = LocalDateTime.now()
        val startTime = date.format(f1) + "T" + date.format(f2)
        date = date.plusHours(1)
        val endTime = date.format(f1) + "T" + date.format(f2)

        val getOptimalShiftWorkerPayload = workDataOf(
            "startTime" to startTime,
            "endTime" to endTime
            )
        val getOptimalShiftWorker = OneTimeWorkRequestBuilder<GetOptimalShiftWorker>()
            .setInputData(getOptimalShiftWorkerPayload)
            .setInitialDelay(20, TimeUnit.SECONDS)
            .build()

        Timber.d("WorkManager: OneTime Work request is scheduled")
        WorkManager.getInstance().enqueue(getOptimalShiftWorker)

        return Result.success()
    }

}