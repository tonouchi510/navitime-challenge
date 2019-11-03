package com.example.navitime_challenge.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.repository.OrdersRepository
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class OrderListViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "OrderListViewModel"

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val ordersRepository = OrdersRepository()

    val orderList: MutableLiveData<List<Order>> = MutableLiveData()

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
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


    fun getSavedOrdersFromRepository(): LiveData<List<Order>> {

        viewModelScope.launch {
            ordersRepository.getSavedOrders().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    orderList.value = null
                    _eventNetworkError.value = true
                    return@EventListener
                }

                var savedOrders: MutableList<Order> = mutableListOf()
                for (doc in value!!) {
                    var item = doc.toObject(Order::class.java)
                    savedOrders.add(item)
                }
                orderList.value = savedOrders
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            })
        }
        return orderList
    }


    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }


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
            if (modelClass.isAssignableFrom(OrderListViewModel::class.java)) {
                return OrderListViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}