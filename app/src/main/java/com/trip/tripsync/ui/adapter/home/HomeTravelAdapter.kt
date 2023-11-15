package com.trip.tripsync.ui.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.trip.tripsync.R
import com.trip.tripsync.databinding.TravelItemBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.ui.fragment.home.HomeFragment

class HomeTravelAdapter(private var items: List<Travel>): RecyclerView.Adapter<HomeTravelAdapter.ViewHolder>() {

    fun updateItems(newItems: List<Travel>) {
        items = newItems
        notifyItemRangeChanged(0, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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
            title.text = item.title

            image.post {
                val myOptions = RequestOptions()
                    .override(image.width, image.height)
                    .centerCrop()
                    .placeholder(R.drawable.item_error)



                Glide
                    .with(image.context)
                    .load(item.imageUrl)
                    .apply(myOptions)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.item_error)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(image)
            }

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