package com.example.navitime_challenge.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil

import com.example.navitime_challenge.R
import com.example.navitime_challenge.databinding.ItemOrderBinding
import com.example.navitime_challenge.domain.Order


class OrderRecyclerViewAdapter : RecyclerView.Adapter<OrderViewHolder>() {

    var orders: List<Order> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val withDataBinding: ItemOrderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            OrderViewHolder.LAYOUT,
            parent,
            false)
        return OrderViewHolder(withDataBinding)
    }

    override fun getItemCount() = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.order = orders[position]
            it.orderTime.text = orders[position].createdAt?.toDate().toString()
            it.timeRequired.text = "15分"
        }
    }
}

/**
 * ViewHolder for Order items. All work is done by data binding.
 */
class OrderViewHolder(val viewDataBinding: ItemOrderBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_order
    }
}
