package com.trip.tripsync.viewmodel.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.trip.tripsync.data.BookmarkRepositoryImpl

class BookmarkManageViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkManageViewModel::class.java)) {
            return BookmarkManageViewModel(BookmarkRepositoryImpl()) as T
        }
        throw IllegalArgumentException("Illegal Argument Exception")
    }
}