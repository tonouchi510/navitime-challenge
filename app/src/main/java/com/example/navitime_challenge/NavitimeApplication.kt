package com.example.navitime_challenge

import android.app.Application
import android.os.Build
import androidx.work.*

import com.example.navitime_challenge.work.GetOptimalShiftWorker
import com.example.navitime_challenge.work.RefreshTokenWorker
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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
    private lateinit var user: GoogleSignInAccount
    private lateinit var accessToken: String

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


        Timber.d("WorkManager: One time Work request")
        val refreshTokenWorkerPayload = workDataOf("clientID" to getString(R.string.default_web_client_id))
        val refreshTokenWorker = PeriodicWorkRequestBuilder<RefreshTokenWorker>(1, TimeUnit.HOURS)
            .setInputData(refreshTokenWorkerPayload)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshTokenWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            refreshTokenWorker)

        Timber.d("WorkManager: Periodic Work request for sync is scheduled")
        val repeatingRequest = PeriodicWorkRequestBuilder<GetOptimalShiftWorker>(3, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            GetOptimalShiftWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }

    fun setAuthUser(account: GoogleSignInAccount) {
        user = account
        Timber.d(user.id)
    }

    fun setAccessToken(token: String) {
        accessToken = token
    }
}
