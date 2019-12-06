package com.example.navitime_challenge.work

import android.content.Context
import androidx.work.*
import com.example.navitime_challenge.R
import com.example.navitime_challenge.database.getGoogleAuthDatabase
import com.example.navitime_challenge.domain.GoogleAuthPayload
import com.example.navitime_challenge.network.GoogleCalendarApi
import com.example.navitime_challenge.repository.GoogleAuthRepository
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class GetFreeTimeWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params)  {

    companion object {
        const val WORK_NAME = "com.example.navitime_challenge.work.GetFreeTimeWorker"
    }

    override suspend fun doWork(): Result {
        Timber.d("Start RefreshTokenWorker")

        /*
        val repository = GoogleAuthRepository(getGoogleAuthDatabase(applicationContext))
        val clientID = inputData.getString("clientID")

        val p = GoogleAuthPayload(
            clientId = clientID!!,
            clientSecret = "YAtyWz_fZlJsV34WZC6L9DMh",
            grantType = "refresh_token",
            code = null
        )
        val accessToken = repository.refreshAccessToken(p)

         */

        val accessToken = "Bearer ya29.ImC0BylGeVMbnCwSTIYH1QgeGaX9Hu3K02w0XoanRYyxENCrSfZ9obfZv006mhipuFb26pX714GV8ZABVXRpN04rIT6C2lv2Yr5Zkv5-55h4IjWQ6muBM8ud_HNytIkAFA0"
        Timber.d(accessToken)

        // formatter
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val f2 = DateTimeFormatter.ofPattern("HH:mm")

        var date = LocalDateTime.now()
        val timeMax = date.format(f1) + "T" + date.format(f2)
        date = date.plusDays(1)
        val timeMin = date.format(f1) + "T" + date.format(f2)


        val response = GoogleCalendarApi.service.getEvents(
            accessToken = accessToken,
            calendarId = "tonouchi27@gmail.com").await()

        val events = response.items
        Timber.d(events.toString())

        // 空き時間候補を取得
        var startTime = date
        val startTimes = mutableListOf<LocalDateTime>()
        val endTimes = mutableListOf<LocalDateTime>()
        var i = 0
        while (i < events.size) {
            if (i != 0) {
                startTime = LocalDateTime.ofInstant(
                    df.parse(events[i - 1].end.dateTime ?: events[i - 1].end.date!!)!!.toInstant(),
                    ZoneId.systemDefault())
            }

            val endTime = LocalDateTime.ofInstant(
                df.parse(events[i].start.dateTime ?: events[i].start.date!!)!!.toInstant(),
                ZoneId.systemDefault())

            if (diffDateTime(startTime, endTime) > 40) {
                // 40分以上の空きがあれば
                startTimes.add(startTime)
                endTimes.add(endTime)
            }

            i++
        }
        Timber.d(startTimes.toString())
        Timber.d(endTimes.toString())

        /*
        i = 0
        while (i < startTimes.size) {
            val delayTime = diffDateTime(date, startTimes[i])
            val getOptimalShiftWorkerPayload = workDataOf(
                "startTime" to startTimes[i],
                "endTime" to endTimes[i]
            )
            val getOptimalShiftWorker = OneTimeWorkRequestBuilder<GetOptimalShiftWorker>()
                .setInputData(getOptimalShiftWorkerPayload)
                .setInitialDelay(delayTime.toLong(), TimeUnit.MINUTES)
                .build()

            Timber.d("WorkManager: OneTime Work request is scheduled")
            WorkManager.getInstance().enqueue(getOptimalShiftWorker)

            i++
        }
        */

        val getOptimalShiftWorkerPayload = workDataOf(
            "startTime" to timeMax,
            "endTime" to timeMin
        )
        val getOptimalShiftWorker = OneTimeWorkRequestBuilder<GetOptimalShiftWorker>()
            .setInputData(getOptimalShiftWorkerPayload)
            .setInitialDelay(20, TimeUnit.SECONDS)
            .build()

        Timber.d("WorkManager: OneTime Work request is scheduled")
        WorkManager.getInstance().enqueue(getOptimalShiftWorker)


        return Result.success()
    }

    private fun diffDateTime(a: LocalDateTime, b: LocalDateTime): Int {
        if (b.dayOfMonth > a.dayOfMonth) {
            return (24 + b.hour - a.hour) * 60 + (b.minute - a.minute)
        } else {
            return (b.hour - a.hour) * 60 + (b.minute - a.minute)
        }
    }

}