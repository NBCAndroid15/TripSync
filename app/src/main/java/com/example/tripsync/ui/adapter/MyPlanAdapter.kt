package com.example.tripsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.tripsync.R
import com.example.tripsync.databinding.MyplanPlanItemBinding
import com.example.tripsync.model.Plan
import com.example.tripsync.model.PlanDetail
import com.example.tripsync.ui.collapse
import com.example.tripsync.ui.expand

class MyPlanAdapter(private val gotoPlan : (Plan, Int) -> Unit) : RecyclerView.Adapter<MyPlanAdapter.ViewHolder>(){
    private var planList = listOf<Plan>()
    
    class ViewHolder (val binding : MyplanPlanItemBinding, private val parentAdapter: MyPlanAdapter, private val gotoPlan : (Plan, Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: Plan) {
            binding.myplanDetailRv.visibility = View.GONE
            binding.myplanPlanPlusbtn.visibility = View.VISIBLE
            binding.myplanPlanMinusbtn.visibility = View.GONE

            Glide.with(binding.root)
                .load(plan.planDetailList?.getOrNull(0)?.travelList?.getOrNull(0)?.imageUrl ?: "")
                .error(R.drawable.item_error)
                .into(binding.myplanPlanThumbnail)

            val adapter = MyPlanDetailAdapter(parentAdapter, gotoPlan)
            binding.myplanDetailRv.adapter = adapter
            binding.myplanDetailRv.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            adapter.setList(plan.planDetailList ?: listOf())
            binding.myplanPlanTitle.text = plan.title


            binding.myplanPlanPlusbtn.setOnClickListener {
                binding.myplanPlanPlusbtn.visibility = View.GONE
                binding.myplanPlanMinusbtn.visibility = View.VISIBLE
                binding.myplanDetailRv.expand()
            }

            binding.myplanPlanMinusbtn.setOnClickListener {
                binding.myplanPlanMinusbtn.visibility = View.GONE
                binding.myplanPlanPlusbtn.visibility = View.VISIBLE
                binding.myplanDetailRv.collapse()
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