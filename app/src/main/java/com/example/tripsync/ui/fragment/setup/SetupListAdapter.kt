package com.example.tripsync.ui.fragment.setup

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.SetupRecyclerDateItemBinding
import com.prolificinteractive.materialcalendarview.CalendarDay

class SetupListAdapter : ListAdapter<CalendarDay, SetupListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<CalendarDay>() {
        override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SetupRecyclerDateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: SetupRecyclerDateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: CalendarDay) = with(binding) {
            setupMakingBtn.text = "${date.year}년 ${date.month}월 ${date.day}일"

            setupMakingBtn.setOnClickListener {
                itemClickListener?.onItemClick(date)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(date: CalendarDay)
    }
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}