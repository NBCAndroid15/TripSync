package com.example.tripsync.ui.fragment.plan.planbookmarklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripsync.databinding.PlanBookmarkListItemBinding
import com.example.tripsync.model.Travel

class PlanBookmarkListAdapter(private val itemClickCallBack: (Travel)-> Unit ): ListAdapter<Travel, PlanBookmarkListAdapter.ViewHolder>(
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
        val binding = PlanBookmarkListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: PlanBookmarkListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Travel) = with(binding) {
            Glide.with(itemView)
                .load(item.imageUrl)
                .into(planbookListItemThumbnail)

            binding.planBooklistBtn.setOnClickListener {
                itemClickCallBack(item)
            }

            planbookListItemTitle.text = item.title
            planbookListItemAddr.text = item.area
        }

    }


}