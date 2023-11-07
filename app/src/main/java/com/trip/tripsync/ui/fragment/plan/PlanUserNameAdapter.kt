package com.trip.tripsync.ui.fragment.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trip.tripsync.R
import com.trip.tripsync.databinding.PlanUsernameItemBinding
import com.trip.tripsync.model.User


class PlanUserNameAdapter : ListAdapter<User, PlanUserNameAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
) {

    class ViewHolder(private val binding: PlanUsernameItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User) = with(binding) {

            Glide.with(itemView)
                .load(item.profileImg)
                .error(R.drawable.defalt_profile)
                .into(planUserProfile)



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlanUsernameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}