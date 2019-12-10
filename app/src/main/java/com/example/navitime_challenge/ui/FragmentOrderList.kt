package com.example.navitime_challenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navitime_challenge.R
import com.example.navitime_challenge.adapter.OrderRecyclerViewAdapter
import com.example.navitime_challenge.databinding.FragmentOrderlistBinding
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.viewmodel.OrderListViewModel

class FragmentOrderList: Fragment(){

    private val viewModel: OrderListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, OrderListViewModel.Factory(activity.application))
            .get(OrderListViewModel::class.java)
    }

    private var recyclerViewAdapter: OrderRecyclerViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentOrderlistBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_orderlist, container, false)

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        recyclerViewAdapter = OrderRecyclerViewAdapter()

        binding.root.findViewById<RecyclerView>(R.id.order_recyclerview).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }

        // Observer for the network error.
        viewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getSavedOrdersFromRepository().observe(viewLifecycleOwner, Observer<List<Order>> { orders ->
            orders?.apply {
                recyclerViewAdapter?.orders = orders
            }
        })
    }

    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

}

