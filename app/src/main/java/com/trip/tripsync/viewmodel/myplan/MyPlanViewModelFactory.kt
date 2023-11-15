package com.trip.tripsync.viewmodel.myplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trip.tripsync.data.PlanRepositoryImpl

class MyPlanViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPlanViewModel::class.java)) {
            return MyPlanViewModel(PlanRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Illegal Argument Exception")
    }

}