package com.trip.tripsync.ui.fragment.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FestivalItemBinding
import com.trip.tripsync.model.Travel

class HomeFestivalAdapter(private var items: List<Travel>): RecyclerView.Adapter<HomeFestivalAdapter.ViewHolder>() {

    fun updateItems(newItems: List<Travel>) {
        items = newItems
        notifyItemRangeChanged(0, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFestivalAdapter.ViewHolder {
        val binding = FestivalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(private val binding: FestivalItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun setItem(item: Travel) {
            val image = binding.festivalItemImg
            val startDate = binding.festivalStartdateText
            val endDate = binding.festivalEnddateText
            val city = binding.festivalCityText
            val title = binding.festivalItemText
            val formattedStartDate = formatEventDate(item.startDate.toString())
            val formattedEndDate = formatEventDate(item.endDate.toString())

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
            
            startDate.text = "$formattedStartDate 부터"
            endDate.text = "$formattedEndDate 까지"
            city.text = item.area
            title.text = item.title
            Log.d("지역코드", "${item.area}")

            binding.festivalItemView.setOnClickListener {
                itemClick?.onFestivalClick(item)
            }
        }
    }
    fun formatEventDate(eventDate: String): String {
        if (eventDate.length >= 8) {
            val year = eventDate.substring(0, 4)
            val month = eventDate.substring(4, 6)
            val day = eventDate.substring(6, 8)
            return "$year-$month-$day"
        }
        return eventDate // 이상한 형식의 날짜인 경우 그대로 반환
    }

    interface onFestivalClick {
        fun onFestivalClick (travel: Travel)
    }
    private var itemClick : onFestivalClick? = null
    fun setOnFestivalClickListener(listener: HomeFragment) {
        itemClick = listener
    }
}