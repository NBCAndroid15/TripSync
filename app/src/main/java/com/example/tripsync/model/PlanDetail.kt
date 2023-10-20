package com.example.tripsync.model

data class PlanDetail(
    var date: String? = null,
    var title: String? = null,
    var group: List<String>? = null,
    var content: String? = null,
    var travelList: List<Travel>? = null
)