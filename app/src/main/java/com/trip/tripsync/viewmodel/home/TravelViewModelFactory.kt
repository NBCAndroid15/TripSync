package com.trip.tripsync.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trip.tripsync.data.TravelRepositoryImpl

class TravelViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelViewModel::class.java)) {
            return TravelViewModel(TravelRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Illegal Argument Exception")
    }

}
