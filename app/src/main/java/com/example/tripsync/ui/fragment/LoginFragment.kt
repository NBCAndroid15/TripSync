package com.example.tripsync.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentLoginBinding
import com.example.tripsync.ui.fragment.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    lateinit var inputId: EditText
    lateinit var inputPw: EditText
    lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        // 이메일 형식 체크
        fun isLoginChecked(email: String) : Boolean {
            val regexPattern = "^(?=.*[A-Za-z])(?=.*[@#$%^&+=])(?=\\S+$).{1,}"
            val pattern = Pattern.compile(regexPattern)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        // DB 정보와 일치할 경우에만 로그인
        fun login(inputId: String, inputPw: String) {
            if (inputPw.length >= 6) {
                auth.signInWithEmailAndPassword(inputId, inputPw)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.main_frame, HomeFragment())
                                .addToBackStack(null)
                                .commit()
                        } else {
                            Toast.makeText(context, "로그인 정보가 올바르지 않습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "비밀번호가 너무 짧습니다. 최소 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 로그인 버튼 클릭 -> 메인 페이지
        binding.loginButton.setOnClickListener {
            val inputId = binding.loginIdEdittext.text.toString().trim()
            val inputPw = binding.loginPwEdittext.text.toString().trim()
            if (inputId.isNotEmpty() && inputPw.isNotEmpty() && isLoginChecked(inputId)) {
                login(inputId, inputPw)
            } else {
                Toast.makeText(context, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 버튼 클릭 -> 회원가입 페이지
        binding.signupButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_frame, SignupFragment())
                .addToBackStack(null)
                .commit()
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
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}