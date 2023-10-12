package com.example.tripsync.model

data class PlanDetail(
    val date: String? = null,
    val title: String? = null,
    val group: List<String>? = null,
    val content: String? = null,
    val travelList: List<Travel>? = null
)