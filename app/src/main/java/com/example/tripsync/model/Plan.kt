package com.example.tripsync.model

import com.example.tripsync.ui.fragment.plan.PlanViewType

data class Plan(
    val title: String? = null,
    val planDetailList: List<PlanDetail>? = null,
    val viewType: Int = PlanViewType.Normal.INT
)