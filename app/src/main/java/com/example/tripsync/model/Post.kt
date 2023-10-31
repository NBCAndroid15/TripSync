package com.example.tripsync.model

data class Post (
    val title: String? = null,
    val memoNo: String? = null,
    val imageUrl: String? =null,
    val timeStamp: Long? = null,
    val user: List<String>? = null
)