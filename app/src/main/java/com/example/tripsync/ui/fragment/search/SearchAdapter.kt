package com.example.tripsync.ui.fragment.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripsync.databinding.SearchItemBinding
import com.example.tripsync.model.Travel

class SearchAdapter () : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }
    var itemClick : ItemClick? = null


    private var searchList = listOf<Travel>()

    class ViewHolder (private val binding : SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(travel: Travel) {
            Glide.with(binding.root.context)
                .load(travel.imageUrl)
                .into(binding.searchItemThumbnail)
            binding.searchItemTitle.text = travel.title
            binding.searchItemAddr.text = travel.addr

        }
    }

    fun setList(searchList : List<Travel>) {
        var curSize = this.searchList.size
        this.searchList = searchList
        notifyItemRangeRemoved(0, curSize)
        notifyItemRangeInserted(0, this.searchList.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchList[position])
    }
}
