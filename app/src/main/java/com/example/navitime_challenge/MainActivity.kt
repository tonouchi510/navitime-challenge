package com.example.navitime_challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.navitime_challenge.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val LIMIT = 50

    private var mFirestore: FirebaseFirestore? = null
    private var mQuery: Query? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        pager.adapter =
            TabAdapter(supportFragmentManager, this)
        tabLayout.setupWithViewPager(pager)

        initFirestore()
    }

    private fun initFirestore() {
        mFirestore = FirebaseFirestore.getInstance()

        // Get the 50 highest rated restaurants
        val docRef = mFirestore!!.collection("orders").document("7V99VLl0mJisOBucdCVS")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }
}
