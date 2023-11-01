package com.example.tripsync.model

data class Post(
    val title: String? = null,
    val content: List<ContentPost>? = null,
    val timeStamp: Long? = null,
    val postType: String? = null

    )