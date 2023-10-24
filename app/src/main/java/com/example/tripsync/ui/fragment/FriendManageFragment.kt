package com.example.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentFriendManageBinding
import com.example.tripsync.ui.adapter.FriendManageAdapter
import com.example.tripsync.ui.dialog.FriendAddDialogFragment
import com.example.tripsync.viewmodel.FriendManageViewModel
import com.example.tripsync.viewmodel.FriendManageViewModelFactory

class FriendManageFragment : Fragment() {

    private var _binding: FragmentFriendManageBinding? = null
    private val binding: FragmentFriendManageBinding
        get() = _binding!!

    private val viewModel: FriendManageViewModel by activityViewModels { FriendManageViewModelFactory() }

    private val adapter by lazy {
        FriendManageAdapter {
            viewModel.deleteFriend(it)
            Toast.makeText(requireContext(), "삭제를 완료하였습니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCurrentUserInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.init()
        _binding = FragmentFriendManageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.friendManageFriendRv.adapter = adapter
        binding.friendManageFriendRv.itemAnimator = null
        binding.friendManageFriendRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.curUser.observe(viewLifecycleOwner) {
            adapter.setList(it?.friends ?: listOf())
        }

        binding.friendManageAddBtn.setOnClickListener {
            FriendAddDialogFragment.newInstance().let {dialog ->
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "FriendAddDialog")
            }
        }
    }


    companion object {
        fun newInstance() = FriendManageFragment()
    }
}