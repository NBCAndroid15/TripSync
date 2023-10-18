package com.example.tripsync.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsync.data.RetrofitInstance.api
import com.example.tripsync.data.TravelRepositoryImpl
import com.example.tripsync.model.Travel
import kotlinx.coroutines.launch

class TravelViewModel(private val travelRepositoryImpl: TravelRepositoryImpl)  : ViewModel() {
    private val _travelData = MutableLiveData<List<Travel>>()
    val travelData: LiveData<List<Travel>> = _travelData

    private var currentKeyword = "전체"

    fun updateKeyword(newKeyword: String) {
        currentKeyword = newKeyword
        getTravelList()
    }

    fun getTravelList() {
        viewModelScope.launch {
            val travelList = travelRepositoryImpl.getTravelInfo(pageNo = 1, keyword = currentKeyword)
            _travelData.value = travelList
        }
    }
}