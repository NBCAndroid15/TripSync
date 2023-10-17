package com.example.tripsync.ui.fragment.plan.plansearchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tripsync.data.TravelRepositoryImpl

class SearchViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(TravelRepositoryImpl()) as T
        }
        throw IllegalArgumentException("ViewModel을 찾을 수 없습니다.")
    }
}