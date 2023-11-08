package com.trip.tripsync.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.trip.tripsync.R
import com.trip.tripsync.databinding.FragmentFriendAddDialogBinding
import com.trip.tripsync.databinding.FragmentUserCheckDialogBinding
import com.trip.tripsync.ui.fragment.plan.PlanUserDialogAdapter
import com.trip.tripsync.ui.fragment.plan.PlanUserNameAdapter
import com.trip.tripsync.ui.fragment.setup.SharedViewModel

class UserCheckDialogFragment : DialogFragment() {

    private var _binding: FragmentUserCheckDialogBinding? = null
    private val binding: FragmentUserCheckDialogBinding
        get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var userAdapter : PlanUserDialogAdapter

    override fun onStart() {
        super.onStart()

        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = FragmentUserCheckDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        userAdapter = PlanUserDialogAdapter()
        binding.userCheckRv.adapter = userAdapter
        binding.userCheckRv.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val userTotalCount = binding.userTotalCount
        sharedViewModel.userList.observe(viewLifecycleOwner, Observer { name ->
            userAdapter.submitList(name)
            userTotalCount.text = "총 ${userAdapter.currentList.size.toString()} 명"
        })

        binding.userCheckBackBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance() : UserCheckDialogFragment {
            return UserCheckDialogFragment()
        }
    }
}