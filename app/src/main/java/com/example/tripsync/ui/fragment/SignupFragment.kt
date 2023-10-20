package com.example.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentSignupBinding
import com.example.tripsync.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        fun signup(inputId: String, inputPw: String, inputNickname: String) {

            auth.createUserWithEmailAndPassword(inputId, inputPw)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful && auth.currentUser != null) {
                        val user = User(email = inputId, nickname = inputNickname)
                        usersRef.document(auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.main_frame, LoginFragment())
                                    .addToBackStack(null)
                                    .commit()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "회원가입에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                    } else if (result.exception?.message.isNullOrEmpty()) {
                        Toast.makeText(context, "올바르지 않은 정보가 있습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // 아이디, 닉네임 중복검사 'onCheckComplete' -> 중복 여부
        fun checkEmailAndNickname(inputId: String, inputNickname: String, onCheckComplete: (Boolean) -> Unit) {
            binding.signupIdPass.visibility = View.GONE
            binding.signupIdFailed.visibility = View.GONE
            binding.signupNicknamePass.visibility = View.GONE
            binding.signupNicknameFailed.visibility = View.GONE

            usersRef.whereEqualTo("email", inputId).get()
                .addOnSuccessListener { idDocuments ->
                    if (idDocuments.isEmpty) {
                        // email 필드가 inputId와 일치하는 중복된 정보가 없어서 통과하고 이어서 nickname 검열
                        usersRef.whereEqualTo("nickname", inputNickname).get()
                            .addOnSuccessListener { nicknameDocuments ->
                                if (nicknameDocuments.isEmpty) {
                                    // 즉 id와 nickname 모두 중복되지 않기 때문에 true를 반환
                                    onCheckComplete(true)
                                } else {
                                    // id는 중복되는 정보가 없어 통과했지만 nickname이 중복이 있음
                                    onCheckComplete(false)
                                    Toast.makeText(context, "이미 존재하는 닉네임입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        // 애초에 inputId와 중복된 정보가 있어 false 반환
                        onCheckComplete(false)
                        Toast.makeText(context, "이미 존재하는 아이디입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // 회원가입 버튼 클릭 -> 로그인 페이지
        binding.signupButton.setOnClickListener {
            val inputId = binding.signupIdEdittext.text.toString().trim()
            val inputPw = binding.signupPwEdittext.text.toString().trim()
            val inputPwRe = binding.signupCheckpwEdittext.text.toString().trim()
            val inputNickname = binding.signupNicknameEdittext.text.toString().trim()

            if (inputId.isNotEmpty() && inputPw.isNotEmpty() && inputPwRe.isNotEmpty() && inputNickname.isNotEmpty()) {
                if (inputPw == inputPwRe && inputPw.length >= 6) {
                    checkEmailAndNickname(inputId, inputNickname) { isAvailable ->
                        if (isAvailable) {
                            signup(inputId, inputPw, inputNickname)
                            Toast.makeText(context, "회원가입 성공!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "비밀번호가 일치하지 않습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "회원가입 정보를 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 이메일 형식 체크
        fun isLoginChecked(email: String) : Boolean {
            val regexPattern = "^(?=.*[A-Za-z])(?=.*[@#$%^&+=])(?=\\S+$).{1,}"
            val pattern = Pattern.compile(regexPattern)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        // 아이디 중복 검사 버튼클릭 msg
        binding.signupIdcheckButton.setOnClickListener {
            val inputId = binding.signupIdEdittext.text.toString().trim()
            binding.signupIdPass.visibility = View.GONE
            binding.signupIdFailed.visibility = View.GONE
            usersRef.whereEqualTo("email", inputId).get()
                .addOnSuccessListener {documents ->
                    if (documents.isEmpty && isLoginChecked(inputId) && inputId.isNotEmpty()) {
                        binding.signupIdPass.visibility = View.VISIBLE
                    } else if (inputId.isEmpty()) {
                        binding.signupIdPass.visibility = View.GONE
                    } else {
                        binding.signupIdFailed.visibility = View.VISIBLE
                    }
                }
        }

        // 닉네임 중복 검사 버튼클릭 msg
        binding.signupNncheckButton.setOnClickListener {
            val inputNickname = binding.signupNicknameEdittext.text.toString().trim()
            binding.signupNicknamePass.visibility = View.GONE
            binding.signupNicknameFailed.visibility = View.GONE
            usersRef.whereEqualTo("nickname", inputNickname).get()
                .addOnSuccessListener {documents ->
                    if (documents.isEmpty && inputNickname.isNotEmpty()) {
                        binding.signupNicknamePass.visibility = View.VISIBLE
                    } else if (inputNickname.isEmpty()) {
                        binding.signupNicknamePass.visibility = View.GONE
                    }else {
                        binding.signupNicknameFailed.visibility = View.VISIBLE
                    }
                }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //doSomething
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): SignupFragment {
            return SignupFragment()
        }
    }
}