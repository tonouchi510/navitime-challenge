package com.example.navitime_challenge.domain

import com.google.firebase.firestore.GeoPoint

data class Shop(
    val address: String = "",
    val geopoint: GeoPoint? = null,
    val brand: String = "",
    val name: String = ""
)