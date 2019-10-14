package com.example.navitime_challenge.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.navitime_challenge.R
import com.example.navitime_challenge.model.Order
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

import kotlinx.android.synthetic.main.item_order.view.*

open class OrderRecyclerViewAdapter(query: Query) :
    FirestoreAdapter<OrderRecyclerViewAdapter.ViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val TAG = "OrderRecyclerViewAdapter-ViewHolder"

        fun bind(snapshot: DocumentSnapshot) {

            val order = snapshot.toObject(Order::class.java) ?: return
            order.id = snapshot.id
            Log.w(TAG, order.toString())

            itemView.shop.text = order.shop!!.name
            itemView.order_time.text = order.created_at!!.toDate().toString()
            itemView.time_required.text = "15分"
        }
    }
}
