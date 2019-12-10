package com.example.navitime_challenge.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.navitime_challenge.database.getRouteDatabase
import com.example.navitime_challenge.repository.RouteRepository
import kotlinx.coroutines.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val routeRepository = RouteRepository(getRouteDatabase(application))

    val routes = routeRepository.routes

    private val viewModelJob = SupervisorJob()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
