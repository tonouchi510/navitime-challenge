package com.example.navitime_challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.navitime_challenge.R
import com.example.navitime_challenge.databinding.ItemOrderBinding
import com.example.navitime_challenge.databinding.ItemScheduleBinding
import com.example.navitime_challenge.domain.Schedule

class ScheduleRecycleViewAdapter: RecyclerView.Adapter<ScheduleViewHolder>() {

    var schedule: Schedule? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val withDataBinding: ItemScheduleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ScheduleViewHolder.LAYOUT,
            parent,
            false)
        return ScheduleViewHolder(withDataBinding)
    }

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.schedule = schedule
        }
    }
}

/**
 * ViewHolder for Order items. All work is done by data binding.
 */
class ScheduleViewHolder(val viewDataBinding: ItemScheduleBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_schedule
    }
}
