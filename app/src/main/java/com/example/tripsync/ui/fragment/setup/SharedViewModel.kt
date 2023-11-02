package com.example.tripsync.ui.fragment.setup

import android.content.ContentProvider
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsync.data.AuthRepositoryImpl
import com.example.tripsync.data.PlanRepositoryImpl
import com.example.tripsync.model.Plan
import com.example.tripsync.model.PlanDetail
import com.example.tripsync.model.Travel
import com.example.tripsync.model.User
import com.google.firebase.auth.FirebaseAuth
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    var _plan = Plan()
    var currentPosition = 0

    private val _planItems = MutableLiveData<List<Travel>>(_plan.planDetailList?.get(currentPosition)?.travelList)
    val planItems: LiveData<List<Travel>> get() = _planItems

    private val _userList = MutableLiveData<List<User>>()
    val userList : LiveData<List<User>> get() = _userList

    private val _memoList = MutableLiveData<List<String>>()

/*
*
* SetupFragment의 Titlebtn, Userbtn, check
* 아이템들의 visible 관리
*
* */

    private val _isTitleVisible = MutableLiveData(false)
    val isTitleVisible: LiveData<Boolean> get() = _isTitleVisible

    private val _isUserVisible = MutableLiveData(false)
    val isUserVisible: LiveData<Boolean> get() = _isUserVisible

    private val _isUserCheck = MutableLiveData(false)
    val isUserCheck: LiveData<Boolean> = _isUserCheck

    val _isDateSelected = MutableLiveData(false)

    private val _ishint = MutableLiveData(false)
    val ishint : LiveData<Boolean> = _ishint


    fun setHint(visible: Boolean) {
        _ishint.value = visible
    }

    fun setTitleVisible(visible: Boolean) {
        _isTitleVisible.value = visible
    }

    fun setUserVisible(visible: Boolean) {
        _isUserVisible.value = visible
    }

    fun setUserCheck(visible: Boolean) {
        _isUserCheck.value = visible
    }


    fun addMemo(memo: String) {
        val curremtMemo = _memoList.value.orEmpty().toMutableList()
        curremtMemo.add(memo)
        _plan.planDetailList?.getOrNull(currentPosition)?.content = memo
        _memoList.value = curremtMemo
    }

    // 초기 plan data class을 초기화시켜주는 메서드
    fun initPlan(title: String, size: Int, dateList: List<String>) {
        viewModelScope.launch {
            val curUser = AuthRepositoryImpl().getCurrentUserInfo()
            val owner = listOf(FirebaseAuth.getInstance().currentUser?.uid ?: "")

            _plan = Plan(title = title, planDetailList = mutableListOf<PlanDetail>().also { plan ->
                repeat(size) { idx ->
                    plan.add(PlanDetail().also { it.date = dateList[idx] })
                }
            }, group = owner)

            _userList.value = listOf(curUser ?: User())
        }
    }

    fun initPlan(plan: Plan) {
        viewModelScope.launch {
            _plan = plan
            _userList.value =
                AuthRepositoryImpl().getUserListByUidList(plan.group ?: listOf()) ?: listOf()
        }
    }

    fun updatePlan() {
        viewModelScope.launch {
            PlanRepositoryImpl().run {
                updatePlan(_plan)
            }
        }
    }

    fun initPosition(position: Int) {
        val planDetailList = _plan.planDetailList
        if (planDetailList != null && position >= 0 && position < planDetailList.size) {
            currentPosition = position
            _planItems.value =
                _plan.planDetailList?.getOrNull(currentPosition)?.travelList?.toMutableList()
            Log.d("position", currentPosition.toString())
        }
    }

    fun getUserNickName(user: User) {
        val currentName = _userList.value.orEmpty().toMutableList()
        if (!currentName.any { it.uid == user.uid }) {
            currentName.add(user)
            _userList.value = currentName.toList()
        }

        val groupList = _plan.group.orEmpty().toMutableList()
        if (!groupList.any { it == user.uid }) {
            groupList.add(user.uid ?: "")
            _plan.group = groupList.toList()
        }

    }

    fun updatePlanBookItem(item: Travel) {
        val currentItem = _planItems.value.orEmpty()

        if (currentItem.none { it.title == item.title }) {
            if (currentItem.size < 10) {
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
                _plan.planDetailList?.getOrNull(currentPosition)?.travelList =
                    _planItems.value?.toMutableList()
            } else {
            }
        }

    }

    fun updatePlanSearchItem(item: Travel) {
        val currentItem = _planItems.value.orEmpty()

        if (currentItem.none { it.title == item.title }) {
            if (currentItem.size < 10) {
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
                _plan.planDetailList?.get(currentPosition)?.travelList =
                    _planItems.value?.toMutableList()
            }
        }

    }

    // planfragment에서 아이템을 삭제하기 위한 메서드
    fun planRemoveItem(item: Travel) {
        val currentItem = _planItems.value.orEmpty().toMutableList()
        currentItem.remove(item)
        _planItems.value = currentItem
        _plan.planDetailList?.getOrNull(currentPosition)?.travelList = currentItem
    }


}