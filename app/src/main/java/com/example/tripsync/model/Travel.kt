package com.example.tripsync.model

data class Travel(
    val imageUrl: String? = null,
    val title: String? = null,
    val content: String? = null,
    val mapx: Double? = null,
    val mapy: Double? = null,
    val categoryId: Int? = null,
    val areaCode: Int? = null,
    val tel: String? = null
)
//위치추가