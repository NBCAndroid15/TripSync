package com.example.tripsync.ui.fragment.plan

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripsync.databinding.PlanRecyclerItemBinding
import com.example.tripsync.model.Travel
import java.util.Collections

class PlanListAdapter(private val onItemRemove: (Travel) -> Unit): ListAdapter<Travel, PlanListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Travel>() {
        override fun areItemsTheSame(oldItem: Travel, newItem: Travel): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: Travel, newItem: Travel): Boolean {
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

    inner class ViewHolder(private val binding: PlanRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Travel) = with(binding) {
            Glide.with(itemView)
                .load(item.imageUrl)
                .into(planItemImage)

            planItemTitle.text = item.title
            planItemWhere.text = item.area
            planListNumber.text = (currentList.indexOf(item) + 1).toString()


            tvRemove.setOnClickListener {
                val itemPositionToDelete = absoluteAdapterPosition
                Log.d("PlanListAdapter", "클릭: $itemPositionToDelete")
                onItemRemove(item)
            }

        }
    }
}