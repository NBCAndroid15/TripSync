package com.example.tripsync.ui.fragment.plan.plansearchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsync.data.TravelRepositoryImpl
import com.example.tripsync.model.Travel
import kotlinx.coroutines.launch

class SearchViewModel(private val travelRepositoryImpl: TravelRepositoryImpl): ViewModel() {

    private val _getSearchItem = MutableLiveData<List<Travel>>()
    val getSearchItem: LiveData<List<Travel>> get() = _getSearchItem

    fun updateSearchItem (keyword: String) {
        viewModelScope.launch {
            _getSearchItem.value = travelRepositoryImpl.getTravelInfo(1, keyword )
        }
    }
}