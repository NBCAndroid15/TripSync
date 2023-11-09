package com.trip.tripsync.ui.fragment.plan.plansearchlist

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trip.tripsync.databinding.FragmentPlanSearchListBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.ui.fragment.plan.LocationUtility
import com.trip.tripsync.ui.fragment.setup.SharedViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.trip.tripsync.R
import com.trip.tripsync.ui.fragment.DetailFragment
import com.trip.tripsync.ui.fragment.setup.SetupFragment


class PlanSearchListDialog : DialogFragment(), PlanSearchListAdapter.OnItemClickListener {

    private var _binding: FragmentPlanSearchListBinding? = null
    private val binding: FragmentPlanSearchListBinding
        get() = _binding!!

    private val viewModel: SearchViewModel by viewModels { SearchViewModelFactory() }
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy {
        PlanSearchListAdapter ({item ->
            if(sharedViewModel.planItems.value?.size ?: 0 < 10) {
                sendItem(item)
                return@PlanSearchListAdapter true
            } else {
                return@PlanSearchListAdapter false
            }
        }, sharedViewModel.planItems, { travel ->
            sharedViewModel.planRemoveItem(travel)
        })
    }

    private val utility : LocationUtility by lazy {
        LocationUtility(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanSearchListBinding.inflate(inflater, container, false)

        adapter.setOnItemClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = WindowManager.LayoutParams.MATCH_PARENT

        dialog?.window?.setLayout(width, height)

        binding.planSearchLocation.setOnClickListener {
            nearItem()
        }
        binding.planSearchAll.setOnClickListener {
        }


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

    override fun onItemClick(item: Travel) {
        val detailFragment = DetailFragment(item)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun nearItem() {
        if (hasLocationPermission()) {
            utility.requestLocationUpdate(OnSuccessListener { currentLocation ->
                if (currentLocation != null) {
                    viewModel.searchItemSorted(currentLocation)
                }
            })
        } else {
            showPermissionSettingsDialog()
        }

    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionSettingsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("권한 필요")
        builder.setMessage("위치 권한이 필요합니다. 설정에서 권한을 변경하세요.")
        builder.setPositiveButton("설정으로 이동") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }











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