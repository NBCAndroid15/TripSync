package com.trip.tripsync.ui.adapter.setup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trip.tripsync.R
import com.trip.tripsync.databinding.PlanUserAddItemBinding
import com.trip.tripsync.model.User

class SetupUserAddDialogAdapter(private val itemClickCallBack: (User)-> Unit,
                                private val planUsers: LiveData<List<User>>): ListAdapter<User, SetupUserAddDialogAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
) {

    inner class ViewHolder(private val binding: PlanUserAddItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isState = false
        fun bind(user: User) = with(binding) {

            Glide.with(itemView)
                .load(user.profileImg)
                .error(R.drawable.defalt_profile)
                .into(planUserProfile)

            planUserName.text = user.nickname

            planUserAddBtn.setOnClickListener {
                if(!isState) {
                    itemClickCallBack(user)
                    planUserAddBtn.setImageResource(R.drawable.ic_person_remove)
                    isState = true
                } else {
                    isState = false
                    itemClickCallBack(user)
                    planUserAddBtn.setImageResource(R.drawable.ic_person_add)
                }
            }

            if (planUsers.value.orEmpty().any {
                    it.uid == user.uid
                }) {
                planUserAddBtn.setImageResource(R.drawable.ic_person_remove)
                isState = true
            } else {
                isState = false
                planUserAddBtn.setImageResource(R.drawable.ic_person_add)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = PlanUserAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}