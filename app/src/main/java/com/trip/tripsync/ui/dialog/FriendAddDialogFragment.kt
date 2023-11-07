package com.trip.tripsync.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.trip.tripsync.databinding.FragmentFriendAddDialogBinding
import com.trip.tripsync.ui.adapter.FriendAddAdapter
import com.trip.tripsync.viewmodel.FriendManageViewModel
import com.trip.tripsync.viewmodel.FriendManageViewModelFactory

class FriendAddDialogFragment : DialogFragment() {

    private var _binding: FragmentFriendAddDialogBinding? = null
    private val binding: FragmentFriendAddDialogBinding
        get() = _binding!!
    private val viewModel: FriendManageViewModel by activityViewModels { FriendManageViewModelFactory() }
    private val adapter = FriendAddAdapter {
        viewModel.addFriend(it)
        Toast.makeText(requireContext(), "친구 추가를 완료하였습니다", Toast.LENGTH_SHORT).show()
    }

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
        _binding = FragmentFriendAddDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.friendAddDialogFriendRv.adapter = adapter
        binding.friendAddDialogFriendRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.friendList.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }

        binding.friendAddNoBtn.setOnClickListener {
            dismiss()
        }

        binding.friendAddDialogSearchBtn.setOnClickListener {
            viewModel.getFriendList(binding.friendAddDialogSearchText.text.toString())
        }

        binding.friendAddDialogSearchText.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.friendAddDialogSearchBtn.performClick()
                handled = true
            }
            handled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance() =
            FriendAddDialogFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}