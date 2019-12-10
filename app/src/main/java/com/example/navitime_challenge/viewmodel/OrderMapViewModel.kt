package com.example.navitime_challenge.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.navitime_challenge.database.getRouteDatabase
import com.example.navitime_challenge.repository.RouteRepository
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class OrderMapViewModel(application: Application) : AndroidViewModel(application) {

    private val routesRepository = RouteRepository(getRouteDatabase(application))

    val routeList = routesRepository.routes

    private val viewModelJob = SupervisorJob()

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Factory for constructing OrderListViewModel with parameter
     */
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderMapViewModel::class.java)) {
                return OrderMapViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}