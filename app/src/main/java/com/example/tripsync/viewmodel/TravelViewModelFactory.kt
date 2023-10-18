package com.example.tripsync.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripsync.data.PlanRepositoryImpl
import com.example.tripsync.data.TravelRepositoryImpl

class TravelViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelViewModel::class.java)) {
            return TravelViewModel(TravelRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Illegal Argument Exception")
    }

}
