package com.example.tripsync.ui.fragment.plan.plansearchlist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripsync.databinding.PlanSearchListItemBinding
import com.example.tripsync.model.Travel

class PlanSearchListAdapter(private val itemClickCallBack: (Travel)-> Boolean ) : ListAdapter<Travel, PlanSearchListAdapter.ViewHolder> (
    object : DiffUtil.ItemCallback<Travel>() {
        override fun areItemsTheSame(oldItem: Travel, newItem: Travel): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: Travel, newItem: Travel): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = PlanSearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: PlanSearchListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Travel) = with(binding) {
            Glide.with(itemView)
                .load(item.imageUrl)
                .into(planSearchThumbnail)

            planSearchCheck.setOnClickListener {
                if(!itemClickCallBack(item)) {
                    Toast.makeText(binding.root.context, "여행지는 최대 10개까지 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            planSearchTitle.text = item.title
            planSearchAddr.text = item.area
        }

    }
}