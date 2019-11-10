package com.example.navitime_challenge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.navitime_challenge.network.NavitimeApi
import kotlinx.coroutines.*

enum class NavitimeApiStatus { LOADING, ERROR, DONE }

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<NavitimeApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<NavitimeApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of MarsProperty
    // with new values
    private val _routes = MutableLiveData<String>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val routes: LiveData<String>
        get() = _routes

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        // 後でTimerとか使って定期実行できるようにする
        getOptimalShift()
    }

    private fun getOptimalShift() {
        viewModelScope.launch {
            // Get the Deferred object for our Retrofit request
            var getOptimalShiftDeferred = NavitimeApi.service.getOptimalShift()
            try {
                _status.value = NavitimeApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val response = getOptimalShiftDeferred.await()
                Log.w("-------------------------------------", response.toString())
                _status.value = NavitimeApiStatus.DONE
                _routes.value = response.toString()
            } catch (e: Exception) {
                Log.w("-------------------------------------", e.toString())
                _status.value = NavitimeApiStatus.ERROR
                _routes.value = ""
            }
        }
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
