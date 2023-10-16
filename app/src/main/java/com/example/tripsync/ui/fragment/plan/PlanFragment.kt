package com.example.tripsync.ui.fragment.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.FragmentPlanBinding
import com.example.tripsync.ui.fragment.setup.PlanMemoFragment
import com.prolificinteractive.materialcalendarview.CalendarDay

class PlanFragment : Fragment() {


    private var _binding: FragmentPlanBinding? = null
    private val binding: FragmentPlanBinding
        get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlanListAdapter
    private val viewModel: PlanViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)

        binding.planEditBtn.setOnClickListener {
            showMemoDialog()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDate()

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

    private fun initView() = with(binding) {
        recyclerView = planRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PlanListAdapter{ position, plan ->

        }

        recyclerView.adapter = adapter

    }

    private fun showMemoDialog() = with(binding) {
        val dialogFragment = PlanMemoFragment(requireContext())
        dialogFragment.setOnSaveListener { memoText ->
            planTextView.text = memoText
        }
        dialogFragment.show()

    }

    private fun getDate()= with(binding) {
        val selectedDate = arguments?.getString("selectedDate")

        if(selectedDate != null) {
            planDate.text = "$selectedDate"
        }
    }
}