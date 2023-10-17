package com.example.tripsync.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsync.data.TravelRepositoryImpl
import com.example.tripsync.model.Travel
import kotlinx.coroutines.launch

class FestivalViewModel(private val travelRepositoryImpl: TravelRepositoryImpl)  : ViewModel() {
    private val _festivalData = MutableLiveData<List<Travel>>()
    val festivalData: LiveData<List<Travel>> = _festivalData

    fun getFestivalList() {
        viewModelScope.launch {
            val festivalList = travelRepositoryImpl.getFestivalInfo(pageNo = 1)
            val sortedFestivalList = festivalList.sortedByDescending { it.startDate }
            _festivalData.value = sortedFestivalList
        }
    }
}