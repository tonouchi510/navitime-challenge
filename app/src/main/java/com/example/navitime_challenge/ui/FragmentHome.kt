package com.example.navitime_challenge.ui

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.navitime_challenge.R
import com.example.navitime_challenge.databinding.FragmentHomeBinding

class FragmentHome: Fragment(){

    private val TAG = "----------------------------------------------------"
    private lateinit var location: Location

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)

        binding.freeTime.text = "17:30 ~ 18:20"


        setHasOptionsMenu(true)
        return binding.root
    }

}
