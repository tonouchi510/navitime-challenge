package com.example.navitime_challenge.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.navitime_challenge.R
import com.example.navitime_challenge.databinding.FragmentOrdermapBinding


class FragmentOrderMap: Fragment() {

    private lateinit var mapView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentOrdermapBinding>(inflater,
            R.layout.fragment_ordermap, container, false)

        mapView = binding.mapWebview
        mapView.webViewClient = WebViewClient()
        mapView.settings.javaScriptEnabled = true

        val query = "latitude=35.689634&longitude=139.692101&shop=[{\"lat\":\'35.689634\',\"lon\":\'139.692101\'},{\"lat\":\'35.701429\',\"lon\":\'139.700003\'}]"
        mapView.loadUrl("https://asia-northeast1-navitime-challenge.cloudfunctions.net/getMap?$query")

        return binding.root
    }

}
