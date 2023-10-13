package com.example.tripsync.ui.fragment.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripsync.databinding.FragmentPlanBinding
import com.example.tripsync.model.Plan

class PlanFragment : Fragment() {

    private var _binding: FragmentPlanBinding? = null
    private val binding: FragmentPlanBinding
        get() = _binding!!

    private lateinit var planListAdapter: PlanListAdapter
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)

        planListAdapter = PlanListAdapter{ position, plan ->

        }

        binding.planRecycler.adapter = planListAdapter
        binding.planRecycler.layoutManager = LinearLayoutManager(context)

        binding.planEditBtn.setOnClickListener {
            isEditMode = !isEditMode
            updateView()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //doSomething
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlanFragment {
            return PlanFragment()
        }
    }

    private fun updateView() = with(binding) {
        if(isEditMode) {
            planListAdapter.submitList(getEditedPlanList())
        } else {
            planListAdapter.submitList(getNormalPlanList())
        }
        }

    private fun getEditedPlanList(): List<Plan> {
        val editedPlans = mutableListOf<Plan>()
        for (originalPlan in planListAdapter.currentList) {
            val editedPlan = originalPlan.copy(viewType = PlanViewType.Edit.INT)
            editedPlans.add(editedPlan)
        }
        return editedPlans

    }

    private fun getNormalPlanList(): List<Plan> {
        val normalPlans = mutableListOf<Plan>()
        for (originalPlan in planListAdapter.currentList) {
            val normalPlan = originalPlan.copy(viewType = PlanViewType.Normal.INT)
            normalPlans.add(normalPlan)
        }
        return normalPlans
    }
}