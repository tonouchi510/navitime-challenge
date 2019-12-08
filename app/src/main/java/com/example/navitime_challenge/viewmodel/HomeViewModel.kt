package com.example.navitime_challenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.navitime_challenge.database.getRouteDatabase
import com.example.navitime_challenge.domain.RoutePayload
import com.example.navitime_challenge.repository.RouteRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val routeRepository = RouteRepository(getRouteDatabase(application))

    val routes = routeRepository.routes

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

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
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        refreshOptimalShift()
    }

    private fun refreshOptimalShift() {
        viewModelScope.launch {
            try {
                val p = RoutePayload(
                    start = "{\"lat\":35.706822,\"lon\":139.813956}",
                    shop = "{\"lat\":35.655392,\"lon\":139.748642}",
                    starttime = "2018-05-01T13:00",
                    via = "[{\"name\":\"東京都台東区秋葉原\",\"lat\":\"35.702069\",\"lon\":\"139.775327\",\"stay-time\":\"30\"},{\"name\":\"代々木公園\",\"lat\":\"35.662141\",\"lon\":\"139.771023\",\"spot\":\"02301-1300514\"},{\"stay-time\":\"30\",\"name\":\"東京\",\"node\":\"00006668\"}]"
                )
                routeRepository.refreshRoutes(p)
                Timber.d(routes.toString())
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(routes.value!!.isEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    /**
    * Resets the network error flag.
    */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

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
