package com.example.navitime_challenge.network

import com.example.navitime_challenge.domain.Route
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NavitimeRouteContainer(val routes: NavitimeRoute)

@JsonClass(generateAdapter = true)
data class NavitimeRoute(
    val items: String,
    val unit: String)
