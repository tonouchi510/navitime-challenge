package com.example.navitime_challenge.ui

import android.location.Location
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
import com.example.navitime_challenge.adapter.ScheduleRecycleViewAdapter
import com.example.navitime_challenge.databinding.FragmentCalendarTestBinding
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.domain.Schedule
import com.example.navitime_challenge.viewmodel.ScheduleViewModel


class FragmentCalendarTest: Fragment(){

    private val TAG = "FragmentCalendarTest"

    private val viewModel: ScheduleViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, ScheduleViewModel.Factory(activity.application))
            .get(ScheduleViewModel::class.java)
    }


    private var recyclerViewAdapter: ScheduleRecycleViewAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCalendarTestBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_calendar_test, container, false)


        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        recyclerViewAdapter = ScheduleRecycleViewAdapter()
        /*
        binding.root.findViewById<RecyclerView>(R.id.calendarTestButton).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }*/

        // Observer for the network error.
        viewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.refreshDataFromRepository().observe(viewLifecycleOwner, Observer<List<Schedule>> { schedules ->
            schedules?.apply {
                recyclerViewAdapter?.schedule = Schedule("aa")
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
