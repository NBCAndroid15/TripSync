package com.trip.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.trip.tripsync.databinding.FragmentFriendManageBinding
import com.trip.tripsync.model.User
import com.trip.tripsync.ui.adapter.friend.FriendManageAdapter
import com.trip.tripsync.ui.dialog.confirm.ConfirmFriendDialog
import com.trip.tripsync.ui.dialog.FriendAddDialogFragment
import com.trip.tripsync.viewmodel.friendmanage.FriendManageViewModel
import com.trip.tripsync.viewmodel.friendmanage.FriendManageViewModelFactory

class FriendManageFragment : Fragment() {

    private var _binding: FragmentFriendManageBinding? = null
    private val binding: FragmentFriendManageBinding
        get() = _binding!!

    private val viewModel: FriendManageViewModel by activityViewModels { FriendManageViewModelFactory() }
    var targetUser = User()

    private val adapter by lazy {
        FriendManageAdapter {
            targetUser = it
            ConfirmFriendDialog.newInstance().let { dialog ->
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "ConfirmFriendDialog")
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        clearFragmentResultListener("deleteConfirmFriend")
    }

    private fun initView() {
        binding.friendManageFriendRv.adapter = adapter
        binding.friendManageFriendRv.itemAnimator = null
        binding.friendManageFriendRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.friendManageHintText.visibility = View.VISIBLE

        viewModel.curUser.observe(viewLifecycleOwner) {
            val result = it?.friends ?: listOf()

            if(result.isEmpty()) {
                adapter.setList(result)
                binding.friendManageFriendRv.visibility = View.GONE
                binding.friendManageHintText.visibility = View.VISIBLE
            } else {
                binding.friendManageHintText.visibility = View.GONE
                binding.friendManageFriendRv.visibility = View.VISIBLE
                adapter.setList(result)
            }


        }

        binding.friendManageAddBtn.setOnClickListener {
            FriendAddDialogFragment.newInstance().let {dialog ->
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "FriendAddDialog")
            }
        }

        setFragmentResultListener("deleteConfirmFriend") { _, bundle ->
            if (bundle.getBoolean("isConfirmFriend")) {
                viewModel.deleteFriend(targetUser)
            }

        }
    }


    companion object {
        fun newInstance() = FriendManageFragment()
    }
}