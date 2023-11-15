package com.trip.tripsync.ui.adapter.myplan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trip.tripsync.databinding.MyplanDetailItemBinding
import com.trip.tripsync.model.Plan
import com.trip.tripsync.model.PlanDetail

class MyPlanDetailAdapter(private val parentAdapter: MyPlanAdapter, private val gotoPlan : (Plan, Int) -> Unit) : RecyclerView.Adapter<MyPlanDetailAdapter.ViewHolder>(){
    var planDetailList = listOf<PlanDetail>()

    class ViewHolder (private val binding : MyplanDetailItemBinding, private val adapter: MyPlanDetailAdapter, private val parentAdapter: MyPlanAdapter, private val gotoPlan : (Plan, Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(planDetail: PlanDetail) {
            binding.myplanDetailDate.text = planDetail.date
            binding.myplanDetailLayout.setOnClickListener {
                gotoPlan(parentAdapter.getPlanByDetailList(adapter.planDetailList) ?: Plan(), absoluteAdapterPosition)
            }
        }
    }

    fun setList(planDetailList : List<PlanDetail>) {
        this.planDetailList = planDetailList
        notifyItemRangeChanged(0, this.planDetailList.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MyplanDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), this, parentAdapter, gotoPlan)
    }


    override fun getItemCount(): Int {
        return planDetailList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(planDetailList[position])
    }
}