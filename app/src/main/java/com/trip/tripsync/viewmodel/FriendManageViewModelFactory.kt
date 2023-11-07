package com.trip.tripsync.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trip.tripsync.data.AuthRepositoryImpl


class FriendManageViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendManageViewModel::class.java)) {
            return FriendManageViewModel(com.trip.tripsync.data.AuthRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Illegal Argument Exception")
    }
}
