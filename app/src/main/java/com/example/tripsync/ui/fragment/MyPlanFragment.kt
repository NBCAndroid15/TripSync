package com.example.tripsync.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentMyPlanBinding
import com.example.tripsync.ui.adapter.MyPlanAdapter
import com.example.tripsync.ui.fragment.plan.PlanFragment
import com.example.tripsync.ui.fragment.setup.SharedViewModel
import com.example.tripsync.viewmodel.MyPlanViewModel
import com.example.tripsync.viewmodel.MyPlanViewModelFactory

class MyPlanFragment : Fragment() {

    private var _binding: FragmentMyPlanBinding? = null
    private val binding: FragmentMyPlanBinding
        get() = _binding!!

    private val viewModel: MyPlanViewModel by activityViewModels {
        MyPlanViewModelFactory()
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy {
        MyPlanAdapter { plan, position ->
            sharedViewModel.initPlan(plan)
            sharedViewModel.initPosition(position)

            parentFragmentManager
                .beginTransaction()
                .add(R.id.main_frame, PlanFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlanList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.myplanRv.adapter = adapter
        binding.myplanRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.myplanRv.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        binding.myplanRv.itemAnimator = null

        Log.d("myplanInit", "myplanInit")
        viewModel.getPlanList()
        viewModel.planList.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): MyPlanFragment {
            return MyPlanFragment()
        }
    }
}