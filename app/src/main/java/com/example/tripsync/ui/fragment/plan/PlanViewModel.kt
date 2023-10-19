package com.example.tripsync.ui.fragment.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel : ViewModel() {

    private val _planItems= MutableLiveData<List<TestModel>>()
    val planItems: LiveData<List<TestModel>> get() = _planItems

    fun setPlanItems(items: List<TestModel>) {
        _planItems.value = items
    }


    fun removeItem(item: TestModel) {
        val currentItems = _planItems.value.orEmpty().toMutableList()
        currentItems.remove(item)
        _planItems.value = currentItems

    }

}
