package com.example.tripsync.ui.fragment.plan.planbookmarklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.databinding.FragmentPlanBoomarkListBinding
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.fragment.setup.NaverMapFragment.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.example.tripsync.ui.fragment.setup.SharedViewModel
import com.example.tripsync.viewmodel.BookmarkManageViewModel
import com.example.tripsync.viewmodel.BookmarkManageViewModelFactory
import com.naver.maps.map.util.FusedLocationSource


class PlanBoomarkListDialog : DialogFragment() {

    private var _binding: FragmentPlanBoomarkListBinding? = null
    private val binding: FragmentPlanBoomarkListBinding
        get() = _binding!!


    private val adapter by lazy {
        PlanBookmarkListAdapter {item ->
            if (sharedViewModel.planItems.value?.size ?: 0 < 10) {
                sendItem(item)
                return@PlanBookmarkListAdapter true
            } else {
                return@PlanBookmarkListAdapter false
            }
        }
    }

    private val viewModel: BookmarkManageViewModel by viewModels { BookmarkManageViewModelFactory() }
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var locationSource: FusedLocationSource



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanBoomarkListBinding.inflate(inflater, container, false)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = WindowManager.LayoutParams.MATCH_PARENT

        dialog?.window?.setLayout(width, height)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() = with(binding) {
        planbookListReyclerview.adapter = adapter
        planbookListReyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        viewModel.bookmarkList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        planbookListClose.setOnClickListener {
            dismiss()
        }
    }

    private fun sendItem(item: Travel) {
        sharedViewModel.updatePlanBookItem(item)

    }


}