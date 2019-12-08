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
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.domain.Route
import com.example.navitime_challenge.network.NavitimeApi
import com.example.navitime_challenge.network.NavitimeRouteContainer
import com.example.navitime_challenge.network.asDomainModel
import com.example.navitime_challenge.repository.OrdersRepository
import com.example.navitime_challenge.ui.MainActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class GetOptimalShiftWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params)  {

    companion object {
        const val WORK_NAME = "com.example.navitime_challenge.work.GetOptimalShiftWorker"
    }

    override suspend fun doWork(): Result {
        Timber.d("doWork!")

        // 空き時間
        val startTime = inputData.getString("startTime")!!
        val endTime = inputData.getString("endTime")!!

        // 現在地のgeopoint取得
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        val location = fusedLocationClient.lastLocation.await()
        val startLoc = "{\"lat\":" + location.latitude + ",\"lon\":" + location.longitude + "}"

        // OrderList取得
        val ordersRepository = OrdersRepository()
        val snapshot = try {
            ordersRepository.getSavedOrders().get().await()
        } catch (e: FirebaseFirestoreException) {
            // Handle exception
            Timber.e(e.message)
            return Result.failure()
        }
        val orderList: MutableList<Order> = mutableListOf()
        for (document in snapshot.documents) {
            val item = document.toObject(Order::class.java)
            orderList.add(item!!)
        }

        // シフト探索
        val result = SearchShift1(startLoc, orderList, startTime, endTime)
        shiftNotify(result.second.toString() + "件配達のシフトがあります。")
        Timber.d(result.second.toString() + "件配達のシフトがあります。")

        return Result.success()
    }

    suspend fun SearchShift1(startLoc: String, orderList: MutableList<Order>,
                             startTime: String, endTime: String): Pair<List<Route>, Int> {
        Timber.d("Get Optimal Shift by algorithm-1")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val groupedOrder = orderList.groupBy { it.shop!!.name }.toMutableMap()

        var routeList: NavitimeRouteContainer
        var selectedOrders: List<Order>

        while (true) {
            selectedOrders = groupedOrder.values.maxBy { v -> v.size }!!

            val shop = "{\"name\":\"" + selectedOrders[0].shop!!.name +
                    "\",\"lat\":\"" + selectedOrders[0].shop!!.geopoint!!.latitude +
                    "\",\"lon\":\"" + selectedOrders[0].shop!!.geopoint!!.longitude + "\"}"

            val via = selectedOrders.map { order ->
                val geopoint = order.user_info!!.geopoint!!
                "{\"name\":\"" + order.user_info.address +
                        "\",\"lat\":\"" + geopoint.latitude +
                        "\",\"lon\":\"" + geopoint.longitude +
                        "\",\"stay-time\":\"5\"}"
            }

            routeList = NavitimeApi.service.getOptimalShift(
                start = startLoc,
                shop = shop,
                via = via.toString(),
                starttime = startTime
            ).await()

            val goalTime = routeList.goaltime
            Timber.d(goalTime)

            if (df.parse(goalTime)!! < df.parse(endTime)) break
            groupedOrder.remove(selectedOrders[0].shop!!.name)

        }
        return Pair(routeList.asDomainModel(), selectedOrders.size)

    }

    private fun shiftNotify(message: String) {

        val pendingIntent = PendingIntent.getActivity(applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java), PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle("NAVITIME-CHALLENGE")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(1, notification)
    }

    suspend fun <T> Task<T>.await(): T {
        // fast path
        if (isComplete) {
            val e = exception
            return if (e == null) {
                if (isCanceled) {
                    throw CancellationException("Task $this was cancelled normally.")
                } else {
                    @Suppress("UNCHECKED_CAST")
                    result as T
                }
            } else {
                throw e
            }
        }

        return suspendCancellableCoroutine { cont ->
            addOnCompleteListener {
                val e = exception
                if (e == null) {
                    @Suppress("UNCHECKED_CAST")
                    if (isCanceled) cont.cancel() else cont.resume(result as T)
                } else {
                    cont.resumeWithException(e)
                }
            }
        }
    }

}