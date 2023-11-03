package com.example.tripsync.ui.fragment.plan.planbookmarklist

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripsync.databinding.PlanBookmarkListItemBinding
import com.example.tripsync.model.Travel

class PlanBookmarkListAdapter(private val itemClickCallBack: (Travel)-> Boolean, private val currentLocation: Location): ListAdapter<Travel, PlanBookmarkListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Travel>() {
        override fun areItemsTheSame(oldItem: Travel, newItem: Travel): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: Travel, newItem: Travel): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlanBookmarkListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: PlanBookmarkListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Travel) = with(binding) {
            Glide.with(itemView)
                .load(item.imageUrl)
                .into(planbookListItemThumbnail)

            binding.planBooklistBtn.setOnClickListener {

                    if(!itemClickCallBack(item)) {
                        Toast.makeText(binding.root.context, "여행지는 최대 10개까지 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }


            }

            planbookListItemTitle.text = item.title
            planbookListItemAddr.text = item.addr

            val travelLoc = Location("travelLoc")
            travelLoc.latitude = item.mapY!!
            travelLoc.longitude = item.mapX!!

            val distance = currentLocation.distanceTo(travelLoc) / 1000
            val distanceInKM = (distance * 10).toInt() / 10.0
            val formatKM = "나와의 거리 - ${distanceInKM.toInt()}km"
            planBookKm.text = formatKM
        }

    }



}