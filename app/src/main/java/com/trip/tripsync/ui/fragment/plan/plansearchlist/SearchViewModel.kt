package com.trip.tripsync.ui.fragment.plan.plansearchlist

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.tripsync.data.TravelRepositoryImpl
import com.trip.tripsync.model.Travel
import kotlinx.coroutines.launch
/*
*
* planfragment에서 검색을 하기 위한 search viewmodel
*
* */

class SearchViewModel(private val travelRepositoryImpl: TravelRepositoryImpl): ViewModel() {

    private val _getSearchItem = MutableLiveData<List<Travel>>()
    val getSearchItem: LiveData<List<Travel>> get() = _getSearchItem

    private var page = 1
    private var currentKeyword: String? = null
    fun updateSearchItem (keyword: String) {
        viewModelScope.launch {
            if ( currentKeyword != keyword) {
                _getSearchItem.value = emptyList()
                page = 1
            }
            val searchItem = travelRepositoryImpl.getTravelInfo(page, keyword )
            val resultItem = _getSearchItem.value.orEmpty().toMutableList()
            resultItem.addAll(searchItem)
            _getSearchItem.value = resultItem
            page++
            currentKeyword = keyword

        }
    }

    fun searchItemSorted(currentLocation : Location) {

        var sortedItems = listOf<Travel>()

        _getSearchItem.value?.let { items ->
            sortedItems = items.sortedBy { item ->
                val itemLocation = Location("itemLocation")
                itemLocation.latitude = item.mapY ?: 0.0
                itemLocation.longitude = item.mapX ?: 0.0
                val distance = currentLocation.distanceTo(itemLocation) / 1000
                distance
            }
        }

        _getSearchItem.value = sortedItems
    }

    fun searchItemAll() {
        updateSearchItem("")
    }


}