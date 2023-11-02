package com.example.tripsync.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tripsync.data.PlanRepositoryImpl
import com.example.tripsync.model.Plan
import kotlinx.coroutines.launch

class MyPlanViewModel(private val planRepositoryImpl: PlanRepositoryImpl) : ViewModel() {
    var planList: LiveData<List<Plan>> = planRepositoryImpl.getPlanListSnapshot().asLiveData()
    private var _editState: MutableLiveData<Boolean> = MutableLiveData(false)
    val editState: LiveData<Boolean>
        get() = _editState

    var sortOption = false

    fun getPlanListSnapshot() {
        planList = planRepositoryImpl.getPlanListSnapshot().asLiveData()
    }

    fun toggleEditState() {
        _editState.value = !(_editState.value!!)
    }


    fun deletePlan(plan: Plan) {
        viewModelScope.launch {
            planRepositoryImpl.deletePlan(plan)
        }
    }
}