package com.trip.tripsync.ui.dialog.setup

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.trip.tripsync.databinding.FragmentPlanUserAddDialogBinding
import com.trip.tripsync.model.User
import com.trip.tripsync.ui.adapter.setup.SetupUserAddDialogAdapter
import com.trip.tripsync.viewmodel.SharedViewModel
import com.trip.tripsync.viewmodel.friendmanage.FriendManageViewModel
import com.trip.tripsync.viewmodel.friendmanage.FriendManageViewModelFactory

class SetupUserAddDialog : DialogFragment() {

    private var _binding: FragmentPlanUserAddDialogBinding? = null
    private val binding: FragmentPlanUserAddDialogBinding
        get() = _binding!!

    private val viewModel: FriendManageViewModel by activityViewModels { FriendManageViewModelFactory() }
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy {
        SetupUserAddDialogAdapter ({item ->
            if (isUserCheck(item)) {
                isUserDelete(item)
                Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                sendUserNickname(item)
                Toast.makeText(requireContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }, sharedViewModel.userList)
    }

    private var onDissListener : (() -> Unit)? = null


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
        viewModel.init()

        viewModel.curUser.observe(viewLifecycleOwner) {user ->
            if (user != null) {
                adapter.submitList(user.friends ?: listOf())
            }
        }
        initView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        onDissListener?.invoke()

    }

    fun setOnDismiss (listener: () -> Unit) {
        onDissListener = listener
    }

    private fun initView()= with(binding) {
        planUserRecycler.adapter = adapter
        planUserRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        planBackBtn.setOnClickListener {
            dismiss()
        }

    }

    private fun sendUserNickname(name: User) {
        sharedViewModel.getUserNickName(name)
    }

    private fun isUserCheck (user: User) : Boolean {
        val addUser = sharedViewModel.userList.value.orEmpty()
        return addUser.any {
            it.uid == user.uid
        }
    }

    private fun isUserDelete(user:User) {
        sharedViewModel.planUserRemove(user)
    }
}