package com.example.tripsync.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right)
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




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView() {
        viewModel.getPlanListSnapshot()
        binding.myplanRv.adapter = adapter
        binding.myplanRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.myplanRv.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        binding.myplanRv.itemAnimator = null

        Log.d("myplanInit", "myplanInit")

        viewModel.planList.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listOf("최신순", "오래된순"))
        binding.mypageSortSpinner.adapter = adapter
        binding.mypageSortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
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