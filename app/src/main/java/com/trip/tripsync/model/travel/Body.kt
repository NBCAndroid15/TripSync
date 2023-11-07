package com.trip.tripsync.model.travel

data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)