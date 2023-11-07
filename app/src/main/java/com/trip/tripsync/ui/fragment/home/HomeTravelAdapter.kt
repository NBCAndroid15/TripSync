package com.trip.tripsync.ui.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trip.tripsync.R
import com.trip.tripsync.databinding.TravelItemBinding
import com.trip.tripsync.model.Travel

class HomeTravelAdapter(private var items: List<Travel>): RecyclerView.Adapter<HomeTravelAdapter.ViewHolder>() {

    fun updateItems(newItems: List<Travel>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTravelAdapter.ViewHolder {
        val binding = TravelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(private val binding: TravelItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: Travel) {
            val image = binding.travelItemImg
            val title = binding.travelItemText
            Glide.with(binding.root)
                .load(item.imageUrl)
                .error(R.drawable.item_error)
                .into(image)
            title.text = item.title

            binding.travelItemView.setOnClickListener {
                itemClick?.onTravelClick(item)
            }
        }
    }

    interface onTravelClick {
        fun onTravelClick (travel: Travel)
    }
    private var itemClick : onTravelClick? = null
    fun setOntravelClickListener(listener: HomeFragment) {
        itemClick = listener
    }
}