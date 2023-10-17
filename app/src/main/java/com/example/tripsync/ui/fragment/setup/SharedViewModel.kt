package com.example.tripsync.ui.fragment.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tripsync.model.Travel
import com.example.tripsync.ui.fragment.plan.TestModel
import com.prolificinteractive.materialcalendarview.CalendarDay

class SharedViewModel : ViewModel() {

    private val _sharedTitle = MutableLiveData<String>()
    val sharedTitle: LiveData<String> get() = _sharedTitle

    private val _sharedDate = MutableLiveData<Set<CalendarDay>>()
    val sharedDate: LiveData<Set<CalendarDay>> get() = _sharedDate

    private val _planBookItem: MutableLiveData<List<TestModel>> = MutableLiveData(listOf())
    val planBookItem: LiveData<List<TestModel>> get() = _planBookItem

    private val _planSearchItem: MutableLiveData<List<TestModel>> = MutableLiveData()
    val planSearchItem: LiveData<List<TestModel>> get() = _planSearchItem

    fun udatePlanBookItem(item: Travel) {
        val currentList = planBookItem.value?.toMutableList() ?: mutableListOf()
        currentList.add(
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
        _planBookItem.value = currentList
    }

    fun updatePlanSearchItem(item: Travel) {
        val currentList = planSearchItem.value?.toMutableList() ?: mutableListOf()
        currentList.add(
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
        _planSearchItem.value = currentList
    }


    fun updateSharedTitle(title: String) {
        _sharedTitle.value = title
    }

    fun updateSharedDate(date: Set<CalendarDay>) {
        _sharedDate.value = date
    }




}