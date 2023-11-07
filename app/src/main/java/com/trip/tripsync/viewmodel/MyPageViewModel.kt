package com.trip.tripsync.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.tripsync.data.AuthRepositoryImpl
import com.trip.tripsync.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.launch

class MyPageViewModel (private val authRepositoryImpl: com.trip.tripsync.data.AuthRepositoryImpl) : ViewModel() {
    private var _curUser: MutableLiveData<User?> = MutableLiveData<User?>()
    val curUser: LiveData<User?>
        get() = _curUser

    var googleSignInClient: GoogleSignInClient? = null

    init {
        viewModelScope.launch {
            _curUser.value = authRepositoryImpl.getCurrentUserInfo()
        }
    }

    fun logout() {
        authRepositoryImpl.logout()
        googleSignInClient?.signOut()
    }
}