package com.example.navitime_challenge.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Order(
    var id: String? = null,
    var status: Int? = null,
    var items: List<String>? = null,
    var shop: Shop? = null,
    var user_info: User? = null,
    var created_at: Timestamp? = null
)
