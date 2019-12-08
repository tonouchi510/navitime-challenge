package com.example.navitime_challenge.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.navitime_challenge.R
import com.example.navitime_challenge.databinding.FragmentHomeBinding
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.domain.Route
import com.example.navitime_challenge.viewmodel.HomeViewModel
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FragmentHome: Fragment(){

    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, HomeViewModel.Factory(activity.application))
            .get(HomeViewModel::class.java)
    }

    private lateinit var mapView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.freeTime.text = "17:30 ~ 18:20"
        mapView = binding.shiftMap
        mapView.webViewClient = WebViewClient()
        mapView.settings.javaScriptEnabled = true

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.routes.observe(viewLifecycleOwner, Observer<List<Route>?> { routes ->
            routes?.apply {

                if (routes.isNotEmpty()) {
                    Timber.d(routes.toString())
                    var startLoc = ""
                    var start = ""
                    var shop = ""
                    val via = mutableListOf<String>()
                    for (i in 0 until routes.size) {
                        if (routes[i].name == "現在地") {
                            startLoc = "latitude="+routes[i].coord!!.lat+"&longitude="+routes[i].coord!!.lon
                            start =
                                "{\"lat\":" + routes[i].coord!!.lat + ",\"lon\":" + routes[i].coord!!.lon + "}"
                        } else if (routes[i].name == "店舗") {
                            shop =
                                "{\"lat\":" + routes[i].coord!!.lat + ",\"lon\":" + routes[i].coord!!.lon + "}"
                        } else if (routes[i].name != null) {
                            via.add("{\"lat\":" + routes[i].coord!!.lat + ",\"lon\":" + routes[i].coord!!.lon + "}")
                        }
                    }

                    val f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val f2 = DateTimeFormatter.ofPattern("HH:mm")
                    val date = LocalDateTime.now()
                    val startTime = date.format(f1) + "T" + date.format(f2)

                    val query = startLoc +
                            "&start=" + start +
                            "&shop=" + shop +
                            "&starttime=" + startTime +
                            "&via=" + via.toString()
                    Timber.d(query)

                    mapView.loadUrl("https://asia-northeast1-navitime-challenge.cloudfunctions.net/getRouteMap?$query")
                }
            }
        })

    }


}
