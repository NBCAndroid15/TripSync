package com.example.tripsync.ui.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.R
import com.example.tripsync.databinding.FestivalItemBinding

class HomeFestivalAdapter(private var items: MutableList<Pair<Any, String>>): RecyclerView.Adapter<HomeFestivalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFestivalAdapter.ViewHolder {
        val binding = FestivalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (itemImage, itemText) = items[position]
        holder.bindImageResource(itemImage as Int)
        holder.bindTextItem(itemText)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(private val binding: FestivalItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindImageResource(imageRes: Int) {
            val imageView = binding.festivalItemImg
            imageView.setImageResource(imageRes)
        }
        fun bindTextItem(text: String) {
            val textView = binding.festivalItemText
            textView.text = text
        }
    }
}