package com.example.tripsync.ui.fragment.plan.plansearchlist

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentPlanBoomarkListBinding
import com.example.tripsync.databinding.FragmentPlanSearchListBinding
import com.example.tripsync.ui.fragment.plan.planbookmarklist.PlanBookmarkListAdapter
import com.example.tripsync.viewmodel.BookmarkManageViewModel
import com.example.tripsync.viewmodel.BookmarkManageViewModelFactory

class PlanSearchListFragment : DialogFragment() {

    private var _binding: FragmentPlanSearchListBinding? = null
    private val binding: FragmentPlanSearchListBinding
        get() = _binding!!

    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory() }


    private val adapter by lazy {
        PlanBookmarkListAdapter {item ->
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)


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

        initView()
    }

    private fun initView()= with(binding) {
        planSearchListRecycler.adapter = adapter
        planSearchListRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        planSearchListSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    performSearch(query)
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
    }

    private fun performSearch(keyword: String) {
        viewModel.updateSearchItem(keyword)

        viewModel.getSearchItem.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}