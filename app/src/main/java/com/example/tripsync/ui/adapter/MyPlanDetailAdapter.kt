package com.example.tripsync.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.MyplanDetailItemBinding
import com.example.tripsync.model.PlanDetail

class MyPlanDetailAdapter : RecyclerView.Adapter<MyPlanDetailAdapter.ViewHolder>(){
    private var planDetailList = listOf<PlanDetail>()

    class ViewHolder (private val binding : MyplanDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(planDetail: PlanDetail) {
            binding.myplanDetailDate.text = planDetail.date
        }
    }

    fun setList(planDetailList : List<PlanDetail>) {
        this.planDetailList = planDetailList
        notifyItemRangeChanged(0, this.planDetailList.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MyplanDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getItemCount(): Int {
        return planDetailList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(planDetailList[position])
    }
}