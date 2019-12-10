package com.example.navitime_challenge

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.*

import com.example.navitime_challenge.work.GetFreeTimeWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 */
class NavitimeApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
            setupRecurringWork()
        }
    }

    /**
     * Setup WorkManager background job to 'fetch' new network data daily.
     */
    private fun setupRecurringWork() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }
            .build()

        createNotificationChannel()

        Timber.d("WorkManager: Periodic Work request for sync is scheduled")
        val getFreeTimeWorkerPayload = workDataOf("clientID" to getString(R.string.default_web_client_id))
        val getFreeTimeWorker = PeriodicWorkRequestBuilder<GetFreeTimeWorker>(6, TimeUnit.HOURS)
            .setInputData(getFreeTimeWorkerPayload)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            GetFreeTimeWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            getFreeTimeWorker)
    }

    private fun createNotificationChannel() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "シフト提案の通知"
        val id = "default"
        val notifyDescription = "この通知の詳細情報を設定します"

        if (manager.getNotificationChannel(id) == null) {
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            channel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(channel)
        }

    }

}
