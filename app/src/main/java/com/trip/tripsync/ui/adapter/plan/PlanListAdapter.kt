package com.trip.tripsync.ui.adapter.plan


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trip.tripsync.R
import com.trip.tripsync.databinding.PlanRecyclerItemBinding
import com.trip.tripsync.model.Travel

class PlanListAdapter(private val onItemRemove: (Travel) -> Unit,
                      private val onItemMove: (List<Travel>)-> Unit): ListAdapter<Travel, PlanListAdapter.ViewHolder>(
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
        val binding = PlanRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    inner class ViewHolder(private val binding: PlanRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Travel) = with(binding) {
            Glide.with(itemView)
                .load(item.imageUrl)
                .error(R.drawable.defalt_profile)
                .into(planItemImage)

            planItemTitle.text = item.title
            planItemWhere.text = item.addr
                planListNumber.text = (absoluteAdapterPosition + 1).toString()

            tvRemove.setOnClickListener {
                onItemRemove(item)

            }

//            val locationUtility = LocationUtility(binding.root.context)
//            locationUtility.requestLocationUpdate(OnSuccessListener { currentLocation ->
//                try {
//                    if (currentLocation != null) {
//                        val itemLocation = Location("itemLocation")
//                        itemLocation.latitude = item.mapY ?: 0.0
//                        itemLocation.longitude = item.mapX ?: 0.0
//
//                        val distance = currentLocation.distanceTo(itemLocation) / 1000
//                        val distanceInKM = (distance * 10).toInt() / 10.0
//                        val formatKM = "나와의 거리 - ${distanceInKM.toInt()}km"
//                        planItemKm.text = formatKM
//                    }
//                } catch (e: SecurityException) {
//                    planItemKm.text = "알 수 없음"
//                    e.printStackTrace()
//                }
//            })

            itemView.setOnClickListener {
                itemClickListener?.onItemClick(item)
            }

        }
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        val list = currentList.toMutableList()
        val item = list.removeAt(fromPosition)
        list.add(toPosition, item)
        onItemMove(list)


    }

    interface OnItemClickListener {
        fun onItemClick(item: Travel)
    }
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }






}