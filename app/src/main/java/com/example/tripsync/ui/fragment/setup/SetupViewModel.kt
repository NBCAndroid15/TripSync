package com.example.tripsync.ui.fragment.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SetupViewModel : ViewModel() {
    private val _setupTitle = MutableLiveData<String>()
    val setupTitle : LiveData<String> get() = _setupTitle

    fun updateSetupTitle(title: String) {
        _setupTitle.value = title
    }

}