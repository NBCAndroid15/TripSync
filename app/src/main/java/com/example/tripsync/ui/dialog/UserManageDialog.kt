package com.example.tripsync.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.tripsync.R
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.databinding.FragmentUserManageDialogBinding
import com.example.tripsync.ui.fragment.LoginFragment
import kotlinx.coroutines.launch

class UserManageDialog : DialogFragment() {

    private var _binding : FragmentUserManageDialogBinding? = null
    private val binding : FragmentUserManageDialogBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = FragmentUserManageDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.userManageModifyPasswordBtn.setOnClickListener {
            binding.userManageDialogMain.visibility = View.GONE
            binding.userManageDialogEdit.visibility = View.VISIBLE
        }

        binding.userManageDialogPasswordBackBtn.setOnClickListener {
            binding.userManageDialogMain.visibility = View.VISIBLE
            binding.userManageDialogEdit.visibility = View.GONE
        }

        binding.userManageDialogUnregBackBtn.setOnClickListener {
            binding.userManageDialogMain.visibility = View.VISIBLE
            binding.userManageDialogConfirm.visibility = View.GONE
        }

        binding.userManageUnregisterBtn.setOnClickListener {
            binding.userManageDialogMain.visibility = View.GONE
            binding.userManageDialogConfirm.visibility = View.VISIBLE
        }

        binding.userManageDialogPasswordConfirmBtn.setOnClickListener {
            val password = binding.userManageDialogPasswordEdittext.text.toString()
            val validPassword = binding.userManageDialogPasswordValidEdittext.text.toString()

            if(password != validPassword) {
                Toast.makeText(requireContext(), "비밀번호를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (password.length < 6) {
                    Toast.makeText(requireContext(), "비밀번호를 최소 6자리 이상으로 설정해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    AuthRepositoryImpl().run {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val result = updatePassword(password)
                            if (result != null) {
                                Toast.makeText(requireContext(), "비밀번호 변경을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(requireContext(), "비밀번호 변경을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
            }
        }

        binding.userManageDialogUnregConfirmBtn.setOnClickListener {
            AuthRepositoryImpl().run {
                viewLifecycleOwner.lifecycleScope.launch {
                    val result = unregister()
                    if (result != null) {
                        Toast.makeText(requireContext(), "회원탈퇴를 완료하였습니다.", Toast.LENGTH_SHORT).show()
                        dismiss()
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.main_frame, LoginFragment.newInstance())
                            .commit()
                    } else {
                        Toast.makeText(requireContext(), "회원탈퇴를 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.userManageNoBtn.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance() =
            UserManageDialog().apply {
                arguments = Bundle().apply {
                }
            }
    }
}