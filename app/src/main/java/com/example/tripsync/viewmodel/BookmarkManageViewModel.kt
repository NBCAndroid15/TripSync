package com.example.tripsync.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsync.data.BookmarkRepositoryImpl
import com.example.tripsync.model.Travel
import kotlinx.coroutines.launch

class BookmarkManageViewModel(private val bookmarkRepositoryImpl: BookmarkRepositoryImpl) : ViewModel() {
    private val _bookmarkList = MutableLiveData<List<Travel>>()
    val bookmarkList: LiveData<List<Travel>>
        get() = _bookmarkList

    init {
        getBookmarkList()
    }

    private fun getBookmarkList() {
        viewModelScope.launch {
            _bookmarkList.value = bookmarkRepositoryImpl.getBookmarkList() ?: listOf()
        }
    }

    fun deleteBookmarkList(travel: Travel) {
        viewModelScope.launch {
            bookmarkRepositoryImpl.deleteBookmark(travel)
            _bookmarkList.value = bookmarkRepositoryImpl.getBookmarkList() ?: listOf()
        }
    }
}