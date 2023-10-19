package com.example.tripsync.ui.fragment.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.fragment.plan.TestModel
import com.naver.maps.geometry.LatLng
import com.prolificinteractive.materialcalendarview.CalendarDay

class SharedViewModel : ViewModel() {

    // setup에서 plan으로 타이틀을 전달하기 위한 라이브 객체
    private val _sharedTitle = MutableLiveData<String>()
    val sharedTitle: LiveData<String> get() = _sharedTitle

    // setup에서 plan으로 날짜를 전달하기 위한 라이브 객체
    private val _sharedDate = MutableLiveData<Set<CalendarDay>>()
    val sharedDate: LiveData<Set<CalendarDay>> get() = _sharedDate

    private val _planBookItem: MutableLiveData<List<TestModel>> = MutableLiveData(listOf())
    val planBookItem: LiveData<List<TestModel>> get() = _planBookItem

    private val _planSearchItem: MutableLiveData<List<TestModel>> = MutableLiveData()
    val planSearchItem: LiveData<List<TestModel>> get() = _planSearchItem

    private val _selectedLocation = MutableLiveData<List<TestModel>>()
    val selectedLocation: LiveData<List<TestModel>> get() = _selectedLocation

    fun udatePlanBookItem(item: Travel) {
        _planBookItem.value = (_planBookItem.value ?: emptyList()) + listOf(
            TestModel(
                imageUrl = item.imageUrl,
                title = item.title,
                addr = item.addr,
                area = item.area,
                mapX = item.mapX,
                mapY = item.mapY,
                category = item.category,
                tel = item.tel
                )
        )
    }

    fun updatePlanSearchItem(item: Travel) {
        _planSearchItem.value = (_planSearchItem.value ?: emptyList()) + listOf(
            TestModel(
                imageUrl = item.imageUrl,
                title = item.title,
                addr = item.addr,
                area = item.area,
                mapX = item.mapX,
                mapY = item.mapY,
                category = item.category,
                tel = item.tel,
            )
        )
    }

    fun updateSelectedLocation(latLng: TestModel) {
        _selectedLocation.value = listOf(latLng)
    }

    fun updateSharedTitle(title: String) {
        _sharedTitle.value = title
    }

    fun updateSharedDate(date: Set<CalendarDay>) {
        _sharedDate.value = date
    }




}