package com.example.tripsync.ui.fragment.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.R
import com.example.tripsync.databinding.AreaItemBinding

class HomeAreaAdapter(private var items: MutableList<String>): RecyclerView.Adapter<HomeAreaAdapter.ViewHolder>() {

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAreaAdapter.ViewHolder {
        val binding = AreaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
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
}