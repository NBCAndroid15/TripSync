package com.example.tripsync.ui.dialog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tripsync.R
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.databinding.FragmentUserManageBinding
import com.example.tripsync.ui.fragment.MyPageFragment
import com.example.tripsync.ui.fragment.SignOutDialogFragment
import com.example.tripsync.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class UserManageDialog : Fragment() {

    private val PICK_FROM_ALBUM = 1

    private lateinit var auth: FirebaseAuth
    private var _binding : FragmentUserManageBinding? = null
    private val binding : FragmentUserManageBinding
        get() = _binding!!

    private var selectedImageUri: Uri? = null
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserManageBinding.inflate(inflater, container, false)

        binding.userManageProfileBg.clipToOutline = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        userProfileViewModel.getImageUrl().observe(viewLifecycleOwner, Observer { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.userManageProfileBg)
        })

        userProfileViewModel.loadImageUrlFromDatabase()
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

        // 비밀번호 변경 저장
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
                    val passwordResult = AuthRepositoryImpl().updatePassword(password)
                    if (passwordResult != null) {
                        Toast.makeText(requireContext(), "비밀번호 변경을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 닉네임 변경 저장
        binding.mypageNicknameSaveButton.setOnClickListener {
            val nickname = binding.userManageNicknameEdittext.text.toString().trim()

            if (nickname.isEmpty()) {
                Toast.makeText(requireContext(), "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    val isAvailable = AuthRepositoryImpl().checkNickname(nickname)
                    if (isAvailable) {
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(nickname)
                            .build()
                        currentUser?.updateProfile(profileUpdate)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userDocRef = usersRef.document(currentUser.uid)
                                    val userData = hashMapOf("nickname" to nickname)
                                    userDocRef.update(userData as Map<String, Any>)
                                    Toast.makeText(requireContext(), "닉네임 변경을 완료하였습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(requireContext(), "닉네임이 이미 사용 중입니다. 다른 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 뒤로가기 버튼
        binding.userManageBackBtn.setOnClickListener {
            parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.main_frame, MyPageFragment.newInstance())
                .commit()
        }

        // 회원탈퇴 다이얼로그
        binding.userManageSignoutBtn.setOnClickListener {
            SignOutDialogFragment.newInstance().let { dialog ->
                dialog.isCancelable = false
                dialog.show(parentFragmentManager, "SignOutDialog")
            }
        }

        // 프로필 이미지 선택
        binding.userManageProfileBg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_FROM_ALBUM)
        }

        // 프로필 사진 변경 저장
        binding.mypageProfileSaveButton.setOnClickListener {
            val selectedImageUri = selectedImageUri

            if (selectedImageUri != null) {
                uploadImage(selectedImageUri) { imageUrl ->
                    saveImageUrlToDatabase(imageUrl) {
                        userProfileViewModel.setImageUrl(imageUrl)
                        Toast.makeText(requireContext(), "프로필 사진이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "프로필 사진을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun newInstance() = UserManageDialog()
    }

    // 갤러리 이미지 선택 후 동작
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            if (selectedImageUri != null) {
                Glide.with(this)
                    .load(selectedImageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.userManageProfileBg)
            }
        }
    }

    // Firebase Storage에 이미지 업로드
    private fun uploadImage(selectedImageUri: Uri, callback: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/${auth.currentUser?.uid}.jpg")

        imageRef.putFile(selectedImageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    callback(imageUrl)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "프로필 사진 업로드 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("ProfileImageSaveError", e.message.toString())
            }
    }

    // Firebase Realltime db에 이미지 URL 저장
    private fun saveImageUrlToDatabase(imageUrl: String, function: () -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = dbReference.child("users").child(userId)
            userRef.child("profileImageUrl").setValue(imageUrl)
        }
    }
}