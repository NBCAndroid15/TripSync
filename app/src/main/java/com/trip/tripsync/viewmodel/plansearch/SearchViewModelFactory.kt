package com.trip.tripsync.viewmodel.plansearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trip.tripsync.data.TravelRepositoryImpl

class SearchViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(TravelRepositoryImpl()) as T
        }
        throw IllegalArgumentException("ViewModel을 찾을 수 없습니다.")
    }
}