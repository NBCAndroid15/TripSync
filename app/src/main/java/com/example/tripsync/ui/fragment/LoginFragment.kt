package com.example.tripsync.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tripsync.R
import com.example.tripsync.databinding.FragmentLoginBinding
import com.example.tripsync.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var gooleSignInClient: GoogleSignInClient

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        // Google 로그인 옵션 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        gooleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Google 로그인 버튼 클릭
        binding.loginGoogleButton.setOnClickListener {
            signInGoogle()
        }

        // 일반 로그인 버튼 클릭 -> 메인 페이지
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
    // Google Intent를 얻어와서 로그인을 시작
    private fun signInGoogle() {
        val signInIntent = gooleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    // Google 로그인 결과 데이터에서 로그인 정보 추출
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    // Google 로그인 결과 처리
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            // 로그인 성공 시 Account에서 사용자 정보 가져옴
            val account : GoogleSignInAccount? = task.result
            if (account != null) {
                // 사용자 정보를 Firebase Authentication으로 전달하여 로그인 완료
                updateUi(account)

                val email = account.email
                val displayName = account.displayName
                // 사용자 정보 DB에 저장
                saveUserInfoToDatabase(email, displayName)
            }
        }
    }

    private fun saveUserInfoToDatabase(email: String?, displayName: String?) {
        if (email != null && displayName != null) {
            val db = FirebaseFirestore.getInstance()
            val usersRef = db.collection("users")

            // 사용자 정보를 Firebase Firestore에 저장
            val user = User(email = email, nickname = displayName)
            usersRef.document(auth.currentUser!!.uid).set(user)
                .addOnSuccessListener {
                    Toast.makeText(context, "Google 로그인을 통해 가입되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Google 로그인이 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Firebase Authentication 사용자 정보로 로그인 완료
    private fun updateUi(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, MainFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    // 일반 로그인, DB 정보와 일치할 경우에만 가능
    private fun login(inputId: String, inputPw: String) {
        if (inputPw.length >= 6) {
            auth.signInWithEmailAndPassword(inputId, inputPw)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frame, MainFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    } else {
                        Log.d("logincheck", result.exception.toString())
                        Toast.makeText(context, "로그인 정보가 올바르지 않습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "비밀번호가 너무 짧습니다. 최소 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 이메일 형식 체크
    private fun isLoginChecked(email: String) : Boolean {
        val regexPattern = "^(?=.*[A-Za-z])(?=.*[@#$%^&+=])(?=\\S+$).{1,}"
        val pattern = Pattern.compile(regexPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
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