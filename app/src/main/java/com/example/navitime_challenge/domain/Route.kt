package com.example.navitime_challenge.domain

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Route(
    val type: String = "",
    val name: String? = null,
    val coord: Coord? = null,
    val move: String? = null,
    val from_time: String? = null,
    val to_time: String? = null,
    val time: Int? = null,
    val distance: Int? = null
)

data class Coord(
    val lat: Double? = null,
    val lon: Double? = null
)

@IgnoreExtraProperties
data class RoutePayload(
    val start: String,
    val shop: String,
    val starttime: String,
    val endtime: String,
    val via: String
)
