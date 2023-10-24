package com.example.tripsync.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsync.data.PlanRepositoryImpl
import com.example.tripsync.model.Plan
import kotlinx.coroutines.launch

class MyPlanViewModel(private val planRepositoryImpl: PlanRepositoryImpl) : ViewModel() {
    private val _planList = MutableLiveData<List<Plan>>()
    val planList: LiveData<List<Plan>>
        get() = _planList


    fun getPlanList() {
        viewModelScope.launch {
            _planList.value = planRepositoryImpl.getPlanList() ?: listOf()
        }
    }


    fun deletePlan(plan: Plan) {
        viewModelScope.launch {
            planRepositoryImpl.deletePlan(plan)
            _planList.value = planRepositoryImpl.getPlanList() ?: listOf()
        }
    }
}