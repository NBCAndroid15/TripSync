package com.trip.tripsync.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trip.tripsync.data.AuthRepositoryImpl

class MyPageViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
            return MyPageViewModel(com.trip.tripsync.data.AuthRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Illegal Argument Exception")
    }
}