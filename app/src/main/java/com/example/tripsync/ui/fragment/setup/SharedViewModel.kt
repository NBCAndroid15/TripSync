package com.example.tripsync.ui.fragment.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay

class SharedViewModel : ViewModel() {

    private val _sharedTitle = MutableLiveData<String>()
    val sharedTitle: LiveData<String> get() = _sharedTitle

    private val _sharedDate = MutableLiveData<Set<CalendarDay>>()
    val sharedDate: LiveData<Set<CalendarDay>> get() = _sharedDate

    fun updateSharedTitle(title: String) {
        _sharedTitle.value = title
    }

    fun updateSharedDate(date: Set<CalendarDay>) {
        _sharedDate.value = date
    }


}