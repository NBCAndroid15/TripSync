package com.example.tripsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.MyplanPlanItemBinding
import com.example.tripsync.model.Plan
import com.example.tripsync.model.PlanDetail
import com.example.tripsync.ui.collapse
import com.example.tripsync.ui.expand

class MyPlanAdapter(private val gotoPlan : (Plan, Int) -> Unit) : RecyclerView.Adapter<MyPlanAdapter.ViewHolder>(){
    private var planList = listOf<Plan>()
    
    class ViewHolder (val binding : MyplanPlanItemBinding, private val parentAdapter: MyPlanAdapter, private val gotoPlan : (Plan, Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        private var foldState = true
        fun bind(plan: Plan) {
            foldState = true
            binding.myplanDetailRv.visibility = View.GONE

            val adapter = MyPlanDetailAdapter(parentAdapter, gotoPlan)
            binding.myplanDetailRv.adapter = adapter
            binding.myplanDetailRv.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            adapter.setList(plan.planDetailList ?: listOf())
            binding.myplanPlanTitle.text = plan.title

            binding.myplanPlanToggle.setOnClickListener {
                if (foldState) {
                    binding.myplanDetailRv.expand()
                    foldState = !foldState
                } else {
                    binding.myplanDetailRv.collapse()
                    foldState = !foldState
                }
            }
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