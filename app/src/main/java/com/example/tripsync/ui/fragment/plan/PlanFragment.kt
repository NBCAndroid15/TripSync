package com.example.tripsync.ui.fragment.plan

import android.os.Bundle
import android.util.Log
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
        recyclerView = binding.planRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        initView()
        recyclerView.adapter = adapter

        binding.planEditBtn.setOnClickListener {
            showMemoDialog()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


        adapter = PlanListAdapter{ position, plan ->

        }

    }

    private fun showMemoDialog() = with(binding) {
        val dialogFragment = PlanMemoFragment(requireContext())
        dialogFragment.setOnSaveListener { memoText ->
            planTextView.text = memoText
            planEditBtn.visibility = View.GONE
        }
        dialogFragment.show()

    }

}