package com.example.tripsync.ui.fragment.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.FragmentPlanBinding
import com.example.tripsync.databinding.PlanEditViewBinding
import com.example.tripsync.model.Plan

class PlanListAdapter(private val onItemChecked: (Int, Plan) -> Unit): ListAdapter<Plan, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Plan>() {
        override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem.planDetailList == newItem.planDetailList
        }

        override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            PlanViewType.Normal.INT -> {
                val view = FragmentPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(view)
            }

            PlanViewType.Edit.INT -> {
                val view = PlanEditViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EditViewHolder(view, onItemChecked )
            }

            else -> {
                val view = FragmentPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                holder.bind(getItem(position))
            }

            is EditViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }


    class ViewHolder(private val binding: FragmentPlanBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Plan) = with(binding) {


        }
    }

    class EditViewHolder(
        private val binding: PlanEditViewBinding, private val onItemChecked: (Int, Plan) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Plan) = with(binding) {

        }
    }
}