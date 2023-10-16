package com.example.tripsync.ui.fragment.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel: ViewModel() {
    private val _getTitle = MutableLiveData<String>()
    val getTitle: LiveData<String> get() = _getTitle

    fun updateGetTitle(title: String) {
        _getTitle.value = title
    }
}