package com.example.navitime_challenge.model

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