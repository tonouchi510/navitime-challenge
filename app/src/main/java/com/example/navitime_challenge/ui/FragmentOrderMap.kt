package com.example.navitime_challenge.ui

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.navitime_challenge.R
import com.example.navitime_challenge.databinding.FragmentOrdermapBinding


class FragmentOrderMap: Fragment() {

    private lateinit var location: Location
    private lateinit var latitudeText: TextView
    private lateinit var longitudeText: TextView

    private lateinit var mapView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentOrdermapBinding>(inflater,
            R.layout.fragment_ordermap, container, false)

        latitudeText = binding.latitudeText
        longitudeText = binding.longitudeText
        latitudeText.text = location.latitude.toString()
        longitudeText.text = location.longitude.toString()

        mapView = binding.mapWebview
        mapView.webViewClient = WebViewClient()
        mapView.settings.javaScriptEnabled = true

        val query: String = "latitude=" + location.latitude + "&longitude=" + location.longitude
        mapView.loadUrl("https://asia-northeast1-navitime-challenge.cloudfunctions.net/getMap?$query")

        return binding.root
    }

}
