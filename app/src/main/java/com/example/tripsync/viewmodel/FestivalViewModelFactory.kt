package com.example.tripsync.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripsync.data.TravelRepositoryImpl

class FestivalViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FestivalViewModel::class.java)) {
            return FestivalViewModel(TravelRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Illegal Argument Exception")
    }

}