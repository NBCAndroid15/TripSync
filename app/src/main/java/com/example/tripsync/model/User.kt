package com.example.tripsync.model

data class User (
    val email : String? = null,
    val nickname : String? = null,
    val friends : List<User>? = null,
    val uid : String? = null,
    val profileImg: String? = null
)