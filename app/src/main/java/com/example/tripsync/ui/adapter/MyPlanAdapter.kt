package com.example.tripsync.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.MyplanPlanItemBinding
import com.example.tripsync.model.Plan
import com.example.tripsync.model.PlanDetail

class MyPlanAdapter(private val gotoPlan : (Plan, Int) -> Unit) : RecyclerView.Adapter<MyPlanAdapter.ViewHolder>(){
    private var planList = listOf<Plan>()
    
    class ViewHolder (val binding : MyplanPlanItemBinding, private val parentAdapter: MyPlanAdapter, private val gotoPlan : (Plan, Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plan: Plan) {
            val adapter = MyPlanDetailAdapter(parentAdapter, gotoPlan)
            binding.myplanDetailRv.adapter = adapter
            binding.myplanDetailRv.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            adapter.setList(plan.planDetailList ?: listOf())
            binding.myplanPlanTitle.text = plan.title
        }
    }

    fun setList(planList : List<Plan>) {
        val curSize = this.planList.size
        this.planList = planList
        notifyItemRangeRemoved(0, curSize)
        notifyItemRangeChanged(0, this.planList.size)
    }

    fun getPlanByDetailList(planDetailList: List<PlanDetail>) =
        planList.firstOrNull {
            it.planDetailList === planDetailList
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MyplanPlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), this, gotoPlan)
    }


    override fun getItemCount(): Int {
        return planList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(planList[position])
    }
}