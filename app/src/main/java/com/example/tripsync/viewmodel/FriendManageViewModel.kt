package com.example.tripsync.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.model.User
import kotlinx.coroutines.launch

class FriendManageViewModel(private val authRepositoryImpl: AuthRepositoryImpl) : ViewModel() {
    private var _curUser = MutableLiveData<User?>()
    val curUser: LiveData<User?>
        get() = _curUser

    private var _friendList = MutableLiveData<List<User>>()
    val friendList : LiveData<List<User>>
        get() = _friendList

    fun init() {
        getCurrentUserInfo()
        _friendList.value = listOf()
    }

    fun getFriendList(query: String) {
        viewModelScope.launch {
            _friendList.value = authRepositoryImpl.searchFriend(query) ?: listOf()
        }
    }

    fun addFriend(user: User) {
        viewModelScope.launch {
            val result = authRepositoryImpl.addFriend(user)

            if (result != null) {
                _curUser.value = result
            }
        }
    }

    private fun getCurrentUserInfo() {
        viewModelScope.launch {
            _curUser.value = authRepositoryImpl.getCurrentUserInfo()
        }
    }

    fun deleteFriend(user: User) {
        viewModelScope.launch {
            val result = authRepositoryImpl.deleteFriend(user)

            if (result != null) {
                _curUser.value = result
            }
        }
    }
}