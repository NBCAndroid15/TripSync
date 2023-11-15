package com.trip.tripsync.ui.dialog.userprofile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.trip.tripsync.databinding.FragmentUserEditDialogBinding
import kotlinx.coroutines.launch

class UserEditDialogFragment : DialogFragment() {

    private var _binding: FragmentUserEditDialogBinding? = null
    private val binding: FragmentUserEditDialogBinding
        get() = _binding!!

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
        _binding = FragmentUserEditDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        binding.mypagePwSaveButton.setOnClickListener {
            val password = binding.userManagePwEdittext.text.toString().trim()
            val validPassword = binding.userManageCheckpwEdittext.text.toString().trim()

            if (password != validPassword) {
                Toast.makeText(requireContext(), "비밀번호를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(requireContext(), "비밀번호를 최소 6자리 이상으로 설정해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    val passwordResult = com.trip.tripsync.data.AuthRepositoryImpl().updatePassword(password)
                    if (passwordResult != null) {
                        Toast.makeText(requireContext(), "비밀번호 변경을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                dismiss()
            }
        }

        binding.mypagePwBackButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance() =
            UserEditDialogFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}