package com.trip.tripsync.ui.fragment.setup

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.trip.tripsync.R
import com.trip.tripsync.data.PlanRepositoryImpl
import com.trip.tripsync.databinding.FragmentSetupBinding
import com.trip.tripsync.model.Plan
import com.trip.tripsync.ui.fragment.MainFragment
import com.trip.tripsync.ui.fragment.plan.PlanFragment
import com.trip.tripsync.ui.dialog.setup.SetupUserAddDialog
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.trip.tripsync.ui.activity.MainActivity.Companion.PERMISSION_REQUEST_CODE
import com.trip.tripsync.ui.adapter.setup.SetupListAdapter
import com.trip.tripsync.ui.dialog.setup.SetupCalendarView
import com.trip.tripsync.ui.dialog.setup.SetupTitleDialog
import com.trip.tripsync.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

class SetupFragment : Fragment(), SetupListAdapter.OnItemClickListener {

    private var _binding: FragmentSetupBinding? = null
    private val binding: FragmentSetupBinding
        get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    private val selectedDates = mutableSetOf<CalendarDay>()
    private val adapter = SetupListAdapter()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

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

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity(), permissions,
                PERMISSION_REQUEST_CODE
            )
        }

        adapter.setOnItemClickListener(this)

        initView()
        showUserDialog()

        binding.setupBackBtn.setOnClickListener {
            requireActivity().onBackPressed()

            visibleState()
        }

        binding.setupPlanAddBtn.setOnClickListener {
            createPlan()

            val bundle = Bundle()
            bundle.putInt("initPosition", 1)
            val mainFragment = MainFragment()
            mainFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, mainFragment)
                .commit()

            visibleState()
        }

        initVisible()
        initView()

        return view
    }

    private fun isFirstRun(fragmentTag: String): Boolean {
        return sharedPreferences.getBoolean("isFirstRun_$fragmentTag", true)
    }
    private fun setFirstRunFlag(fragmentTag: String) {
        sharedPreferences.edit().putBoolean("isFirstRun_$fragmentTag", false).apply()
    }
    private fun showGuideFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, SetupGuideFragment())
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        sharedPreferences = requireContext().getSharedPreferences(
            "com.trip.tripsync.PREFERENCE_FILE_KEY",
            Context.MODE_PRIVATE
        )
        if (isFirstRun("setup")) {
            showGuideFragment()
            setFirstRunFlag("setup")
        }
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
        setupTitleBtn.text = sharedViewModel._plan.title ?: "계획 이름을 입력해주세요!"
        setupTitleBtn.setOnClickListener {
            val setupTitleDialog = SetupTitleDialog(requireContext()) { title ->
                setupTitleBtn.text = title
                sharedViewModel._plan.title = setupTitleBtn.text.toString()

                sharedViewModel.setUserVisible(true)

            }
            setupTitleDialog.show()
        }

        setupDateBtn.setOnClickListener {
            if (sharedViewModel._isDateSelected.value == true) {
                visibleState()
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
        val dateList = selectedDates.toList().map { "${it.year}년 ${it.month}월 ${it.day}일" }
        binding.setupTitleBtn.text = "계획 이름을 입력해주세요!"
        sharedViewModel.initPlan(
            binding.setupTitleBtn.text.toString(),
            selectedDates.size,
            dateList
        )
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
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .replace(R.id.main_frame, planFragment)
            .addToBackStack(null)
            .commit()
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
        if (title.isEmpty()) {
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

    private fun initVisible() = with(binding) {
        sharedViewModel.isTitleVisible.observe(viewLifecycleOwner) { isVisible ->
            setupTitleBtn.visibility = if (isVisible) View.VISIBLE else View.GONE
            setupDateCheck.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        sharedViewModel.isUserVisible.observe(viewLifecycleOwner) { isVisible ->
            setupUserAdd.visibility = if (isVisible) View.VISIBLE else View.GONE
            setupTitleCheck.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        sharedViewModel.isUserCheck.observe(viewLifecycleOwner) { isVisible ->
            setupUserCheck.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    // 버튼들의 가시성 초기화
    private fun visibleState() {
        sharedViewModel.setTitleVisible(false)
        sharedViewModel.setUserVisible(false)
        sharedViewModel.setUserCheck(false)
    }

    private fun hasPermission(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireActivity(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}





