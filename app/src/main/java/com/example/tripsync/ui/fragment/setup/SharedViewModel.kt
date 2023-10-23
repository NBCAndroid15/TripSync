package com.example.tripsync.ui.fragment.setup

import android.util.Log
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

    private val _userNickName = MutableLiveData<List<String>>(_plan.group)
    val userNickName : LiveData<List<String>> get() = _userNickName

    private val _memoList = MutableLiveData<List<String>>()
    val memoList: LiveData<List<String>> get() = _memoList




    fun addMemo(memo: String) {
        val curremtMemo = _memoList.value.orEmpty().toMutableList()
        curremtMemo.add(memo)
        _plan.planDetailList?.get(currentPosition)?.content = memo
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

            _userNickName.value = listOf(curUser?.nickname ?: "")
        }
    }

    fun initPlan(plan: Plan) {
        viewModelScope.launch {
            _plan = plan
            _userNickName.value = AuthRepositoryImpl().getNameListByUidList(plan.group ?: listOf()) ?: listOf()
        }
    }

    fun updatePlan() {
        viewModelScope.launch {
            PlanRepositoryImpl().run {
                updatePlan(_plan)
            }
        }
    }

    fun initPosition( position: Int) {
        val planDetailList = _plan.planDetailList
        if (planDetailList != null && position >= 0 && position < planDetailList.size) {
            currentPosition = position
            _planItems.value = _plan.planDetailList?.get(currentPosition)?.travelList?.toMutableList()
            Log.d("position", currentPosition.toString())
        }
//        Log.d("initDate", _plan.planDetailList?.get(position)?.date ?: "null")
//        currentPosition = position
    }

    fun getUserNickName(nickName: User) {
        // 중복체크
        val currentName = _userNickName.value.orEmpty().toMutableList()
        currentName?.add(nickName.nickname ?: "")
        _userNickName.value = currentName ?: listOf()
        Log.d("setup", _userNickName.value!!.size.toString())
        _plan.group = _plan.group?.plus(listOf(nickName.uid ?: ""))
        Log.d("user", _plan.group?.toString() ?: "")
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
        _plan.planDetailList?.get(currentPosition)?.travelList = _planItems.value?.toMutableList()
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
        _plan.planDetailList?.get(currentPosition)?.travelList = _planItems.value?.toMutableList()
    }

    // planfragment에서 아이템을 삭제하기 위한 메서드
    fun planRemoveItem(item: Travel) {
        val currentItem = _planItems.value.orEmpty().toMutableList()
        currentItem.remove(item)
        _planItems.value = currentItem
        _plan.planDetailList?.get(currentPosition)?.travelList = currentItem
    }






}