package com.trip.tripsync.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FriendmanageFriendItemBinding
import com.trip.tripsync.model.User

class FriendManageAdapter(private val deleteFriend: (User) -> Unit) : RecyclerView.Adapter<FriendManageAdapter.ViewHolder>() {

    private var friendList = listOf<User>()

    class ViewHolder (private val binding : FriendmanageFriendItemBinding, private val deleteFriend: (User) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Glide.with(binding.root.context)
                .load(user.profileImg)
                .error(R.drawable.defalt_profile)
                .into(binding.friendManageImage)
            binding.friendManageEmail.text = user.email
            binding.friendManageNickname.text = user.nickname
            binding.friendManageDeleteBtn.setOnClickListener {
                deleteFriend(user)
            }
        }
    }

    fun setList(friendList : List<User>) {
        var curSize = this.friendList.size
        this.friendList = friendList
        notifyItemRangeRemoved(0, curSize)
        notifyItemRangeInserted(0, this.friendList.size)
    }

    fun isListEmpty() = friendList.isEmpty()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FriendmanageFriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), deleteFriend)
    }


    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(friendList[position])
    }
}