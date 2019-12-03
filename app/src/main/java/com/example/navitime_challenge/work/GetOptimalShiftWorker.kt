package com.example.navitime_challenge.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.domain.Route
import com.example.navitime_challenge.network.NavitimeApi
import com.example.navitime_challenge.network.asDomainModel
import com.example.navitime_challenge.repository.OrdersRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class GetOptimalShiftWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params)  {

    companion object {
        const val WORK_NAME = "com.example.navitime_challenge.work.GetOptimalShiftWorker"
    }

    override suspend fun doWork(): Result {
        Timber.d("doWork!")

        val startTime = inputData.getString("startTime")!!
        val endTime = inputData.getString("endTime")!!

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
        Timber.d(orderList[2].shop!!.geopoint.toString())

        val startLoc = "{\"lat\":35.483135,\"lon\":139.613108}"

        val proposedShift = SearchShift1(startLoc, orderList, startTime, endTime)
        Timber.d("-----------------------------")
        Timber.d(proposedShift.toString())
        Timber.d("-----------------------------")

        return Result.success()
    }

    suspend fun SearchShift1(startLoc: String, orderList: MutableList<Order>,
                             startTime: String, endTime: String): List<Route>? {
        Timber.d("Get Optimal Shift by algorithm-1")
        val groupedOrder = orderList.groupBy { it.shop!!.name }
        val selectedOrders = groupedOrder.values.maxBy { v -> v.size } ?: return null

        val shop = "{\"lat\":\"" + selectedOrders[0].shop!!.geopoint!!.latitude +
                "\",\"lon\":\"" + selectedOrders[0].shop!!.geopoint!!.longitude + "\"}"

        val via = selectedOrders.map { order ->
            val geopoint = order.user_info!!.geopoint!!
            "{\"lat\":\"" + geopoint.latitude + "\",\"lon\":\"" + geopoint.longitude + "\",\"stay-time\":\"5\"}"
        }
        Timber.d(via.toString())

        val routeList = NavitimeApi.service.getOptimalShift(
            start = startLoc,
            shop = shop,
            via = via.toString(),
            starttime = startTime,
            endtime = endTime
        ).await()

        return routeList.asDomainModel()
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