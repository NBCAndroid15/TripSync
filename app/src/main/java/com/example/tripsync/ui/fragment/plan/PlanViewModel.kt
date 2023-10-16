package com.example.tripsync.ui.fragment.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlanViewModel: ViewModel() {


    val itemList = mutableListOf(
        TestModel(1, "테스트1", 35.1795543, 129.0756416129),
        TestModel(2, "테스트2", 35.178501, 129.076096 ),
        TestModel(3, "테스트3", 35.179098, 129.076921),
        TestModel(4, "테스트4", 35.1795343, 129.0298872)
    )

}