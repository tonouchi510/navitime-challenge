package com.example.navitime_challenge.domain

import com.google.firebase.firestore.GeoPoint

data class User (
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val geopoint: GeoPoint? = null
)