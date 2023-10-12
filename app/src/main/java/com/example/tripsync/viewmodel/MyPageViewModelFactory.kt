package com.example.tripsync.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripsync.data.AuthRepositoryImpl

class MyPageViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
            return MyPageViewModel(AuthRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Illegal Argument Exception")
    }
}