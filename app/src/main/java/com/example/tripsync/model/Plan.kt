package com.example.tripsync.model

data class Plan(
    var title: String? = null,
    var group: List<User>? = null,
    var planDetailList: List<PlanDetail>? = null,
)