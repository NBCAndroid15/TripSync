package com.trip.tripsync.ui.dialog.plan

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FragmentPlanBoomarkListBinding
import com.trip.tripsync.model.Travel
import com.trip.tripsync.util.LocationUtility
import com.trip.tripsync.viewmodel.SharedViewModel
import com.trip.tripsync.viewmodel.bookmark.BookmarkManageViewModel
import com.trip.tripsync.viewmodel.bookmark.BookmarkManageViewModelFactory
import com.google.android.gms.tasks.OnSuccessListener
import com.trip.tripsync.ui.fragment.detail.DetailFragment
import com.trip.tripsync.ui.adapter.plan.PlanBookmarkListAdapter


class PlanBoomarkListDialog : DialogFragment(), PlanBookmarkListAdapter.OnItemClickListener {

    private var _binding: FragmentPlanBoomarkListBinding? = null
    private val binding: FragmentPlanBoomarkListBinding
        get() = _binding!!

    private val adapter by lazy {
        PlanBookmarkListAdapter ({ item ->
            if (sharedViewModel.planItems.value?.size ?: 0 < 10) {
                sendItem(item)
                return@PlanBookmarkListAdapter true
            } else {
                return@PlanBookmarkListAdapter false
            }
        }, sharedViewModel.planItems, { travel ->
            sharedViewModel.planRemoveItem(travel)
        })
    }

    private val viewModel: BookmarkManageViewModel by viewModels { BookmarkManageViewModelFactory() }
    private val sharedViewModel: SharedViewModel by activityViewModels()


    private val utility : LocationUtility by lazy {
        LocationUtility(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanBoomarkListBinding.inflate(inflater, container, false)

        initView()
        adapter?.setOnItemClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = WindowManager.LayoutParams.MATCH_PARENT

        dialog?.window?.setLayout(width, height)

        binding.planBookLocation.setOnClickListener {
            nearItem()
        }
        binding.planBookAll.setOnClickListener {
            viewModel.getBookmarkList()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() = with(binding) {
        planbookListReyclerview.adapter = adapter
        planbookListReyclerview.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

        viewModel.bookmarkList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.hintText.visibility = View.VISIBLE
            } else {
                adapter?.submitList(it)
                binding.hintText.visibility = View.GONE
            }

        }

        planbookListClose.setOnClickListener {
            dismiss()
        }
    }

    private fun sendItem(item: Travel) {
        sharedViewModel.updatePlanBookItem(item)

    }

    override fun onItemClick(item: Travel) {
        val fragment = DetailFragment(item, false)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun nearItem() {
        if (hasLocationPermission()) {
            utility.requestLocationUpdate(OnSuccessListener { currentLocation ->
                if (currentLocation != null) {
                    viewModel.bookmarkItemSorted(currentLocation)
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


}