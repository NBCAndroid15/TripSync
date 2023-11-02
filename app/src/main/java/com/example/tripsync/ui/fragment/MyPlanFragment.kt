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
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentMyPlanBinding
import com.example.tripsync.model.Plan
import com.example.tripsync.ui.adapter.MyPlanAdapter
import com.example.tripsync.ui.dialog.ConfirmFriendDialog
import com.example.tripsync.ui.fragment.plan.PlanFragment
import com.example.tripsync.ui.fragment.setup.SharedViewModel
import com.example.tripsync.viewmodel.MyPlanViewModel
import com.example.tripsync.viewmodel.MyPlanViewModelFactory
import okhttp3.internal.notify

class MyPlanFragment : Fragment() {

    private var _binding: FragmentMyPlanBinding? = null
    private val binding: FragmentMyPlanBinding
        get() = _binding!!

    private val viewModel: MyPlanViewModel by activityViewModels {
        MyPlanViewModelFactory()
    }

    private var targetPlan = Plan()

    private var curList = listOf<Plan>()

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy {
        MyPlanAdapter ({ plan, position ->
            sharedViewModel.initPlan(plan)
            sharedViewModel.initPosition(position)

            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right)
                .add(R.id.main_frame, PlanFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }, viewLifecycleOwner, viewModel.editState,
            {
                targetPlan = it
                ConfirmFriendDialog.newInstance().let { dialog ->
                    dialog.isCancelable = false
                    dialog.show(parentFragmentManager, "ConfirmFriendDialog")
                }
            })
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
            curList = it

            if (viewModel.sortOption) {
                adapter.setList(it)
            } else {
                adapter.setList(it.asReversed())
            }
        }
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listOf("최신순", "오래된순"))
        binding.mypageSortSpinner.adapter = spinnerAdapter
        binding.mypageSortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        adapter.setList(curList.asReversed())
                        viewModel.sortOption = false
                    }

                    1 -> {
                        adapter.setList(curList)
                        viewModel.sortOption = true
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.myplanEditBtn.setOnClickListener {
            viewModel.toggleEditState()
        }

        setFragmentResultListener("deleteConfirmFriend") { _, bundle ->
            if (bundle.getBoolean("isConfirmFriend")) {
                viewModel.deletePlan(targetPlan)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        clearFragmentResultListener("deleteConfirmFriend")
    }

    companion object {
        fun newInstance(): MyPlanFragment {
            return MyPlanFragment()
        }
    }
}