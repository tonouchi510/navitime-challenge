package com.example.navitime_challenge.domain

import com.google.firebase.firestore.GeoPoint

data class Shop(
    val address: String? = null,
    val geopoint: GeoPoint? = null,
    val brand: String? = null,
    val name: String? = null
)