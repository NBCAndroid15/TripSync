package com.trip.tripsync.ui.fragment.setup.setupuseradd

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
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
import com.trip.tripsync.ui.fragment.setup.SharedViewModel
import com.trip.tripsync.viewmodel.FriendManageViewModel
import com.trip.tripsync.viewmodel.FriendManageViewModelFactory

class SetupUserAddDialog : DialogFragment() {

    private var _binding: FragmentPlanUserAddDialogBinding? = null
    private val binding: FragmentPlanUserAddDialogBinding
        get() = _binding!!

    private val viewModel: FriendManageViewModel by activityViewModels { FriendManageViewModelFactory() }
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy {
        SetupUserAddDialogAdapter {item ->
            if (isUserCheck(item)) {
                Toast.makeText(requireContext(), "이미 추가된 일행입니다.", Toast.LENGTH_SHORT).show()
            } else {
                sendUserNickname(item)
                Toast.makeText(requireContext(), "추가", Toast.LENGTH_SHORT).show()
            }
        }
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
        Log.d("SetupUserAddDialog", "선택된 아이템: ${name.nickname}")
        sharedViewModel.getUserNickName(name)
    }

    private fun isUserCheck (user: User) : Boolean {
        val addUser = sharedViewModel.userList.value.orEmpty()
        return addUser.any {
            it.uid == user.uid
        }
    }
}