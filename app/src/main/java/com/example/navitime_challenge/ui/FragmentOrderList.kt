package com.example.navitime_challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navitime_challenge.adapter.OrderRecyclerViewAdapter
import com.example.navitime_challenge.databinding.FragmentOrderlistBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query

class FragmentOrderList: Fragment(){

    private val TAG = "FragmentOrderList"
    private val LIMIT = 50

    lateinit var recyclerViewAdapter: OrderRecyclerViewAdapter
    lateinit var mFirestore: FirebaseFirestore
    lateinit var query: Query
    private lateinit var mActivity: MainActivity

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = activity as MainActivity
        mFirestore = mActivity.mFirestore

        query = mFirestore.collection("orders").whereEqualTo("status", 0).limit(LIMIT.toLong())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding =
            DataBindingUtil.inflate<FragmentOrderlistBinding>(inflater, R.layout.fragment_orderlist, container, false)

        // RecyclerView
        recyclerView = binding.orderRecyclerview
        recyclerViewAdapter = object : OrderRecyclerViewAdapter(query) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    recyclerView.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Log.e(TAG, e.stackTrace.toString())
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = recyclerViewAdapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // Start sign in if necessary
        /*
        if (shouldStartSignIn()) {
            startSignIn()
            return
        }
         */

        // Start listening for Firestore updates
        recyclerViewAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        recyclerViewAdapter.stopListening()
    }
}

