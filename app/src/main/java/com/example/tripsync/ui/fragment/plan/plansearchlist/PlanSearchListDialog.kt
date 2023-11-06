package com.example.tripsync.ui.fragment.plan.plansearchlist

import android.app.Activity
import android.location.Location
import android.os.Bundle
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
import com.example.tripsync.databinding.FragmentPlanSearchListBinding
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.fragment.plan.LocationUtility
import com.example.tripsync.ui.fragment.setup.SharedViewModel
import com.google.android.gms.tasks.OnSuccessListener


class PlanSearchListDialog : DialogFragment() {

    private var _binding: FragmentPlanSearchListBinding? = null
    private val binding: FragmentPlanSearchListBinding
        get() = _binding!!

    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory() }
    private val sharedViewModel: SharedViewModel by activityViewModels()



    private val adapter by lazy {
        PlanSearchListAdapter {item ->
            if(sharedViewModel.planItems.value?.size ?: 0 < 10) {
                sendItem(item)
                return@PlanSearchListAdapter true
            } else {
                return@PlanSearchListAdapter false
            }
        }
    }

//    private val utility : LocationUtility by lazy {
//        LocationUtility(requireContext())
//    }


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

        binding.planSearchLocation.setOnClickListener {
//            nearItem()
        }
        binding.planSearchAll.setOnClickListener {
        }



        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
//        utility.stopLocationUpdate()
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

        planSearchClose.setOnClickListener {
            dismiss()
        }

        planSearchListRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)?.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)
                if (!planSearchListRecycler.canScrollVertically(1) && lastItemPosition == itemTotalCount){
                    performSearch(planSearchListSearch.query.toString())

                }

            }
        })

//        planSearchListSearch.isSubmitButtonEnabled = true
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

    companion object {
        fun newInstance(): PlanSearchListDialog {
            return PlanSearchListDialog()
        }
    }

    private fun sendItem(item: Travel) {
        sharedViewModel.updatePlanSearchItem(item)
    }

//    private fun nearItem() {
//        utility.requestLocationUpdate(OnSuccessListener { currentLocation ->
//            if (currentLocation != null) {
//                viewModel.searchItemSorted(currentLocation)
//            }
//        })
//    }





//    private fun nearItem(item: List<Travel>) {
//        utility.requestLocationUpdate(OnSuccessListener { currentLocation ->
//            if (currentLocation != null) {
//                val sortedItems = item.sortedBy { item ->
//                    val itemLocation = android.location.Location("itemLocation")
//                    itemLocation.latitude = item.mapY ?: 0.0
//                    itemLocation.longitude = item.mapX ?: 0.0
//                    val distance = currentLocation.distanceTo(itemLocation) / 1000
//                    distance
//                }
//
//                adapter.submitList(sortedItems)
//
//
//            }
//        })
//    }




}