package com.example.tripsync.ui.fragment.setup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentSetupBinding
import com.example.tripsync.ui.fragment.plan.PlanFragment
import com.prolificinteractive.materialcalendarview.CalendarDay

class SetupFragment : Fragment(), SetupListAdapter.OnItemClickListener {

    private var _binding: FragmentSetupBinding? = null
    private val binding: FragmentSetupBinding
        get() = _binding!!

    private val selectedDates = mutableSetOf<CalendarDay>()
    private val adapter = SetupListAdapter()

    private val setupViewModel: SetupViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView = binding.setupRecycler
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(this)

        initView()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): SetupFragment {
            return SetupFragment()
        }
    }

    private fun initView() = with(binding) {
        setupTitleBtn.setOnClickListener {
            val setupTitleDialog = SetupTitleDialog(requireContext()) { title ->
                setupTitleBtn.text = title
                sharedViewModel.updateSharedTitle(setupTitleBtn.text as String)

            }
            setupTitleDialog.show()
        }

        sharedViewModel.sharedTitle.observe(viewLifecycleOwner, Observer {
            setupTitleBtn.text = it as String
        })

        setupDateBtn.setOnClickListener {
            val setupCalendarView = SetupCalendarView(selectedDates) { dates ->
                onDateSelected(dates)
            }
            setupCalendarView.show(childFragmentManager, "SetupCalendarView")
        }

    }

    fun onDateSelected(selectedDates: Set<CalendarDay>) {
        Log.d("SetupFragment", "Selected Dates: $selectedDates")
//        this.selectedDates.clear()
//        this.selectedDates.addAll(selectedDates)
        adapter.submitList(selectedDates.toList())
    }

    override fun onItemClick(date: CalendarDay) {

        sharedViewModel.updateSharedDate(setOf(date))

        val planFragment = PlanFragment()
        val bundle = Bundle()
        bundle.putParcelable("selectedDate", date)
        planFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, planFragment)
            .addToBackStack(null)
            .commit()
        Log.d("setup", "$selectedDates")
    }

}





