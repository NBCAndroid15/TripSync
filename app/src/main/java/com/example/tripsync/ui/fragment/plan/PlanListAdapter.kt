package com.example.tripsync.ui.fragment.plan


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tripsync.R
import com.example.tripsync.databinding.PlanRecyclerItemBinding
import com.example.tripsync.model.Travel

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

        }
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        val list = currentList.toMutableList()
        val item = list.removeAt(fromPosition)
        list.add(toPosition, item)
        onItemMove(list)


    }






}