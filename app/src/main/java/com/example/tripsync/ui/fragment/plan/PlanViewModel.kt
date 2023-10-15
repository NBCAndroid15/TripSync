package com.example.tripsync.ui.fragment.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel: ViewModel() {
    private val _planText = MutableLiveData<String>("your initial text")
    val planText: LiveData<String> get() = _planText

    fun updatePlanText(newText: String) {
        _planText.value = newText
    }

    fun getCurrentPlanText(): String? {
        return _planText.value
    }
}