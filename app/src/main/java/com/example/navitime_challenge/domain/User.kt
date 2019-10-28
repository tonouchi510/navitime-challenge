package com.example.navitime_challenge.domain

import com.google.firebase.firestore.GeoPoint

data class User (
    val uid: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val geopoint: GeoPoint? = null
)