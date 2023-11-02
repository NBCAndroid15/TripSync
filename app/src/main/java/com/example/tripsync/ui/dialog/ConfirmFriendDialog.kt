package com.example.tripsync.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.tripsync.databinding.FragmentConfirmFriendDialogBinding
import com.example.tripsync.model.User


class ConfirmFriendDialog : DialogFragment() {

    private var _binding: FragmentConfirmFriendDialogBinding? = null
    private val binding: FragmentConfirmFriendDialogBinding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = FragmentConfirmFriendDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.confirmYesBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean("isConfirmFriend", true)
            }
            setFragmentResult("deleteConfirmFriend", bundle)
            dismiss()
        }

        binding.confirmNoBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putBoolean("isConfirmFriend", false)
            }
            setFragmentResult("deleteConfirmFriend", bundle)
            dismiss()
        }
    }

    companion object {
        fun newInstance(): ConfirmFriendDialog {
            val bundle = Bundle()

            return ConfirmFriendDialog().apply {
                arguments = bundle
            }
        }
    }
}