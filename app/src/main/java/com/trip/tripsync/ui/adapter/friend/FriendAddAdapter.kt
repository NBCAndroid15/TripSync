package com.trip.tripsync.ui.adapter.friend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FriendmanageFriendAddItemBinding
import com.trip.tripsync.model.User

class FriendAddAdapter(private val addFriend: (User) -> Unit) : RecyclerView.Adapter<FriendAddAdapter.ViewHolder>() {

    private var friendList = listOf<User>()

    class ViewHolder (private val binding : FriendmanageFriendAddItemBinding, private val addFriend: (User) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Glide.with(binding.root.context)
                .load(user.profileImg)
                .error(R.drawable.defalt_profile)
                .into(binding.friendManageProfileImg)
            binding.friendManageNickname.text = user.nickname
            binding.friendManageAddFriendBtn.setOnClickListener {
                addFriend(user)
            }
        }
    }

    fun setList(friendList : List<User>) {
        var curSize = this.friendList.size
        this.friendList = friendList
        notifyItemRangeRemoved(0, curSize)
        notifyItemRangeInserted(0, this.friendList.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FriendmanageFriendAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), addFriend)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(friendList[position])
    }
}