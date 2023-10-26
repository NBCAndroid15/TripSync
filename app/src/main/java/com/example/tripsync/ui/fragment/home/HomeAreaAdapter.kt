package com.example.tripsync.ui.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.R
import com.example.tripsync.databinding.AreaItemBinding

class HomeAreaAdapter(private var items: MutableList<String>): RecyclerView.Adapter<HomeAreaAdapter.ViewHolder>() {

    val itemStates = mutableMapOf<Int, Boolean>()

    interface ItemClick {
        fun onClick(keyword: String)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAreaAdapter.ViewHolder {
        val binding = AreaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])

        val isColor = itemStates[position] == true
        if (isColor) {
            holder.itemView.setBackgroundResource(R.drawable.setup_btn_round12)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.setup_btn_round)
        }

        holder.itemView.setOnClickListener {
            val keyword = items[position]
            itemStates[position] = true
            itemClick?.onClick(keyword)

            clearItem(position)
            notifyDataSetChanged()
        }

    }

    private fun clearItem(selectedPosition: Int) {
        for((position, _) in itemStates) {
            if (position != selectedPosition) {
                itemStates[position] = false
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(private val binding: AreaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val textView = binding.areaItem
        fun bind(text: String) {
            textView.text = text

        }
    }

    init {
        if (items.isNotEmpty()) {
            itemStates[0] = true
        }
    }

}