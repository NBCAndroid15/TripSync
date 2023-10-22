package com.example.tripsync.ui.fragment.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.tripsync.databinding.PlanMemoItemBinding

class PlanMemoListAdapter(private val memoList: MutableList<String>): ListAdapter<String, PlanMemoListAdapter.ViewHolder> (
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.length == newItem.length
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
) {
    class ViewHolder(private val binding: PlanMemoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memo: String) = with(binding) {
            planMemoText.text = memo
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlanMemoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}