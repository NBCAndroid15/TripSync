package com.trip.tripsync.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.tripsync.data.BookmarkRepositoryImpl
import com.trip.tripsync.model.Travel
import kotlinx.coroutines.launch

class BookmarkManageViewModel(private val bookmarkRepositoryImpl: BookmarkRepositoryImpl) : ViewModel() {
    private val _bookmarkList = MutableLiveData<List<Travel>>()
    val bookmarkList: LiveData<List<Travel>>
        get() = _bookmarkList

    private val orgList = mutableListOf<Travel>()

    init {
        getBookmarkList()
    }

    fun getBookmarkList() {
        viewModelScope.launch {
            _bookmarkList.value = bookmarkRepositoryImpl.getBookmarkList() ?: listOf()
            orgList.clear()
            orgList.addAll(_bookmarkList.value!!)
        }
    }

    fun getBookmarkListWithFilter(filter: String) {
        if (filter == "전체") {
            _bookmarkList.value = orgList
        } else {
            _bookmarkList.value = orgList.filter {
                it.area?.contains(filter) ?: false
            }
        }

    }

    fun deleteBookmarkList(travel: Travel) {
        viewModelScope.launch {
            val result = bookmarkRepositoryImpl.deleteBookmark(travel)
            result?.let {
                _bookmarkList.value = it
            }
        }
    }
}