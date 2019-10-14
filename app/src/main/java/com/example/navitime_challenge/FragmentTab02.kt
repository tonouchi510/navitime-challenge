package com.example.navitime_challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navitime_challenge.adapter.OrderRecyclerViewAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_tab02.*

class FragmentTab02: Fragment(){

    private val LIMIT = 50
    lateinit var recyclerViewAdapter: OrderRecyclerViewAdapter
    lateinit var mFirestore: FirebaseFirestore
    lateinit var query: Query

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirestore = FirebaseFirestore.getInstance()
        query = mFirestore.collection("orders").limit(LIMIT.toLong())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_tab02, container,false)
        recyclerView = view.findViewById(R.id.order_recyclerview)

        // RecyclerView
        recyclerViewAdapter = object : OrderRecyclerViewAdapter(query) {
            override fun onDataChanged() {
                // Show/hide content if the query returns empty.
                if (itemCount == 0) {
                    order_recyclerview.visibility = View.GONE
                } else {
                    order_recyclerview.visibility = View.VISIBLE
                }
            }

            override fun onError(e: FirebaseFirestoreException) {
                // Show a snackbar on errors
                Snackbar.make(view.findViewById(android.R.id.content),
                    "Error: check logs for info.", Snackbar.LENGTH_LONG).show()
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = recyclerViewAdapter
        return view
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

