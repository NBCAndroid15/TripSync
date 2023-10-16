package com.example.tripsync.ui.fragment.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.R
import com.example.tripsync.databinding.SetupRecyclerDateItemBinding
import com.example.tripsync.ui.fragment.plan.PlanFragment
import com.prolificinteractive.materialcalendarview.CalendarDay

class SetupListAdapter : ListAdapter<CalendarDay, SetupListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<CalendarDay>() {
        override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
            return oldItem == newItem
        }

    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SetupRecyclerDateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: SetupRecyclerDateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: CalendarDay) = with(binding) {
            setupMakingBtn.text = "${date.year}년 ${date.month}월 ${date.day}일"


            itemView.setOnClickListener {
                val context = itemView.context
                val planFragment = PlanFragment.newInstance()

                // PlanFragment로 선택한 날짜를 전달할 수 있도록 Bundle에 데이터를 추가
                val args = Bundle()
                args.putString("selectedDate", "${date.year}-${date.month}-${date.day}")
                planFragment.arguments = args

                // PlanFragment로 이동
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(androidx.fragment.R.id.fragment_container_view_tag, planFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}