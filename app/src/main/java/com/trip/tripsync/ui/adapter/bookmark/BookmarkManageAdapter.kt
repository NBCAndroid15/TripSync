package com.trip.tripsync.ui.adapter.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trip.tripsync.databinding.BookmarkmanageBookmarkItemBinding
import com.trip.tripsync.model.Travel

class BookmarkManageAdapter(private val deleteBookmark: (Travel) -> Unit, private val gotoDetail: (Travel) -> Unit) : RecyclerView.Adapter<BookmarkManageAdapter.ViewHolder>() {

    private var bookmarkList = listOf<Travel>()

    class ViewHolder (private val binding : BookmarkmanageBookmarkItemBinding, private val deleteBookmark: (Travel) -> Unit, private val gotoDetail: (Travel) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(travel: Travel) {
            Glide.with(binding.root.context)
                .load(travel.imageUrl)
                .into(binding.bookmarkItemThumbnail)
            binding.bookmarkItemTitle.text = travel.title
            binding.bookmarkItemAddr.text = travel.addr

            binding.bookmarkItemDelete.setOnClickListener {
                deleteBookmark(travel)
            }

            binding.bookmarkManageLayout.setOnClickListener {
                gotoDetail(travel)
            }
        }
    }

    fun setList(bookmarkList : List<Travel>) {
        this.bookmarkList = bookmarkList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BookmarkmanageBookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), deleteBookmark, gotoDetail)
    }


    override fun getItemCount(): Int {
        return bookmarkList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bookmarkList[position])
    }
}