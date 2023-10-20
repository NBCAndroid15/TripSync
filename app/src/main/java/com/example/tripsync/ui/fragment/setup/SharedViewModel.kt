package com.example.tripsync.ui.fragment.setup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tripsync.model.Plan
import com.example.tripsync.model.PlanDetail
import com.example.tripsync.model.Travel
import com.example.tripsync.model.User
import com.prolificinteractive.materialcalendarview.CalendarDay

class SharedViewModel : ViewModel() {

    // setup에서 plan으로 타이틀을 전달하기 위한 라이브 객체
    private val _sharedTitle = MutableLiveData<String>()
    val sharedTitle: LiveData<String> get() = _sharedTitle

    // setup에서 plan으로 날짜를 전달하기 위한 라이브 객체
    private val _sharedDate = MutableLiveData<Set<CalendarDay>>()
    val sharedDate: LiveData<Set<CalendarDay>> get() = _sharedDate

    private val _planItems = MutableLiveData<List<Travel>>()
    val planItems: LiveData<List<Travel>> get() = _planItems

    private val _userNickName = MutableLiveData<List<User>>()
    val userNickName : LiveData<List<User>> get() = _userNickName

    var _plan = Plan()
    var currentPosition = 0

    fun addPlanDetail(planDetail: PlanDetail) {
        val currentPlanDetails = _plan.planDetailList?.toMutableList() ?: mutableListOf()
        val selectedDateIndex = currentPosition

        if (selectedDateIndex < currentPlanDetails.size) {
            currentPlanDetails[selectedDateIndex] = planDetail
            _plan.planDetailList = currentPlanDetails
        }

    }

    // 초기 plan data class을 초기화시켜주는 메서드
    fun initPlan(title: String, size: Int, dateList: List<String>) {
        _plan = Plan(title= title, planDetailList = mutableListOf<PlanDetail>().also { plan ->
            repeat(size) { idx ->
                plan.add(PlanDetail().also { it.date = dateList[idx] })
            }
        })
    }

    fun initPosition( position: Int) {
        Log.d("initDate", _plan.planDetailList?.get(position)?.date ?: "null")
        currentPosition = position
    }

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
                Travel(
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
                Travel(
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

    fun updateMemo(memo: String) {

    }

    // planfragment에서 아이템을 삭제하기 위한 메서드
    fun planRemoveItem(item: Travel) {
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