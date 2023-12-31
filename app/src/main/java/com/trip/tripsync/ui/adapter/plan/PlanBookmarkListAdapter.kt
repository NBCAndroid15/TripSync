package com.trip.tripsync.ui.adapter.plan

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.trip.tripsync.R
import com.trip.tripsync.databinding.PlanBookmarkListItemBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.util.LocationUtility

//, private val currentLocation: Location
class PlanBookmarkListAdapter(private val itemClickCallBack: (Travel)-> Boolean,
                              private val planItems: LiveData<List<Travel>>,
                              private val deleteItem: (Travel) -> Unit): ListAdapter<Travel, PlanBookmarkListAdapter.ViewHolder>(
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

        private var isItemState = false
        fun bind(item: Travel) = with(binding) {
            Glide.with(itemView)
                .load(item.imageUrl)
                .error(R.drawable.item_error)
                .into(planbookListItemThumbnail)

            if (planItems.value.orEmpty().any {
                    it.title == item.title
                }) {
                planBooklistBtn.setImageResource(R.drawable.ic_plansearch_plus)
                isItemState = true
            } else {
                isItemState = false
                planBooklistBtn.setImageResource(R.drawable.ic_plansearch_add)
            }

            binding.planBooklistBtn.setOnClickListener {
                if(!isItemState) {
                    if (!itemClickCallBack(item)) {
                        Toast.makeText(
                            binding.root.context,
                            "여행지는 최대 10개까지 추가할 수 있습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        planBooklistBtn.setImageResource(R.drawable.ic_plansearch_plus)
                        isItemState = true
                    }
                } else {
                    deleteItem(item)
                    isItemState = false
                    planBooklistBtn.setImageResource(R.drawable.ic_plansearch_add)

                }
            }

            planbookListItemTitle.text = item.title
            planbookListItemAddr.text = item.addr

            itemView.setOnClickListener {
                itemClickListener?.onItemClick(item)
            }

            val locationUtility = LocationUtility(binding.root.context)
            locationUtility.requestLocationUpdate(OnSuccessListener { currentLocation ->
                try {
                    if (currentLocation != null) {
                        val itemLocation = Location("itemLocation")
                        itemLocation.latitude = item.mapY ?: 0.0
                        itemLocation.longitude = item.mapX ?: 0.0

                        val distance = currentLocation.distanceTo(itemLocation) / 1000
                        val distanceInKM = (distance * 10).toInt() / 10.0
                        val formatKM = "나와의 거리 - ${distanceInKM.toInt()}km"
                        planBookKm.text = formatKM
                    }
                } catch (e: SecurityException) {
                    planBookKm.text = "알 수 없음"
                    e.printStackTrace()
                }
            })



        }

    }

    interface OnItemClickListener {
        fun onItemClick(item: Travel)
    }
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }




}