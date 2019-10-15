package com.example.navitime_challenge

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class FragmentTab03: Fragment() {

    private val TAG = "FragmentTab03"

    private lateinit var location: Location
    private lateinit var latitudeText: TextView
    private lateinit var longitudeText: TextView
    private lateinit var mActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity
        location = mActivity.location
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_tab03, container,false)

        latitudeText = view.findViewById(R.id.latitude_text)
        longitudeText = view.findViewById(R.id.longitude_text)

        latitudeText.text = location.latitude.toString()
        longitudeText.text = location.longitude.toString()
        return view
    }

}
