package com.example.tripsync.ui.fragment.plan.plansearchlist

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentPlanBoomarkListBinding
import com.example.tripsync.databinding.FragmentPlanSearchListBinding
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.fragment.plan.PlanFragment
import com.example.tripsync.ui.fragment.plan.planbookmarklist.PlanBookmarkListAdapter
import com.example.tripsync.ui.fragment.setup.SharedViewModel
import com.example.tripsync.viewmodel.BookmarkManageViewModel
import com.example.tripsync.viewmodel.BookmarkManageViewModelFactory

class PlanSearchListFragment : DialogFragment() {

    private var _binding: FragmentPlanSearchListBinding? = null
    private val binding: FragmentPlanSearchListBinding
        get() = _binding!!

    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory() }
    private val sharedViewModel: SharedViewModel by activityViewModels()


    private val adapter by lazy {
        PlanSearchListAdapter {item ->
            sendItem(item)
        }
    }

    override fun onResume() {
        super.onResume()


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanSearchListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = WindowManager.LayoutParams.MATCH_PARENT

        dialog?.window?.setLayout(width, height)

        initView()
    }

    private fun initView()= with(binding) {
        planSearchListRecycler.adapter = adapter
        planSearchListRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        planSearchListSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(keyword: String): Boolean {
                if (keyword != null && keyword.isNotEmpty()) {
                    performSearch(keyword)
                    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(planSearchListSearch.windowToken, 0)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.isNotEmpty()) {

                    } else {
                    }
                }
                return true
            }
        })

        viewModel.getSearchItem.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        planSearchClose.setOnClickListener {
            dismiss()
        }
    }

    private fun performSearch(keyword: String) {
        Log.d("PlanSearchListFragment", "Searching for: $keyword") // 검색어 로그

        viewModel.updateSearchItem(keyword)

        viewModel.getSearchItem.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            Log.d("PlanSearchListFragment", "Search results: $it") // 검색 결과 로그

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlanSearchListFragment {
            return PlanSearchListFragment()
        }
    }

    private fun sendItem(item: Travel) {
        sharedViewModel.updatePlanSearchItem(item)
    }


}