package com.trip.tripsync.ui.fragment.setup

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trip.tripsync.databinding.SetupRecyclerDateItemBinding

class SetupListAdapter : ListAdapter<String, SetupListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
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
        fun bind(date: String) = with(binding) {
            setupMakingBtn.text = date

            setupMakingBtn.setOnClickListener {
                itemClickListener?.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}