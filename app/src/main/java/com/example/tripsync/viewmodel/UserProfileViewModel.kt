package com.example.tripsync.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileViewModel : ViewModel() {
    var curUser: LiveData<User> = AuthRepositoryImpl().getUserSnapshot().asLiveData()

    fun getCurrentUserSnapshot() {
        curUser = AuthRepositoryImpl().getUserSnapshot().asLiveData()
    }
}