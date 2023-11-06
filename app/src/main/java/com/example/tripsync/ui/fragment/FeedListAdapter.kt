package com.example.tripsync.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.FeedListItemBinding
import com.example.tripsync.databinding.FragmentFeedListBinding
import com.example.tripsync.ui.fragment.search.SearchAdapter


data class FeedItem(
    val title: String,
    val content: String,
    val nickname: String,
    val type: String,
    val location: String,
    val time: String,
    val veiw: String,
    val comment: String)


class FeedListAdapter : RecyclerView.Adapter<FeedListAdapter.ViewHolder>() {

    private val items: List<FeedItem>

    init {
        val itemList = mutableListOf<FeedItem>()

        for (i in 1..10) {
            val feedItem = FeedItem(
                "제목 $i",
                "설명 $i",
                "닉네임 $i",
                "일상",
                "서울",
                "?",
                "?",
                "?"
            )
            itemList.add(feedItem)
        }

        items = itemList
    }

    inner class ViewHolder(private val binding: FeedListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeedItem) {
            // 아이템을 화면에 바인딩
        }
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(FeedListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClick {
        fun onItemClick()
    }

    private var itemClick: OnItemClick? = null

    fun setOnItemClickListener(listener: OnItemClick) {
        itemClick = listener
    }
}
