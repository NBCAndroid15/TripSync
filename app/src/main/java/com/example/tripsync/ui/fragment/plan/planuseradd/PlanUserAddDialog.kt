package com.example.tripsync.ui.fragment.plan.planuseradd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentPlanSearchListBinding
import com.example.tripsync.databinding.FragmentPlanUserAddDialogBinding
import com.example.tripsync.model.User
import com.example.tripsync.viewmodel.FriendManageViewModel
import com.example.tripsync.viewmodel.FriendManageViewModelFactory

class PlanUserAddDialog : DialogFragment() {

    private var _binding: FragmentPlanUserAddDialogBinding? = null
    private val binding: FragmentPlanUserAddDialogBinding
        get() = _binding!!

    private lateinit var adapter : PlanUserAddDialogAdapter
    private val viewModel: FriendManageViewModel by activityViewModels { FriendManageViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanUserAddDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = 1200

        dialog?.window?.setLayout(width, height)

        adapter = PlanUserAddDialogAdapter()
        initView()
    }

    private fun initView()= with(binding) {
        planUserRecycler.adapter = adapter
        planUserRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.curUser.observe(viewLifecycleOwner) {user ->
            if (user != null) {
                adapter.submitList(user.friends ?: listOf())
            }
        }

        planBackBtn.setOnClickListener {
            dismiss()
        }
    }
}