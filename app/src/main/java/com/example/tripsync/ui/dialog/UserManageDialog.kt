package com.example.tripsync.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.tripsync.R
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.databinding.FragmentUserManageDialogBinding
import com.example.tripsync.ui.fragment.LoginFragment
import com.example.tripsync.ui.fragment.MyPageFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UserManageDialog : DialogFragment() {

    private lateinit var auth: FirebaseAuth
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

        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")
        val currentUser = auth.currentUser
        val currentUserUid = auth.currentUser?.uid

        // 현재 유저 이메일
        val email = currentUser?.email
        binding.userManageIdCurrent.text = email

        // 현재 유저 닉네임 불러오기
        if (currentUserUid != null) {
            usersRef.document(currentUserUid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val nickname = documentSnapshot.getString("nickname")
                        binding.mypageProfileNickname.text = nickname
                    }
                }
        }

        // 변경한 정보 저장 이벤트
        binding.userManageSaveBtn.setOnClickListener {
            val password = binding.userManagePwEdittext.text.toString().trim()
            val validPassword = binding.userManageCheckpwEdittext.text.toString().trim()
            val nickname = binding.userManageNicknameEdittext.text.toString().trim()

            when {
                (password != validPassword) -> {
                    Toast.makeText(requireContext(), "비밀번호를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show()
                }
                (password.length < 6) -> {
                    Toast.makeText(requireContext(), "비밀번호를 최소 6자리 이상으로 설정해주세요.", Toast.LENGTH_SHORT).show()
                }
                (nickname.isEmpty()) -> {
                    Toast.makeText(requireContext(), "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val isAvailable = AuthRepositoryImpl().checkNickname(nickname)
                        if (isAvailable) {
                            val passwordResult = AuthRepositoryImpl().updatePassword(password)
                            if (passwordResult != null) {
                                val profileUpdate = UserProfileChangeRequest.Builder()
                                    .setDisplayName(nickname)
                                    .build()
                                currentUser?.updateProfile(profileUpdate)
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val userDocRef = usersRef.document(currentUser.uid)
                                            val userData = hashMapOf("nickname" to nickname)
                                            userDocRef.update(userData as Map<String, Any>)
                                            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                                            parentFragmentManager
                                                .beginTransaction()
                                                .replace(R.id.main_frame, MyPageFragment.newInstance())
                                                .commit()
                                            Toast.makeText(requireContext(), "회원정보 변경을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            val error = task.exception?.message
                                            if (error != null) {
                                                Log.e("PasswordUpdate", error)
                                            }
                                            Toast.makeText(requireContext(), "회원정보 변경을 실패하였습니다.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        } else {
                            Toast.makeText(requireContext(), "닉네임이 이미 사용 중입니다. 다른 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.userManageNncheckButton.setOnClickListener {
            val inputNickname = binding.userManageNicknameEdittext.text.toString().trim()
            binding.userManageNicknamePass.visibility = View.GONE
            binding.userManageNicknameFailed.visibility = View.GONE

            usersRef.whereEqualTo("nickname", inputNickname).get()
                .addOnSuccessListener {documents ->
                    if (documents.isEmpty && inputNickname.isNotEmpty()) {
                        binding.userManageNicknamePass.visibility = View.VISIBLE
                    } else if (inputNickname.isEmpty()) {
                        binding.userManageNicknamePass.visibility = View.GONE
                    }else {
                        binding.userManageNicknameFailed.visibility = View.VISIBLE
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