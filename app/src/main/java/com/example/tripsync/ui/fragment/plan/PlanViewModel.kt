package com.example.tripsync.ui.fragment.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel: ViewModel() {
    private val _getPlanList = MutableLiveData<List<TestModel>>()
    val getPlanList: LiveData<List<TestModel>> get() = getPlanList

    fun updateList (item: TestModel) {
        _getPlanList.value = listOf(item)
    }



}