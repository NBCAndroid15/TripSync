package com.example.tripsync.ui.fragment.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripsync.databinding.PlanRecyclerItemBinding
import com.example.tripsync.model.Plan

class PlanListAdapter(private val onItemChecked: (Int, TestModel) -> Unit): ListAdapter<TestModel, PlanListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<TestModel>() {
        override fun areItemsTheSame(oldItem: TestModel, newItem: TestModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TestModel, newItem: TestModel): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlanRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    class ViewHolder(private val binding: PlanRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TestModel) = with(binding) {
            Glide.with(itemView)
                .load(item.id)
                .into(planItemImage)

            planItemTitle.text = item.name
        }
    }

}