package com.trip.tripsync.model

data class Plan(
    var title: String? = null,
    var group: List<String>? = null,
    var planDetailList: List<PlanDetail>? = null,
)