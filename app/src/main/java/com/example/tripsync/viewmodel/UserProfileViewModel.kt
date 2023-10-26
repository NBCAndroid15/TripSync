package com.example.tripsync.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileViewModel : ViewModel() {
    private val imageUrlLiveData = MutableLiveData<String>()

    fun setImageUrl(imageUrl: String) {
        imageUrlLiveData.value = imageUrl
    }

    fun getImageUrl(): LiveData<String> {
        return imageUrlLiveData
    }

    // 이미지 URL을 Firebase Realltime db에서 가져오고 프로필 이미지로 설정
    fun loadImageUrlFromDatabase() {
        val dbReference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val userRef = dbReference.child("users").child(userId)
            userRef.child("profileImageUrl").addValueEventListener(object : ValueEventListener  {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val imageUrl = dataSnapshot.getValue(String::class.java)
                    if (imageUrl != null) {
                        setImageUrl(imageUrl)
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
    }
}