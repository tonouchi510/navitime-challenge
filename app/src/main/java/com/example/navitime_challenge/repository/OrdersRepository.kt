package com.example.navitime_challenge.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


/**
 * Repository for fetching orders from the network and storing them on disk
 */
class OrdersRepository {

    private val firestoreDB = FirebaseFirestore.getInstance()

    fun getSavedOrders(): Query {
        return firestoreDB.collection("orders")
            .whereGreaterThan("shop.address", "神奈川")
            .whereEqualTo("status", "0")
            .orderBy("shop.address")
            .limit(30)
    }

}