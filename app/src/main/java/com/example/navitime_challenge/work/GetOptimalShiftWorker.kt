package com.example.navitime_challenge.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.navitime_challenge.database.getDatabase
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.repository.OrdersRepository
import com.example.navitime_challenge.repository.RouteRepository
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
                    Timber.i(item.toString())
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

        return Result.success()
    }
}