package com.example.navitime_challenge.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Order(
    val id: String = "",
    val status: String = "",
    val items: List<String>? = null,
    val shop: Shop? = null,
    val user_info: User? = null,
    val createdAt: Timestamp? = null
)
