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


        viewModel.getSavedOrdersFromRepository().observe(viewLifecycleOwner, Observer<List<Order>> { orders ->
            orders?.apply {
                val orderRoutes = orders.subList(0, 5).map { o ->
                    "{\"shop\":{\"lat\":"+o.shop!!.geopoint!!.latitude+",\"lon\":"+o.shop.geopoint!!.longitude+"}," +
                            "\"goal\":{\"lat\":"+o.user_info!!.geopoint!!.latitude+",\"lon\":"+o.user_info.geopoint!!.longitude+"}}"
                }
                val f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val f2 = DateTimeFormatter.ofPattern("HH:mm")
                val date = LocalDateTime.now()
                val startTime = date.format(f1) + "T" + date.format(f2)

                val startLoc = "latitude=35.477664&longitude=139.622101"
                val query = startLoc +
                        "&order=" + orderRoutes.toString() +
                        "&starttime=" + startTime
                Timber.d(query)
                mapView.loadUrl("https://asia-northeast1-navitime-challenge.cloudfunctions.net/getMap?$query")
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
