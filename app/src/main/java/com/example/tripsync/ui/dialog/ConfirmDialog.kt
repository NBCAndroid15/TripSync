package com.example.tripsync.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.tripsync.databinding.FragmentConfirmDialogBinding
import com.example.tripsync.model.Travel


class ConfirmDialog : DialogFragment() {

    private var _binding: FragmentConfirmDialogBinding? = null
    private val binding: FragmentConfirmDialogBinding
        get() = _binding!!
    private lateinit var travel: Travel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        travel = arguments?.getParcelable("travel") ?: Travel()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = FragmentConfirmDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.confirmYesBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("travel", travel)
                putBoolean("isConfirm", true)
            }
            setFragmentResult("deleteConfirm", bundle)
            dismiss()
        }

        binding.confirmNoBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("travel", travel)
                putBoolean("isConfirm", false)
            }
            setFragmentResult("deleteConfirm", bundle)
            dismiss()
        }
    }

    companion object {
        fun newInstance(travel: Travel): ConfirmDialog {
            val bundle = Bundle()

            return ConfirmDialog().apply {
                bundle.putParcelable("travel", travel)
                arguments = bundle
            }
        }
    }
}