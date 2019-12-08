package com.example.navitime_challenge.domain

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Token(
    val accessToken: String? = null,
    val refreshToken: String? = null
)

@IgnoreExtraProperties
data class GoogleAuthPayload(
    val clientId: String,
    val clientSecret: String,
    val grantType: String?,
    val code: String?
)
