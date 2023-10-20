package com.example.tripsync.model

data class Plan(
    var title: String? = null,
    var planDetailList: List<PlanDetail>? = null,
)