package com.example.tripsync.ui.fragment.setup

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripsync.R
import com.example.tripsync.data.PlanRepositoryImpl
import com.example.tripsync.databinding.FragmentSetupBinding
import com.example.tripsync.model.Plan
import com.example.tripsync.model.PlanDetail
import com.example.tripsync.model.User
import com.example.tripsync.ui.fragment.MainFragment
import com.example.tripsync.ui.fragment.MyPageFragment
import com.example.tripsync.ui.fragment.MyPlanFragment
import com.example.tripsync.ui.fragment.home.HomeFragment
import com.example.tripsync.ui.fragment.plan.PlanFragment
import com.example.tripsync.ui.fragment.setup.setupuseradd.SetupUserAddDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch

class SetupFragment : Fragment(), SetupListAdapter.OnItemClickListener {

    private var _binding: FragmentSetupBinding? = null
    private val binding: FragmentSetupBinding
        get() = _binding!!

    private val selectedDates = mutableSetOf<CalendarDay>()
    private val adapter = SetupListAdapter()
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
        showUserDialog()

        binding.setupBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, MainFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.setupPlanAddBtn.setOnClickListener {
            createPlan()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, MyPlanFragment())
                .addToBackStack(null)
                .commit()
        }

        test()
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
        setupTitleBtn.text = sharedViewModel._plan.title ?: "여행 이름을 정해주세요!"
        setupTitleBtn.setOnClickListener {
            val setupTitleDialog = SetupTitleDialog(requireContext()) { title ->
                setupTitleBtn.text = title
                sharedViewModel._plan.title = setupTitleBtn.text.toString()
                Log.d("title", setupTitleBtn.text.toString())

                sharedViewModel.setUserVisible(true)

            }
            setupTitleDialog.show()
        }

        setupDateBtn.setOnClickListener {
            if (sharedViewModel._isDateSelected.value == true) {
                sharedViewModel.setTitleVisible(false)
                sharedViewModel.setUserVisible(false)
                sharedViewModel.setUserCheck(false)
                sharedViewModel._isDateSelected.value = false
                adapter.submitList(emptyList())
            } else {
                val setupCalendarView = SetupCalendarView() { dates ->
                    onDateSelected(dates)
                }
                setupCalendarView.show(childFragmentManager, "SetupCalendarView")
            }
        }

    }

    private fun onDateSelected(selectedDates: Set<CalendarDay>) {
        Log.d("SetupFragment", "Selected Dates: $selectedDates")
        val dateList = selectedDates.toList().map { "${it.year}년 ${it.month}월 ${it.day}일" }
        binding.setupTitleBtn.text = "여행 이름을 정해주세요!"
        sharedViewModel.initPlan(binding.setupTitleBtn.text.toString(), selectedDates.size, dateList)
        adapter.submitList(dateList)

        if (selectedDates.isNotEmpty()) {
            sharedViewModel.setTitleVisible(true)
            sharedViewModel.setUserVisible(false)
            sharedViewModel.setUserCheck(false)

        } else {
            sharedViewModel.setUserVisible(false)
        }

    }

    override fun onItemClick(position: Int) {

        sharedViewModel.initPosition(position)

        val planFragment = PlanFragment()

        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,R.anim.exit_to_right)
            .replace(R.id.main_frame, planFragment)
            .addToBackStack(null)
            .commit()
        Log.d("setup", "$selectedDates")
    }

    private fun showUserDialog() = with(binding) {
        setupUserAdd.setOnClickListener {
            val fragment = SetupUserAddDialog()

            fragment.setOnDismiss {
                sharedViewModel.setUserCheck(true)
            }
            fragment.show(parentFragmentManager, "setupUserAddDialog")

        }
    }

    private fun createPlan() {
        val title = binding.setupTitleBtn.text.toString()
        if(title.isEmpty() ) {
            Toast.makeText(requireContext(), "여행 이름을 정해주세요.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        lifecycleScope.launch {
            addPlanToFirebase(sharedViewModel._plan)
        }
    }

    private suspend fun addPlanToFirebase(plan: Plan) {
        val planRepository = PlanRepositoryImpl()
        planRepository.addPlan(plan)
        Toast.makeText(requireContext(), "계획이 추가되었습니다", Toast.LENGTH_SHORT).show()

    }

    private fun test()=with(binding){
        sharedViewModel.isTitleVisible.observe(viewLifecycleOwner) { isVisible ->
            setupTitleBtn.visibility = if (isVisible) View.VISIBLE else View.GONE
            setupDateCheck.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        sharedViewModel.isUserVisible.observe(viewLifecycleOwner) { isVisible ->
            setupUserAdd.visibility = if (isVisible) View.VISIBLE else View.GONE
            setupTitleCheck.visibility = if(isVisible) View.VISIBLE else View.GONE
        }

        sharedViewModel.isUserCheck.observe(viewLifecycleOwner) { isVisible ->
            setupUserCheck.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

}





