package com.example.tripsync.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.MyplanPlanItemBinding
import com.example.tripsync.model.Plan

class MyPlanAdapter : RecyclerView.Adapter<MyPlanAdapter.ViewHolder>(){
    private var planList = listOf<Plan>()
    
    class ViewHolder (val binding : MyplanPlanItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plan: Plan) {
            val adapter = MyPlanDetailAdapter()
            binding.myplanDetailRv.adapter = adapter
            binding.myplanDetailRv.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            adapter.setList(plan.planDetailList ?: listOf())
            binding.myplanPlanTitle.text = plan.title
        }
    }

    fun setList(planList : List<Plan>) {
        this.planList = planList
        notifyItemRangeChanged(0, this.planList.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MyplanPlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getItemCount(): Int {
        return planList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(planList[position])
    }
}