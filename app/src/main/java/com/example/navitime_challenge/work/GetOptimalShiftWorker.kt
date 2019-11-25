package com.example.navitime_challenge.work

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.navitime_challenge.R
import com.example.navitime_challenge.database.getDatabase
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.repository.OrdersRepository
import com.example.navitime_challenge.repository.RouteRepository
import com.example.navitime_challenge.ui.MainActivity
import retrofit2.HttpException
import timber.log.Timber


class GetOptimalShiftWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params)  {

    companion object {
        const val WORK_NAME = "com.example.navitime_challenge.work.GetOptimalShiftWorker"
    }

    override suspend fun doWork(): Result {
        val ordersRepository = OrdersRepository()
        ordersRepository.getSavedOrders().get()
            .addOnSuccessListener { documemts ->
                val orders: MutableList<Order> = mutableListOf()
                for (doc in documemts) {
                    val item = doc.toObject(Order::class.java)
                    orders.add(item)
                }
            }
            .addOnFailureListener { exception ->
                Timber.d("Error getting documents: ", exception)
            }

        val database = getDatabase(applicationContext)
        val routeRepository = RouteRepository(database)

        try {
            routeRepository.refreshRoutes()
            Timber.d("WorkManager: Work request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java), PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle(routeRepository.routes.value?.get(0)?.name)
            .setContentText(routeRepository.routes.value.toString())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(1, notification)

        return Result.success()
    }

    /*

    /** Authorizes the installed application to access user's protected data.  */
    @Throws(Exception::class)
    private fun authorize(): Credential {
        // load client secrets
        val clientSecrets = GoogleClientSecrets.load(
            JSON_FACTORY,
            InputStreamReader(CalendarSample::class.java!!.getResourceAsStream("/client_secrets.json"))
        )
        // set up authorization code flow
        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, clientSecrets,
            Collections.singleton(CalendarScopes.CALENDAR)
        ).setDataStoreFactory(dataStoreFactory)
            .build()
        // authorize
        return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
    }

     */
}