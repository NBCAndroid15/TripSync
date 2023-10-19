package com.example.tripsync.ui.fragment.setup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tripsync.model.Travel
import com.example.tripsync.model.User
import com.example.tripsync.ui.fragment.plan.TestModel
import com.naver.maps.geometry.LatLng
import com.prolificinteractive.materialcalendarview.CalendarDay

class SharedViewModel : ViewModel() {

    // setup에서 plan으로 타이틀을 전달하기 위한 라이브 객체
    private val _sharedTitle = MutableLiveData<String>()
    val sharedTitle: LiveData<String> get() = _sharedTitle

    // setup에서 plan으로 날짜를 전달하기 위한 라이브 객체
    private val _sharedDate = MutableLiveData<Set<CalendarDay>>()
    val sharedDate: LiveData<Set<CalendarDay>> get() = _sharedDate

    private val _planItems = MutableLiveData<List<TestModel>>()
    val planItems: LiveData<List<TestModel>> get() = _planItems

    private val _userNickName = MutableLiveData<List<User>>()
    val userNickName : LiveData<List<User>> get() = _userNickName

    fun getUserNickName(nickName: User) {
        val currentName = _userNickName.value.orEmpty().toMutableList()
        currentName?.add(nickName)
        _userNickName.value = currentName ?: listOf()
        Log.d("setup", _userNickName.value!!.size.toString())
    }

    fun updatePlanBookItem(item: Travel) {
        val currentItem = _planItems.value.orEmpty()

        if (currentItem.none { it.imageUrl == item.imageUrl}) {
            _planItems.value = currentItem + listOf(
                TestModel(
                    imageUrl = item.imageUrl,
                    title = item.title,
                    addr = item.addr,
                    area = item.area,
                    mapX = item.mapX,
                    mapY = item.mapY,
                    category = item.category,
                    tel = item.tel
                )
            )
        }
    }

    fun updatePlanSearchItem(item: Travel) {
        val currentItem = _planItems.value.orEmpty()

        if (currentItem.none { it.imageUrl == item.imageUrl}) {
            _planItems.value = currentItem + listOf(
                TestModel(
                    imageUrl = item.imageUrl,
                    title = item.title,
                    addr = item.addr,
                    area = item.area,
                    mapX = item.mapX,
                    mapY = item.mapY,
                    category = item.category,
                    tel = item.tel
                )
            )
        }

    }

    // planfragment에서 아이템을 삭제하기 위한 메서드
    fun planRemoveItem(item: TestModel) {
        val currentItem = _planItems.value.orEmpty().toMutableList()
        currentItem.remove(item)
        _planItems.value = currentItem
    }


    fun updateSharedTitle(title: String) {
        _sharedTitle.value = title
    }

    fun updateSharedDate(date: Set<CalendarDay>) {
        _sharedDate.value = date
    }




}