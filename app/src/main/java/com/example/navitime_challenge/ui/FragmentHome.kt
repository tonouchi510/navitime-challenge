package com.example.navitime_challenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.navitime_challenge.R
import com.example.navitime_challenge.databinding.FragmentHomeBinding
import com.example.navitime_challenge.viewmodel.HomeViewModel

class FragmentHome: Fragment(){

    private val TAG = "FragmentHome"

    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, HomeViewModel.Factory(activity.application))
            .get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.freeTime.text = "17:30 ~ 18:20"

        setHasOptionsMenu(true)
        return binding.root
    }

}
