package com.example.tripsync.ui.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentUserManageDialogBinding

class UserManageDialog : Fragment() {

    private var _binding : FragmentUserManageDialogBinding? = null
    private val binding : FragmentUserManageDialogBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserManageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            UserManageDialog().apply {
                arguments = Bundle().apply {
                }
            }
    }
}