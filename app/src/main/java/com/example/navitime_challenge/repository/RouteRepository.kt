package com.example.navitime_challenge.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.navitime_challenge.database.RoutesDatabase
import com.example.navitime_challenge.database.asDomainModel
import com.example.navitime_challenge.domain.Route
import com.example.navitime_challenge.network.NavitimeApi
import com.example.navitime_challenge.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


class RouteRepository(private val database: RoutesDatabase) {

    val routes: LiveData<List<Route>> = Transformations.map(database.routeDao.getRoutes()) {
        it.asDomainModel()
    }

    suspend fun refreshRoutes() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh routes of optimal shift is called")
            val routelist = NavitimeApi.service.getOptimalShift().await()
            database.routeDao.insertAll(routelist.asDatabaseModel())
        }
    }
}