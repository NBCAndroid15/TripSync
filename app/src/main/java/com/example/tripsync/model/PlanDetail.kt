package com.example.tripsync.model

data class PlanDetail(
    var date: String? = null,
    var content: String? = null,
    var travelList: MutableList<Travel>? = null
)