package com.example.tripsync.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.model.User
import kotlinx.coroutines.launch

class MyPageViewModel (private val authRepositoryImpl: AuthRepositoryImpl) : ViewModel() {
    var curUser: User? = null

    init {
        viewModelScope.launch {
            curUser = authRepositoryImpl.getCurrentUserInfo()
        }
    }

    fun logout() {
        authRepositoryImpl.logout()
    }
}