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
import com.example.navitime_challenge.databinding.FragmentOrdermapBinding
import com.example.navitime_challenge.domain.Order
import com.example.navitime_challenge.domain.Route
import com.example.navitime_challenge.viewmodel.OrderMapViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FragmentOrderMap: Fragment() {

    private val viewModel: OrderMapViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, OrderMapViewModel.Factory(activity.application))
            .get(OrderMapViewModel::class.java)
    }

    private lateinit var binding: FragmentOrdermapBinding
    private lateinit var mapView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ordermap, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        mapView = binding.mapWebview
        mapView.webViewClient = WebViewClient()
        mapView.settings.javaScriptEnabled = true

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel.routeList.observe(viewLifecycleOwner, Observer<List<Route>> { routes ->
            routes?.apply {

                if (routes.isNotEmpty()) {
                    var startLoc = ""
                    var shop = ""
                    var goals = mutableListOf<String>()
                    for (i in 0 until routes.size) {
                        if (routes[i].id == 0) {
                            startLoc =
                                "latitude=" + routes[i].coord!!.lat + "&longitude=" + routes[i].coord!!.lon
                        } else if (routes[i].name == "店舗") {
                            shop =
                                "{\"lat\":" + routes[i].coord!!.lat + ",\"lon\":" + routes[i].coord!!.lon + "}"
                        } else if (routes[i].name != null && routes[i].name != "現在地") {
                            goals.add("{\"lat\":" + routes[i].coord!!.lat + ",\"lon\":" + routes[i].coord!!.lon + "}")
                        }
                    }
                    val f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val f2 = DateTimeFormatter.ofPattern("HH:mm")
                    val date = LocalDateTime.now()
                    val startTime = date.format(f1) + "T" + date.format(f2)

                    val query = startLoc +
                            "&shop=" + shop +
                            "&goals=" + goals.toString() +
                            "&starttime=" + startTime
                    Timber.d(query)
                    mapView.loadUrl("https://asia-northeast1-navitime-challenge.cloudfunctions.net/getMap?$query")
                }
            }
        })

    }

    @BindingAdapter("url")
    fun WebView.bindUrl(url: String?) {
        if (url != null) {
            loadUrl(url)
        }
    }

}
