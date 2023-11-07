package com.trip.tripsync.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.trip.tripsync.data.AuthRepositoryImpl
import com.trip.tripsync.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    var curUser: LiveData<User> = com.trip.tripsync.data.AuthRepositoryImpl().getUserSnapshot().asLiveData()

    fun getCurrentUserSnapshot() {
        curUser = com.trip.tripsync.data.AuthRepositoryImpl().getUserSnapshot().asLiveData()
    }

    fun uploadImage(selectedImageUri: Uri) {
        val auth = FirebaseAuth.getInstance()
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/${auth.currentUser?.uid}.jpg")

        viewModelScope.launch(Dispatchers.IO) {
            imageRef.putFile(selectedImageUri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        saveImageUrlToDatabase(imageUrl)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ProfileImageSaveError", e.message.toString())
                }
        }

    }

    private fun saveImageUrlToDatabase(imageUrl: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            viewModelScope.launch {
                com.trip.tripsync.data.AuthRepositoryImpl().updateProfileImg(imageUrl)
            }
        }
    }
}