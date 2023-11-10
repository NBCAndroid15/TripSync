package com.trip.tripsync.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.trip.tripsync.R
import com.trip.tripsync.data.AuthRepositoryImpl
import com.trip.tripsync.databinding.FragmentSignOutDialogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch

class SignOutDialogFragment : DialogFragment() {

    private var _binding: FragmentSignOutDialogBinding? = null
    private val binding: FragmentSignOutDialogBinding
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
        _binding = FragmentSignOutDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        binding.signoutDialogNoBtn.setOnClickListener {
            dismiss()
        }

        binding.signoutDialogYesBtn.setOnClickListener {
            signOutAccount()

            val sharedPreferences =
                requireContext().getSharedPreferences("AutoLoginPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("autoLogin").apply()

            parentFragmentManager
                .beginTransaction()
                .replace(R.id.main_frame, LoginFragment.newInstance())
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signOutAccount() {

        com.trip.tripsync.data.AuthRepositoryImpl().run {
            viewLifecycleOwner.lifecycleScope.launch {
                val result = unregister()
                if (result != null) {
                    deleteUserData()
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

    private fun deleteUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val dbReference = FirebaseDatabase.getInstance().reference
            val firestore = FirebaseFirestore.getInstance()
            val storage = FirebaseStorage.getInstance()

            // Firebase Realtime Database에서 데이터 삭제
            dbReference.child("users").child(userId).removeValue()

            // Firebase Firestore에서 데이터 삭제
            val usersCollection = firestore.collection("users")
            usersCollection.document(userId).delete()

            // Firebase Storage에서 데이터 삭제
            val storageRef = storage.reference.child("profile_images/$userId.jpg")
            storageRef.delete()
        }
    }

    companion object {
        fun newInstance() = SignOutDialogFragment()
    }
}